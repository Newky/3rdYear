#!/usr/bin/env python
import gtk
def responseToDialog(entry, dialog, response):
	dialog.response(response)

def getText():
	#base this on a message dialog
	dialog = gtk.MessageDialog(
		None,
		gtk.DIALOG_MODAL | gtk.DIALOG_DESTROY_WITH_PARENT,
		gtk.MESSAGE_QUESTION,
		gtk.BUTTONS_OK,
		None)
	dialog.set_markup('Please enter your host to black_list:')
	#create the text input field
	entry = gtk.Entry()
	#allow the user to press enter to do ok
	#entry.connect("activate", responseToDialog, dialog, gtk.RESPONSE_OK)
	#create a horizontal box to pack the entry and a label
	hbox = gtk.HBox()
	hbox.pack_start(gtk.Label("Name:"), False, 5, 5)
	hbox.pack_end(entry)
	#some secondary text
	#add it and show it
	dialog.vbox.pack_end(hbox, True, True, 0)
	dialog.show_all()
	#go go go
	dialog.run()
	text = entry.get_text()
	dialog.destroy()
	return text

