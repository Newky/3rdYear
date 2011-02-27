#######################
 
# consumeservice.py
# consumes a method in a service on the dbus
 
import dbus
 
bus = dbus.SessionBus()
proxySpk = bus.get_object('org.newky.proxySpk', '/org/newky/proxySpk')
hello = proxySpk.get_dbus_method('hello', 'org.newky.proxySpk')
print hello("richy")
