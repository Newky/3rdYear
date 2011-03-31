#!/usr/bin/env python

import sys
import re
import os
import smtplib

validate = "^.+\\@(\\[?)[a-zA-Z0-9\\-\\.]+\\.([a-zA-Z]{2,3}|[0-9]{1,3})(\\]?)$"

def get_email(msg):
	user = raw_input(msg);

	while re.match(validate, user) == None:
		print "Incorrect email"
		user = raw_input("To:");

	return user

def get_body():
	line= raw_input("Body:\n");
	body = line + "\n"

	while(line != ".") :
		line = raw_input();
		body += line + "\n"

	return body

def gen_keys(user):
	key_dir = ".keys/%s" %(user)
	if not os.path.exists(key_dir):
		os.makedirs(key_dir)
		command = "openssl genrsa -out %s/private.pem 1024" %(key_dir)
		os.popen(command)
		command = "openssl rsa -in %s/private.pem -out %s/public.pem -outform PEM -pubout" %(key_dir, key_dir)
		os.popen(command)
	else:
		return

def sign_digest(mail, user):
	key_dir = ".keys/%s" %(user)
	#command = "openssl dgst -sha"
	priv_key_path = key_dir + "/" + "private.pem"
	command = "echo '%s' | openssl dgst -sha1 -sign %s" %(mail, priv_key_path)
	p=os.popen(command)
	return p.read()

def encrypt_body(mail, user, password = None):
	key_dir = ".keys/%s" %(user)
	priv_key_path = key_dir + "/" + "private.pem"
	if password is None:
		password = raw_input("Passphrase:")
	command = "echo '%s' | openssl enc -aes-256-cbc -a -salt -pass pass:%s" %(mail, password)
	p=os.popen(command)
	return (p.read(), password)

def set_up_smtp_link(user):
	pwd = raw_input("Password for gmail auth:")
	smtpserver = smtplib.SMTP("smtp.gmail.com",587)
	smtpserver.ehlo()
	smtpserver.starttls()
	smtpserver.ehlo
	smtpserver.login(user, pwd)
	return smtpserver

if __name__ == "__main__":
	to = get_email("To:")
	sender = get_email("From:")

	subject = raw_input("Subject:")
	mail = get_body()

	print "To:" + to
	print "From:" + sender
	print "Subject:" + subject
	print "Body:\n" + mail

	#command = "echo '%s' | openssl dgst -sha1 " %(mail)
	#p = os.popen(command)

	#digest= p.read();
	gen_keys(sender)
	#Sign the digest with private key, can be verified by your public key 
	digest_signed = sign_digest(mail, sender)
	#Garbled.
	#digest_signed
	
	(encrypted_body,passphrase) = encrypt_body(mail, sender)
	(encrypt_subject, passphrase) = encrypt_body(subject, sender, passphrase)

	smtpserver = set_up_smtp_link(sender)
	
	header = "To:" + to + "\nFrom:"+ sender + "\nSubject:"+encrypt_subject +"\n";

	msg = header + encrypted_body 
	
	smtpserver.sendmail(sender, to, msg)
	smtpserver.close()

