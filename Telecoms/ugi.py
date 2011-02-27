import sys
import gtk
import random
import thread	    # Import threading module
import dbus
import dbus.service
from dbus.mainloop.glib import DBusGMainLoop

requests = ("CONNECT", "GET", "POST", "DELETE")
urls = ("http://www.google.com/", "http://www.facebook.com/", "http://www.rte.ie/")
http_version = 'HTTP/1.1'
DBusGMainLoop(set_as_default=True)

class TutorialTextEditor(dbus.service.Object):

    def on_window_destroy(self, widget, data=None):
        gtk.main_quit()

    def on_button1_clicked(self, widget):
	print "proxy stopped"
     
    def on_button2_clicked_old(self, widget):
	print "proxy started"
	self.treestore = gtk.TreeStore(str)
	self.treeview.set_model(self.treestore)
	#for i in range(0, 50):
		#self.treestore.append(None, [random.choice(requests) + " " + random.choice(urls) + " "+http_version])	
	 ## create the TreeViewColumn to display the data 
        self.tvcolumn = gtk.TreeViewColumn('Request') 
        ## add tvcolumn to treeview 
        #self.treeview.append_column(self.tvcolumn) 
        # create a CellRendererText to render the data 
        self.cell = gtk.CellRendererText() 
        # add the cell to the tvcolumn and allow it to expand 
        self.tvcolumn.pack_start(self.cell, True) 
        # set the cell "text" attribute to column 0 - retrieve text 
        # from that column in treestore 
        self.tvcolumn.add_attribute(self.cell, 'text', 0) 
        # make it searchable 
        self.treeview.set_search_column(0) 
        # Allow sorting on the column 
        self.tvcolumn.set_sort_column_id(0) 
        # Allow drag and drop reordering of rows 
        self.treeview.set_reorderable(True) 

    def on_button2_clicked(self, widget):
	print "proxy started"
	self.model = gtk.ListStore(str)
	self.treeview.set_model(self.model)
	self.cell = gtk.CellRendererText()
	self.col = gtk.TreeViewColumn("Column 1")
	self.col.pack_start(self.cell, True)
	self.col.add_attribute(self.cell, 'text', 0) 
        # make it searchable 
        self.treeview.set_search_column(0) 
        # Allow sorting on the column 
        self.col.set_sort_column_id(0) 
        # Allow drag and drop reordering of rows 
        self.treeview.set_reorderable(True) 

    @dbus.service.method('org.newky.proxySpk')
    def append_to_list(self,request_str):
	print "Trying to append something."+request_str
    	self.model.append([request_str])
    	
    def __init__(self):
        bus_name = dbus.service.BusName('org.newky.proxySpk', bus=dbus.SessionBus())
        dbus.service.Object.__init__(self, bus_name, '/org/newky/proxySpk')
        builder = gtk.Builder()
        builder.add_from_file("proxygui_test.glade") 
        
        self.window = builder.get_object("window1")
	self.treeview = builder.get_object("treeview1")
        builder.connect_signals(self)       

    def main(self):
	    self.window.show()
	    gtk.main()

if __name__ == "__main__":
    editor = TutorialTextEditor()
    editor.main();
