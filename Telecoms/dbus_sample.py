# myservice.py
# simple python-dbus service that exposes 1 method called hello()
 
import gtk
import dbus
import dbus.service
from dbus.mainloop.glib import DBusGMainLoop
 
class MyDBUSService(dbus.service.Object):
    def __init__(self):
        bus_name = dbus.service.BusName('org.newky.proxySpk', bus=dbus.SessionBus())
        dbus.service.Object.__init__(self, bus_name, '/org/newky/proxySpk')
 
    @dbus.service.method('org.newky.proxySpk')
    def hello(self, name):
        return "Hello,"+str(name)+"!"
 
DBusGMainLoop(set_as_default=True)
myservice = MyDBUSService()
gtk.main()
