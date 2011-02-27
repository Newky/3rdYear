#!/usr/bin/env python
#
# [SNIPPET_NAME: Tree View Column]
# [SNIPPET_CATEGORIES: PyGTK]
# [SNIPPET_DESCRIPTION: Using tree view columns]
# [SNIPPET_DOCS: http://www.pygtk.org/pygtk2tutorial/ch-TreeViewWidget.html, http://www.pygtk.org/docs/pygtk/class-gtktreeviewcolumn.html]

# example treeviewcolumn.py

import pygtk
pygtk.require('2.0')
import gtk
import dbus
import dbus.service
from dbus.mainloop.glib import DBusGMainLoop

class TreeViewColumnExample(dbus.service.Object):

    # close the window and quit
    def delete_event(self, widget, event, data=None):
        gtk.main_quit()
        return False

    def make_pb(self, tvcolumn, cell, model, iter):
        stock = model.get_value(iter, 1)
        pb = self.treeview.render_icon(stock, gtk.ICON_SIZE_MENU, None)
        cell.set_property('pixbuf', pb)
        return

    def __init__(self):
        # Create a new window
        self.window = gtk.Window(gtk.WINDOW_TOPLEVEL)

        self.window.set_title("TreeViewColumn Example")
	
        bus_name = dbus.service.BusName('org.newky.proxySpk', bus=dbus.SessionBus())
        dbus.service.Object.__init__(self, bus_name, '/org/newky/proxySpk')

        #self.window.set_size_request(200, 200)

        self.window.connect("delete_event", self.delete_event)
	
	self.scrolled_window = gtk.ScrolledWindow(hadjustment=None, vadjustment=None)
	#self.scrolled_window.set_policy(POLICY_ALWAYS, POLICY_ALWAYS)
	self.scrolled_window.show()
        # create a liststore with one string column to use as the model
        self.liststore = gtk.ListStore(str, str, str, 'gboolean')

        # create the TreeView using liststore
        self.treeview = gtk.TreeView(self.liststore)
	
	self.scrolled_window.add_with_viewport(self.treeview)
        # create the TreeViewColumns to display the data
        self.tvcolumn = gtk.TreeViewColumn('Method')
        self.tvcolumn1 = gtk.TreeViewColumn('Path')
        self.tvcolumn2 = gtk.TreeViewColumn('Protocol')

        # add a row with text and a stock item - color strings for
        # the background
        #self.liststore.append(['Open', gtk.STOCK_OPEN, 'Open a File', True])
        #self.liststore.append(['New', gtk.STOCK_NEW, 'New File', True])
        #self.liststore.append(['Print', gtk.STOCK_PRINT, 'Print File', False])

        # add columns to treeview
        self.treeview.append_column(self.tvcolumn)
        self.treeview.append_column(self.tvcolumn1)
        self.treeview.append_column(self.tvcolumn2)

        # create a CellRenderers to render the data
        #self.cellpb = gtk.CellRendererPixbuf()
        self.cell = gtk.CellRendererText()
        self.cell1 = gtk.CellRendererText()
        self.cell2 = gtk.CellRendererText()

        # set background color property
        #self.cellpb.set_property('cell-background', 'yellow')
        self.cell.set_property('cell-background', 'cyan')
        self.cell1.set_property('cell-background', 'pink')
        self.cell2.set_property('cell-background', 'white')


        # add the cells to the columns - 2 in the first
        #self.tvcolumn.pack_start(self.cellpb, False)
        self.tvcolumn.pack_start(self.cell, True)
        self.tvcolumn1.pack_start(self.cell1, True)
        self.tvcolumn2.pack_start(self.cell2, True)

        # set the cell attributes to the appropriate liststore column
        # GTK+ 2.0 doesn't support the "stock_id" property
        #if gtk.gtk_version[1] < 2:
            #self.tvcolumn.set_cell_data_func(self.cellpb, self.make_pb)
        #else:
            #self.tvcolumn.set_attributes(self.cellpb, stock_id=1)
        self.tvcolumn.set_attributes(self.cell, text=0, cell_background_set=1)
        self.tvcolumn1.set_attributes(self.cell1, text=1,cell_background_set=2)
        self.tvcolumn2.set_attributes(self.cell2, text=2,cell_background_set=3)

        # make treeview searchable
        self.treeview.set_search_column(0)

        # Allow sorting on the column
        self.tvcolumn.set_sort_column_id(0)

        # Allow drag and drop reordering of rows
        self.treeview.set_reorderable(True)

        self.window.add(self.scrolled_window)

        self.window.show_all()

    @dbus.service.method('org.newky.proxySpk')
    def append_to_list(self,request_str):
	self.method, self.path, self.protocol = request_str.split()
	print "Trying to append something %s %s %s."%(self.method , self.path , self.protocol)
    	self.liststore.append([self.method, self.path, self.protocol, True])

def main():
    gtk.main()

if __name__ == "__main__":
    DBusGMainLoop(set_as_default=True)
    tvcexample = TreeViewColumnExample()
    main()



