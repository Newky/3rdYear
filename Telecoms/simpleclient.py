#!/usr/bin/python           # This is client.py file

import socket               # Import socket module
import threading
import time
import random

class MadClient(threading.Thread):
	def __init__(self, sock, port):
		self.s = sock
		self.p = port
		self.host = socket.gethostname()
		self.s.connect((self.host, self.p))
		time.sleep( random.randint(0, 5))
		print self.s.recv(1024)
		self.s.close()                     # Close the socket when done


if __name__ == "__main__" :
	for i in range(0, 10):
		MadClient(socket.socket(), 8080);

