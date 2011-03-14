#!/usr/bin/python 
import socket               # Import socket module
import thread	    # Import threading module
import select
import sys
import os 
import gtk
import random
import ugi
import dbus
import dbus.service
from dbus.mainloop.glib import DBusGMainLoop

#Multiple of the readsize
readBuf = 8192 
#Just a http version
http_version = 'HTTP/1.1'

#Black list will be a dictionary object
black_list = {}

#Set up Dbus Session Bus
bus = dbus.SessionBus()
proxySpk = bus.get_object('org.newky.proxySpk', '/org/newky/proxySpk')
#DBUS uses the append_to_list method to append a request to the gui's list.
append_to_list = proxySpk.get_dbus_method('append_to_list', 'org.newky.proxySpk')

#Variable to turn caching on or off
caching = True

#Main method of proxy server, this get's called once at start
class ProxyServer():
	def __init__(self, port):
		#open a socket
		self.s = socket.socket(socket.AF_INET)
		#Host and port as desired
		self.host = "localhost"
		self.port= port
		#This fixes problem of reusing address, causes address in use error otherwise.
		self.s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
		# bind the socket to the host and port
		self.s.bind((self.host, self.port))
		print "Proxy ready and listening on port %d ..." %(self.port)
		# Listen for all connections on that socket.
		self.s.listen(0)
		while 1:
			#If incoming connection accept and start a new Request thread
			c, addr = self.s.accept()     
			#Start new request thread.
			thread.start_new_thread(new_request, (c, addr))

#This function is used so that the thread
#doesnt get passed a class, which doesn't exit properly
def new_request(c, addr):
	#Make a new request
	r = Request(c, addr)
	#Do the request
	r.do()
	print "Exiting Request"
	

#Class which does all the needed request
#Both to server and back to client browser.
class Request:
	def __init__(self, conn, addr):
		print "Started Request" 
		#setup classes variables for connection client and addr
		self.conn = conn
		self.addr = addr
		self.buf = ""
		self.timeout = 60

	def do(self):
		#the default format of request is something like
		#GET http://www.google.com/ HTTP/1.1
		#This splits it up into the different sections from the header data
		self.method, self.path, self.protocol = self.get_header()
		if self.method == "CONNECT":
			self.on_connect()
		elif self.method in ('OPTIONS', 'GET', 'HEAD', 'POST', 'PUT',
                             'DELETE', 'TRACE'):
			self.on_other_method()
		self.conn.close()
		
	def get_header(self):
		'''
		receives the request from the browser
		and reads the first line of request
		the method, path and protocol
		The rest of the headers from the browser get stored in self.buf and are sent on later on.
		'''
		while 1:
			self.buf += self.conn.recv(readBuf)
			end = self.buf.find('\n')
			if end != -1:
				break;
		#Append the request line to the GUI
		append_to_list(self.buf[:end+1]);
		data = (self.buf[:end+1]).split()
		return data

	def on_connect(self):
		'''
		This function is for a CONNECT request
		It simply connects to the server requested
		And issues a Connection Established to the browser
		And looks for read write
		'''
		#Connects look lik this CONNECT mail.google.com:443 HTTP/1.1
		self.connect_to_target(self.path)
		self.conn.send ( http_version + " 200 Connection established\n" +
				"Proxy version: Richys\n\n")
		self.buf = ""
		self._read_write()

	def on_other_method(self):
		'''
		This is for every type of REQUEST bar CONNECT
		'''
		#strip http://
		self.path = self.path[7:]
		#extract host name
		i = self.path.find("/")
		host = self.path[:i]
		#extract path
		path = self.path[i:]

		data = None
		#Caching toggle
		if caching:
			data = self.check_cache(self.path)
		#Ignoring the cachin toggle, if data means that cached data was found
		if data and caching:
			#Each cached file has the headers in it
			#But I don't think we need to strip these headers now.
			#data = self.strip_header(data)
			print "Returning cached data"
			self.conn.send(data)
		else:
			#If no Cached File is found
			self.connect_to_target(host)
			#Send target our request
			self.target.send("%s %s %s\n" %(self.method, path, self.protocol) + self.buf)
			self.buf = ""
			#Read Write Response loop
			self._read_write()
			#Again caching toggle
			if caching:
				print "Cached "+ self.path

	#used to strip the header from the cached file
	def strip_header(self, data):
		start = data.find("Content-Type:")
		#if start == -1:
			#start = data.find("<html")
			#if start == -1:
				#start = data.find("<HTML")
		#if start != -1:
			#data = data[start:]
		#data = self.strip_footer( data)
		start = data.find("\n", start)
		data = data[start:]
		return data

	def strip_footer(self, data):
		start = data.find("</HTML")
		if start == -1:
			start = data.find("</html")
		if start != -1:
			data = data[:start+7]
		return data


	#Main read write loop
	def _read_write(self):
		'''
		This feeds select the two different sockets, 

		The client socket(ie connection with browser)

		The server socket(ie connection with website server you looking for.)
		'''
		time_out_max = self.timeout/3
		socs = [self.conn, self.target]
		count = 0
		while 1:
		    count += 1
		    (recv, _, error) = select.select(socs, [], socs, 3)
		    if error:
			break
		    if recv:
			for in_ in recv:
			    data = in_.recv(readBuf)
			    if in_ is self.conn:
				out = self.target
			    else:
				out = self.conn
			    if data:
				out.send(data)
				if out != self.target and caching: 
					self.cache(data, self.path)
				count = 0
		    if count == time_out_max:
			break
	

	def check_cache(self, path):
		'''
		Check the path to see if its been cached
		This is basically a series of checks for existance on the os.
		'''
		filename = 'cached/' + path
		#This is a "hack" for paths which have no extension
		#i.e www.google.com/ I append index to these directories as to have somewhere
		#to store the response
		if os.path.isdir(filename):
			filename += "index"
		print "Checking "+filename
		#If the path exists then by definition it is cached
		if os.path.exists(filename):
			print "Found it"+filename
			#Open the file and read it into data
			f= open(filename, "r")
			data = f.read()
			f.close()
			return data
		else:
			print filename + " Not Found in cache."
			return False 

	def connect_to_target(self, host):
		'''
		Makes a connection to the server of the host
		'''
		i = host.find(":")
		if i == -1:
			port = 80
		else:
			port = host[i+1:]
			host = host[:i]
		'''
		Get addr info, gets the ip address among a host of other information
		Soc_family tells the type of connection to make to the server
		'''

		(soc_family, _, _, _, address) = socket.getaddrinfo(host, port)[0]
		self.target = socket.socket(soc_family)
		old_port = port
		port = address[1]
		if(port != old_port):
			print "Not the same"
		#This is my implementation of blacklisting
		#If the ip is in hash, replace it.
		realaddress= if_block_replace(str(address[0]))
		if realaddress != str(address[0]):
			print "Blacklisted"
		newad= (realaddress, port)
		self.target.connect(newad)
		#self.target.connect(address)

	
	def cache(self, data, path):
		'''
		Cache a file, 
		Similar to checking cache except instead of checking for existance, 
		We make it exist.
		'''
		filename = 'cached/' + path
		#print "caching "+path 
		dir_exists(filename)
		# No file name
		if os.path.isdir(filename):
			filename += "index";
		f= open(filename, "a")
		f.write(data)
		f.close()

def dir_exists(f):
	'''
	Powerful little function which makes directories
	From a filename
	'''
	d = os.path.dirname(f)
	if not os.path.exists(d):
		os.makedirs(d)

def if_block_replace(address):
	'''
	Iterate through hash of black list
	And return the value from a key
	'''
	for x,y in black_list.iteritems():
		if x == address:
			return y
	return address

def load_blacklist():
	'''
	Load Blacklist from file
	'''
	f = open('black.list', 'r')
	for line in f.readlines():
		if line.find('#') == -1 and line != "":
			parts = line.split(":")
			black_list[parts[0]] = parts[1]

def print_blacklist():
	print "Blacklist"
	for x,y in black_list.iteritems():
		print x + " " +y;

if __name__ == "__main__" :
	load_blacklist()
	print_blacklist()
	DBusGMainLoop(set_as_default=True)
	if len(sys.argv) > 1:
		if sys.argv[1] == "--disable-cache":
			caching = False
	ProxyServer(8080)
