#Simple Library for RSA encryption in python
#Deals with all the large number woes.


def gcd(a, b):
	"""
	Compute gcd of two numbers
	"""

	if b == 0 : 
		return a
	else:
		return gcd(b, a % b)

def get_keys(p, q):
	'''
	Generate Public and Private Keys 
	Given two prime numbers p and q
	'''

	n = (p * q)

	tot = (p-1) * (q-1)

	#Generate some value e such that tot and e are relatively prime

	e = 3

	while 1:
		if gcd(tot, e) == 1: break
		else: e += 2

	d = extended_gcd(tot, e)

	return ( (n, e), (n, d) )

def extended_gcd(a, b):
	origA = a
	X = 0
	prevX = 1
	Y = 1
	prevY = 0

	while b != 0:

		temp = b
		quotient = a/b
		b = a % b
		a = temp

		temp = X
		a = prevX - quotient * X
		prevX = temp

		temp = Y
		Y = prevY - quotient * Y
		prevY = temp

	return origA + prevY	

