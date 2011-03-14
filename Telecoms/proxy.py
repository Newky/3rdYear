#!/usr/bin/python 
import socket               # Import socket module
import thread	    # Import threading module
from urlparse import urlparse
import select
import sys
import os 
import dbus
import dbus.service
from dbus.mainloop.glib import DBusGMainLoop

#Multiple of the readsize
readBuf = 8192 
#Just a http version
http_version = 'HTTP/1.1'

timeout = 60

#Black list will be a dictionary object
black_list = {}

#Set up Dbus Session Bus
bus = dbus.SessionBus()
proxySpk = bus.get_object('org.newky.proxySpk', '/org/newky/proxySpk')
append_to_list = proxySpk.get_dbus_method('append_to_list', 'org.newky.proxySpk')


class Proxy():
	def __init__(self, port):
		#Make a socket to connect to the browser
		self.s = socket.socket(socket.AF_INET) 
		self.host = "localhost"
		self.port= port
		#Stop the address from being locked by OS
		self.s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
		#Bind the socket to the port
		self.s.bind((self.host, self.port))
		print "Proxy Started\nListening on %d\nOn %s\n" %(self.port,self.host)		
		#Listen for all connections on that port
		self.s.listen(0)
		while 1:
			c, addr = self.s.accept() 
			thread.start_new_thread(new_request, (c, addr))

def new_request(c, addr):
	#Addr not used.
	#C is a connection with the browser or whatever is connection on the relevant port
	#Get header sent from Browser
	(data, buf)= get_header(c)
	method, path, protocol  = data
	if not "http://" in path:
		path = "http://" + path
	if method == "CONNECT":
		do_connect(c, path)
	elif method in ('OPTIONS', 'GET', 'HEAD', 'POST', 'PUT',
                             'DELETE', 'TRACE'):
                other_method(method,protocol, buf, c, path)
        c.close()
        #print "Thread Exiting "+ path

def do_connect(conn, path):
	serv_sock = connect_server(path)
	serv_sock.send ( http_version + " 200 Connection established\nProxy version: Richys\n\n")
	read_write(conn, serv_sock)

def other_method(method, protocol, buf, conn, fpath):
	#if "http://" in fpath:
		#fpath = fpath[7:]
	
	#This is where caching can take place or be checked.
	#i = fpath.find("/")
	#host = fpath[:i]
	#path= fpath[i:]
	data = check_cache(fpath)
	serv_sock = connect_server(fpath)
	serv_sock.send ("%s %s %s\n" %(method, fpath, protocol) + buf)
	read_write(conn, serv_sock, fpath)

def read_write_old(client, server):
	while 1:
		data = server.recv(readBuf)
		if not data:
			break;
		client.send(data)
		"Sent Client "+data

def read_write(client, server, path):
	time_out_max = timeout/3
	socs = [client, server]
	count = 0
	while 1:
	    count += 1
	    (recv, _, error) = select.select(socs, [], socs, 3)
	    if error:
		break
	    if recv:
		for in_ in recv:
		    data = in_.recv(readBuf)
		    if in_ is client:
			out = server 
		    else:
			out = client 
		    if data:
			out.send(data)
			#print "Cached "+ path
			cache(data, path)
			count = 0
	    if count == time_out_max:
		break
		
def cache(data, path):
		if "http://" in path:
			path = path[7:]
		filename = 'cached/' + path
		print "caching "+path 
		dir_exists(filename)
		# No file name
		if os.path.isdir(filename):
			filename += "index";
		f= open(filename, "a")
		f.write(data)
		f.close()

def dir_exists(f):
	d = os.path.dirname(f)
	if not os.path.exists(d):
		os.makedirs(d)

def check_cache(path):
		filename = 'cached/' + path
		if os.path.isdir(filename):
			filename += "index"
		print "Checking "+filename
		if os.path.exists(filename):
			print "Found it"+filename
			f= open(filename, "r")
			data = f.read()
			f.close()
			return data
		else:
			print filename + " Not Found in cache."
			return False 

def connect_server(path):
	o = urlparse(path)
	if not o.port:
		port = 80
	else:
		port = o.port
	(soc_family, _, _, _, address)= socket.getaddrinfo(o.netloc, port)[0]
	serv_sock = socket.socket(soc_family)
	server_ip = address[0]
	server_ip = if_block_replace(server_ip)
	print "Address of Path %s Netloc %s is %s on %d" %(path, o.netloc, server_ip, port)
	serv_sock.connect((server_ip, port))
	return serv_sock

def if_block_replace(address):
	for x,y in black_list.iteritems():
		if x == address:
			return y
	return address


def get_header(conn):
	buf = ""
	while 1:
		buf += conn.recv(readBuf)
		end = buf.find('\n')
		if end != -1:
			break;
	print "%s" %buf[:end]
	append_to_list(buf[:end+1]);
	data = (buf[:end+1]).split()
	return (data, buf)

def load_blacklist():
	f = open('black.list', 'r')
	for line in f.readlines():
		if line.find('#') == -1 and line != "":
			parts = line.split(":")
			black_list[parts[0]] = parts[1]
	

if __name__ == "__main__" :
	load_blacklist()
	DBusGMainLoop(set_as_default=True)
	Proxy(8080)
