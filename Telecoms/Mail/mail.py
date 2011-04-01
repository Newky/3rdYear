#!/usr/bin/env python

import sys
import re
import os
import smtplib
import imaplib
import mimetypes
import email
from email import encoders
from email.MIMEMultipart import MIMEMultipart
from email.MIMEBase import MIMEBase
from email.MIMEText import MIMEText
from email.MIMEAudio import MIMEAudio
from email.MIMEImage import MIMEImage


validate = "^.+\\@(\\[?)[a-zA-Z0-9\\-\\.]+\\.([a-zA-Z]{2,3}|[0-9]{1,3})(\\]?)$"

def get_email(msg):
	user = raw_input(msg);

	while re.match(validate, user) == None:
		print "Incorrect email"
		user = raw_input(msg);

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
	priv_key_path = key_dir + "/" + "private.pem"
	command = "openssl dgst -sha1 -sign %s -out %s %s " %(priv_key_path, "/tmp/digest","/tmp/mail")
	p=os.popen(command)
	return 

def encrypt_body(mail, user, password = None):
	key_dir = ".keys/%s" %(user)
	priv_key_path = key_dir + "/" + "private.pem"
	if password is None:
		password = raw_input("Passphrase:")
	command = "openssl enc -aes-256-cbc -a -salt -in %s -pass pass:%s" %("/tmp/mail", password)
	p=os.popen(command)
	return (p.read(), password)

def set_up_smtp_link(user):
	pwd = raw_input("Password for gmail auth:")
	smtpserver = smtplib.SMTP("smtp.gmail.com",587)
	#smtpserver = smtplib.SMTP("localhost",587)
	smtpserver.ehlo()
	smtpserver.starttls()
	smtpserver.ehlo
	smtpserver.login(user, pwd)
	return smtpserver

def set_up_imap_link(sender):
	key_dir = ".keys/%s" %(sender)
	pub_key_path = key_dir + "/" + "public.pem"
	pwd = raw_input("Password for gmail auth:")
	#imap = imaplib.IMAP4_SSL("localhost", 993)
	imap = imaplib.IMAP4_SSL("imap.gmail.com", 993)
	imap.login(sender, pwd)
	status, count = imap.select("INBOX")
	status, data = imap.fetch(count[0], '(RFC822)')
	msg = email.message_from_string(data[0][1])
	for part in msg.walk():
		if part.get_content_type() == "text/plain":
			e_body = part.get_payload(decode=True)
		if part.get_content_type() == "application/octet-stream":
			e_digest = part.get_payload(decode=True)
		print part.get_content_type()
	pwd = raw_input("Passphrase:")
	command = "echo '%s' | openssl enc -d -aes-256-cbc -a -out %s -pass pass:%s" %(e_body,"/tmp/mail",pwd)
	p=os.popen(command)
	command = "echo '%s' | openssl enc -d -aes-256-cbc -a -pass pass:%s" %(msg["Subject"],pwd)
	p=os.popen(command)
	print "Subject: %s" %(p.read())
	p.close()
	f = open("/tmp/digest", "w")
	f.write(e_digest)
	f.close()
	command = "openssl dgst -sha1 -verify %s -signature %s %s" %(pub_key_path,"/tmp/digest", "/tmp/mail")
	p=os.popen(command)
	e_digest = p.read()
	if(re.match("Verification Failure", e_digest)):
		print "FATAL: email has been tampered with, "
	imap.close()
	imap.logout()

if __name__ == "__main__":
	action = raw_input("Send(s)|Receive(r):")
	if action == "s":
		to = get_email("To:")
		sender = get_email("From:")

		subject = raw_input("Subject:")
		mail = get_body()

		f = open("/tmp/mail", "w")
		f.write(mail)
		f.close()
		
		gen_keys(sender)
		#Sign the digest with private key, can be verified by your public key 
		digest_signed = sign_digest(mail, sender)
		#Garbled.
		#digest_signed
		(encrypted_body,passphrase) = encrypt_body(mail, sender)
		(encrypt_subject, passphrase) = encrypt_body(subject, sender, passphrase)

		msg = MIMEMultipart()
		msg["To"] = to
		msg["From"] = sender
		msg["Subject"] = encrypt_subject

		msg.attach(MIMEText(encrypted_body, 'plain'))
		file_part = MIMEBase('application', "octet-stream")
		file_part.set_payload(open("/tmp/digest", "rb").read());
		encoders.encode_base64(file_part)
		file_part.add_header('Content-Disposition', 'attachment; filename="%s"'
                       % "/tmp/digest")
		msg.attach(file_part)
		
		smtpserver = set_up_smtp_link(sender)
		smtpserver.sendmail(sender, to, msg.as_string())
		smtpserver.close()

	elif action == "r":		
		sender = get_email("Login:")
		imap = set_up_imap_link(sender)
		
		
