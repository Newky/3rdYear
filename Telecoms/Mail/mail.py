#!/usr/bin/env python
import sys
import re
import os
import smtplib
import imaplib
import mimetypes
import email
from getpass import getpass
from email import encoders
from email.MIMEMultipart import MIMEMultipart
from email.MIMEBase import MIMEBase
from email.MIMEText import MIMEText

#Premade regex for email validation
validate = "^.+\\@(\\[?)[a-zA-Z0-9\\-\\.]+\\.([a-zA-Z]{2,3}|[0-9]{1,3})(\\]?)$"
#URL of ca server
ca_server = "http://netsoc.tcd.ie/~richdel/CA"


def cleanup():
	'''
	Cleanup the temporary files
	'''
	command = "rm /tmp/mail /tmp/email /tmp/epass /tmp/subj /tmp/esubj /tmp/digest"
	os.popen(command)
	return

def get_email(msg):
	'''
	Get an email address from user using validation
	'''
	user = raw_input(msg);
	while re.match(validate, user) == None:
		print "Incorrect email"
		user = raw_input(msg);
	return user

def get_subject():
	'''
	Get subject of message write it to a temporary file
	'''
	subject = raw_input("Subject:")
	f = open("/tmp/subj", "w")
	f.write(subject)
	f.close()
	return 

def get_body():
	'''
	Get body of file and write it to a temp file
	'''
	line= raw_input("Body:\n");
	body = line + "\n"

	while(line != ".") :
		line = raw_input();
		body += line + "\n"


	f = open("/tmp/mail", "w")
	f.write(body)
	f.close()

	return 

def gen_keys(user):
	'''
	Generate a key pair for new users of the system
	'''
	key_dir = ".keys/%s" %(user)
	if not os.path.exists(key_dir):
		os.makedirs(key_dir)
		command = "openssl genrsa -out %s/private.pem 1024" %(key_dir)
		os.popen(command)
		command = "openssl rsa -in %s/private.pem -out %s/public.pem -outform PEM -pubout" %(key_dir, key_dir)
		os.popen(command)
	else:
		return

def get_pub_key(user):
	'''
	Get public key, first get CA cert off server, next get requested cert of server, verify the requested cert, 
	if verified extract public key from the cert
	'''
	command = "wget -O %s %s/cacert.pem 2> %s" %("/tmp/cacert.pem", ca_server, "/dev/null")
	os.popen(command)
	command = "wget -O %s %s/%s/cert.pem 2> %s" %("/tmp/usercert.pem", ca_server, user, "/dev/null")
	os.popen(command)
	command = "openssl verify -CAfile %s %s" %("/tmp/cacert.pem", "/tmp/usercert.pem")
	output= os.popen(command).read()
	if "OK" in output:
		command = "openssl x509 -inform pem -in %s -pubkey -noout > %s" %("/tmp/usercert.pem", "/tmp/userpub.pem")
		os.popen(command)
		return "/tmp/userpub.pem"
	else:
		print "Failure to verify"
		return None

def get_private_key(user):
	'''
	Get the private key path
	'''
	key_dir = ".keys/%s" %(user)
	return  key_dir + "/" + "private.pem"

def encrypt_pass(passphrase, user):
	'''
	Get public key of the user you are sending it to and
	encrypt the passphrase used in AES encryption using 
	the public key of the recpient
	'''
	pub_key_path = get_pub_key(user)
	command = "echo '%s' | openssl rsautl -encrypt -pubin -inkey %s -out %s" %(passphrase, pub_key_path, "/tmp/epass")
	os.popen(command)
	return 
	
def decrypt_pass(user):
	'''
	undos the encrypt_pass method
	'''
	priv_key_path = get_private_key(user)
	command = "openssl rsautl -decrypt -inkey %s -in %s" %( priv_key_path, "/tmp/epass")
	p=os.popen(command)
	return p.read()

def sign_digest(mail, user):
	'''
	Sign and generate the sha1 hash digest of the message with
	the senders public key
	'''
	key_dir = ".keys/%s" %(user)
	priv_key_path = key_dir + "/" + "private.pem"
	command = "openssl dgst -sha1 -sign %s -out %s %s " %(priv_key_path, "/tmp/digest","/tmp/mail")
	p=os.popen(command)
	return 

def encrypt_body(mail, user, password = None):
	'''
	Encrypt the body of the message, 
	This uses AES encryption using a passphrase
	'''
	if password is None:
		password = getpass("Passphrase:")
	command = "openssl enc -aes-256-cbc -a -salt -in %s -pass pass:%s" %(mail, password)
	p=os.popen(command)
	return (p.read(), password)

def set_up_smtp_link(user):
	'''
	Set up the smtp link to gmail servers
	Do all the connection stuff and return
	the stmp link object
	'''
	pwd = getpass("Password for gmail auth:")
	#smtpserver = smtplib.SMTP("smtp.gmail.com",587)
	smtpserver = smtplib.SMTP("localhost",587)
	smtpserver.ehlo()
	smtpserver.starttls()
	smtpserver.ehlo
	smtpserver.login(user, pwd)
	return smtpserver

def set_up_imap_link(sender):
	pwd = raw_input("Password for gmail auth:")
	imap = imaplib.IMAP4_SSL("localhost", 993)
	#imap = imaplib.IMAP4_SSL("imap.gmail.com", 993)
	imap.login(sender, pwd)
	return imap

def get_message(imap):
	'''
	This function handles the retrieval method
	It retrieves the message and does the necessary
	decryption, verifying the necessary things.
	Prints the message at the end.
	'''
	status, count = imap.select("INBOX")
	status, data = imap.fetch(count[0], '(RFC822)')
	msg = email.message_from_string(data[0][1])
	toggle = False
	#Walk through the different parts
	for part in msg.walk():
		if part.get_content_type() == "text/plain":
			e_body = part.get_payload(decode=True)
		if part.get_content_type() == "application/octet-stream":
			if toggle:
				e_digest = part.get_payload(decode=True)
			else:
				open("/tmp/epass", "w").write(part.get_payload(decode=True))
				pwd= decrypt_pass(sender)
				toggle = not toggle
	#Decrypt Subject and Body			
	command = "echo '%s' | openssl enc -d -aes-256-cbc -a -out %s -pass pass:%s" %(e_body,"/tmp/mail",pwd)
	p=os.popen(command)
	command = "echo '%s' | openssl enc -d -aes-256-cbc -a -pass pass:%s" %(msg["Subject"],pwd)
	p=os.popen(command)
	subj = p.read()
	mail = open("/tmp/mail", "r").read()
	p.close()

	f = open("/tmp/digest", "w")
	f.write(e_digest)
	f.close()
	#Verify and decrypt the digest
	command = "openssl dgst -sha1 -verify %s -signature %s %s" %(get_pub_key(msg["From"]),"/tmp/digest", "/tmp/mail")
	p=os.popen(command)
	e_digest = p.read()
	#Check the verification
	if(re.match("Verification Failure", e_digest)):
		print "FATAL: email has been tampered with, "
	imap.close()
	imap.logout()
	#Print decrypted message.
	print "To:%s\n" %(msg["To"])
	print "From:%s\n" %(msg["From"])
	print "Subject:\n%s\n" %(subj)
	print "Body:\n%s\n" %(mail)

if __name__ == "__main__":
	action = raw_input("Send(s)|Receive(r):")
	if action == "s":
		to = get_email("To:")
		sender = get_email("From:")
		subject = get_subject()
		mail = get_body()

		#generate private and public keys for sender if not already done.
		gen_keys(sender)
		#Sign the digest with private key, can be verified by your public key 
		digest_signed = sign_digest(mail, sender)
		#encrypt body and subject.
		(encrypted_body,passphrase) = encrypt_body("/tmp/mail", sender)
		(encrypt_subject, passphrase) = encrypt_body("/tmp/subj", sender, passphrase)
		#Start making message
		msg = MIMEMultipart()
		msg["To"] = to
		msg["From"] = sender
		msg["Subject"] = encrypt_subject
		#Encrypt the password
		encrypt_pass(passphrase, to)
		#Attach the body
		msg.attach(MIMEText(encrypted_body, 'plain'))
		#The attachments are application octet-stream
		#The attachments for the password
		file_part = MIMEBase('application', "octet-stream")
		file_part.set_payload(open("/tmp/epass", "rb").read());
		encoders.encode_base64(file_part)
		file_part.add_header('Content-Disposition', 'attachment; filename="%s"'
                       % "passphrase")
		msg.attach(file_part)
		#The attachments  for the digest
		file_part = MIMEBase('application', "octet-stream")
		file_part.set_payload(open("/tmp/digest", "rb").read());
		encoders.encode_base64(file_part)
		file_part.add_header('Content-Disposition', 'attachment; filename="%s"'
                       % "digest")
		msg.attach(file_part)
		#Set up the smtp server
		smtpserver = set_up_smtp_link(sender)
		#Send the mail
		smtpserver.sendmail(sender, to, msg.as_string())
		smtpserver.close()
		#Cleanup some of the temporary files
		cleanup()
	elif action == "r":		
		#get the email account to use
		sender = get_email("Login:")
		#Set up the imap 
		imap = set_up_imap_link(sender)
		get_message(imap)
		
		
