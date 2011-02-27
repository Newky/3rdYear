#!/usr/bin/python           # This is server.py file

import socket               # Import socket module
import thread	    # Import threading module
import select
import sys
import gtk
import random
import ugi
import dbus

readBuf = 8192 
http_version = 'HTTP/1.1'

black_list = {
	"209.85.143.104": "localhost",
	"174.132.225.106" : "209.85.143.104"
}

bus = dbus.SessionBus()
proxySpk = bus.get_object('org.newky.proxySpk', '/org/newky/proxySpk')
append_to_list = proxySpk.get_dbus_method('append_to_list', 'org.newky.proxySpk')
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
		print "Proxy ready and listening..."
		self.s.listen(0)
		while 1:
			#If incoming connection accept and start a new Request thread
			c, addr = self.s.accept()     
			thread.start_new_thread(Request, (c, addr))
			#Request( c, addr ).start()

class Request:
	def __init__(self, conn, addr):
		print "Started Request"
		#setup classes variables for connection client and addr
		self.conn = conn
		self.addr = addr
		self.buf = ""
		self.timeout = 60
		#the default format of request is something like
		#GET http://www.google.com/reader/api/0/unread-count?output=json HTTP/1.1
		#This splits it up
		self.method, self.path, self.protocol = self.get_header()
		if self.method == "CONNECT":
			self.on_connect()
		elif self.method in ('OPTIONS', 'GET', 'HEAD', 'POST', 'PUT',
                             'DELETE', 'TRACE'):
			self.on_other_method()
		self.conn.close()                

	def get_header(self):
		while 1:
			self.buf += self.conn.recv(readBuf)
			end = self.buf.find('\n')
			if end != -1:
				break;
		print "%s" %self.buf[:end]
		append_to_list(self.buf[:end+1]);
		data = (self.buf[:end+1]).split()
		return data

	def on_connect(self):
		#Connects look lik this CONNECT mail.google.com:443 HTTP/1.1
		self.connect_to_target(self.path)
		self.conn.send ( http_version + " 200 Connection established\n" +
				"Proxy version: Richys\n\n")
		self.buf = ""
		self._read_write()

	def on_other_method(self):
		#These will look something like this
		#GET http://talkgadget.google.com/talkgadget/channel/bind?VER=8&clid=6DCBBCAE25C29B5E&gsessionid=j6usJErsrakRaIrAxHtulg&prop=iGoogle&SID=AF7FD803B539D123&RID=59805&TYPE=terminate&zx=a2oruux51ige HTTP/1.1
		#strip http://
		self.path = self.path[7:]
		#extract host name
		i = self.path.find("/")
		host = self.path[:i]
		#extract path
		path = self.path[i:]
		self.connect_to_target(host)
		self.target.send("%s %s %s\n" %(self.method, path, self.protocol) + self.buf)
		self.buf = ""
		self._read_write()

	def _read_write(self):
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
				count = 0
		    if count == time_out_max:
			break
		
	#Host in this case will look something lik mail.google.com:443
	def connect_to_target(self, host):
		i = host.find(":")
		if i == -1:
			port = 80
		else:
			port = host[i+1:]
			host = host[:i]
		(soc_family, _, _, _, address) = socket.getaddrinfo(host, port)[0]
		self.target = socket.socket(soc_family)
		port = address[1]
		realaddress= if_block_replace(str(address[0]))
		newad= (realaddress, port)
		self.target.connect(newad)
		#self.target.connect(address)

def if_block_replace(address):
	for x,y in black_list.iteritems():
		if x == address:
			return y
	return address

if __name__ == "__main__" :
	ProxyServer(8080)
