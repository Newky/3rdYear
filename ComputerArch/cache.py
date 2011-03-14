#!/usr/bin/env python
import sys
class cache:
	
	def __init__(self, way, l, n):
		'''
		Init array of size n * way
		'''
		print str(way) + " Way Cache"
		self.store = []
		#Invalid Data
		self.n = n
		for i in range(0, n):
			self.store.append([])
		self.way = way
		self.hit = 0
		self.miss = 0

	def put(self, address):
		'''
		Address will be for instance

		0000

		First 2 are tag plus the Way MSB's of 3,The remainder of 3 is the set number and the final is an offset.
	
		E.g

		2-way
		Address 00c5
		
		Tag is 00, And the offset is 5, the set number is the 2 LSB's of c which are 1100 so 00 is the set wanted.

		Goto that offset in self.store, check if the tag is there. if not remove LRU and replace it with current tag. if no valid data is there, remove 
		0000 0000 1100 0101
		'''
		sys.stdout.write("\n"+address.rstrip()+"\t")
		chosen_set = self.check_set(address[2])
		#print "chosen set is " + str(chosen_set)
		sys.stdout.write(str(chosen_set)+"\t")
		tag = self.tag(address)
		#If the cache isn't full
		if tag in self.store[chosen_set]:
			sys.stdout.write("HIT\t")
			self.hit += 1
			if self.store[chosen_set].index(tag) == len(self.store[chosen_set]) - 1 :
				#Sort the LRU problem
				temp = self.store[chosen_set].pop()
				self.store[chosen_set].append(temp)
		else:
			if len(self.store[chosen_set]) < self.way:
				self.store[chosen_set].append(self.tag(address))
				#print "Miss, added to cache"
				sys.stdout.write("Miss, added to cache\t")
				self.miss += 1
			else:
				self.store[chosen_set].pop()
				self.store[chosen_set].append(self.tag(address))
				#print "Miss, added to cache, removed LRU"
				sys.stdout.write("Miss, added to cache, removed LRU")
				self.miss += 1
	
	def tag(self, address):
		shift_no = 8 - self.way
		'''
		192 = 11000000
		in 1 way 
		shift_no is 7
		'''
		num_address = int(address, 16)

		for i in range(0, shift_no):
			num_address >>= 1;
		return num_address

	def check_set(self, set_var):
		'''
		in this case u will get the set variable i.e if the address is 00c0 then u will get c
		'''
		full_byte = int(set_var, 16)
		'''	
		We want to set the first Way bits to 0
		d is c
		'''
		return full_byte % self.n

	def print_cache(self):
		count = 0;
		print "Cache DUMP\n"
		for cache_line in self.store:
			print "\nSet " + str(count)
			for x in cache_line:
				print str( int(str(x), 16))
			print "End of Set "+str(count)
			count+= 1

	def print_ratio(self):
		print "\nHit:%d Miss:%d" %(self.hit, self.miss)

def read_data(c):
	f = open("addresses", "r")
	for line in f.readlines():
		if not "pc" in line:
			c.put(line)
		else:
			c.print_cache()

	f.close()

if __name__ == "__main__":
	c = cache(8, 16,1)
	read_data(c)
	c.print_ratio()

