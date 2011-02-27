#!/usr/bin/python

# size.py

import wx

class Size(wx.Frame):
    def __init__(self, parent, id, title):
        wx.Frame.__init__(self, parent, id, title, size=(250, 200))
	
	self.Centre()
        self.Show(True)

class Test(wx.Frame):
	def __init__(self, parent, id, title):
		wx.Frame.__init__(self, parent, id, title, size= (250, 200))
		self.test = wx.ListCtrl(self, style = wx.LC_REPORT | wx.LC_NO_HEADER)
		self.columns = {
				"Type": 60,
				"URL": 300, 
				"Proto": 80
				}
		count = 0
		for x,y in self.columns.items(): 
		    self.test.InsertColumn(count, '%s' % (x))
		    self.test.SetColumnWidth(count, y)
		    count += 1

		for i in range(0, 1000, 3):
		    index = self.test.InsertStringItem(self.test.GetItemCount(), "")
		    count=0
		    for x,y in self.columns.iteritems():
			self.test.SetStringItem(index, count,x )
			count += 1

		self.Show()

	def append_to_list(self, req_str):
	    index = self.test.InsertStringItem(self.test.GetItemCount(), "")
	    parts = req_str.split(' ')
	    count =0
	    for x in parts:
	      self.test.SetStringItem(index,count,x)
	      count += 1

if __name__ == "__main__":
	app = wx.App()
	#Size(None, -1, 'Size')
	app.TopWindow =  Test(None, -1,"Proxy Manager")
	app.MainLoop()
