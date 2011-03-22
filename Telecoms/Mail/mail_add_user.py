import sys
import os
import rsa

script_name = "mail_add_user.py"

def create_keys_for_username(username, pwd):
	curr_dir = os.getcwd()
	key_dir = curr_dir + "/.keys/" + username + "/"
	if os.path.exists(key_dir):
		print "user exists."
	else:	
		os.makedirs(key_dir)
		f = open(key_dir + "config", "w");
		f.write("username: %s \npassword %s" %(username, pwd))
		f.close()
		(pub, priv) = rsa.get_keys(4007,6037)
		f = open(key_dir + "priv.key", "w");
		f.write(str(priv))
		f.close
		f = open(key_dir + "pub.key", "w");
		f.write(str(pub))
		f.close
		print "user added!"

if __name__ == "__main__":
	if len(sys.argv) < 3:
		print "USAGE ./%s <username> <password>\n" %(script_name);
	else:
		username= sys.argv[1]
		pwd = sys.argv[2]
		create_keys_for_username(username, pwd)

