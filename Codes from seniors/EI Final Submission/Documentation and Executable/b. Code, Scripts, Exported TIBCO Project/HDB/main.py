#!/usr/bin/env python
#
# Copyright 2007 Google Inc.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

import xml.parsers.expat
from model import hdb
from google.appengine.ext import webapp
from google.appengine.ext.webapp.util import run_wsgi_app

from xml.dom.minidom import parse, parseString
#http://wiki.python.org/moin/MiniDom
#http://localhost:8086/?vehicleno=1&carpark=1111
#http://localhost:8086/?vehicleno=1&carpark=1111

def getSeasonParking(carparkID,carplate) :
	query= hdb.gql('where carparkID = :1 and carplate = :2',carparkID,carplate)
	#query2 = hdb().gql('where carplate = :1', plate)
	return query

			
#@staticmethod		
class bootstrap(webapp.RequestHandler):
    def get(self):
    	#hdb1 = hdb(carparkID='1',carplate='1234',carparkType='mutistory',vehicle_type='CAR',owner_type='owner',purchase_type='resident',start_date='2012-01-01',end_date='2012-02-01')
    	hdb1 = hdb(carparkID='BIBE36',carplate='SJA1234Z',carparkType='mutistory',vehicle_type='CAR',owner_type='owner',purchase_type='resident',start_date='2012-01-01',end_date='2012-06-01')
    	hdb2 = hdb(carparkID='BIBE37',carplate='SJA1234Z',carparkType='opencarpark',vehicle_type='CAR',owner_type='owner',purchase_type='resident',start_date='2012-01-01',end_date='2012-06-01')
        hdb3 = hdb(carparkID='BIBE38',carplate='SJA1234Z',carparkType='mutistory',vehicle_type='CAR',owner_type='owner',purchase_type='resident',start_date='2012-01-01',end_date='2012-06-01')
        hdb1.put()
        hdb2.put()
        hdb3.put()
     
     	hdb4 = hdb(carparkID='BIBE36',carplate='SJB5678Z',carparkType='mutistory',vehicle_type='CAR',owner_type='owner',purchase_type='resident',start_date='2012-01-01',end_date='2012-06-01')
        hdb5 = hdb(carparkID='BIBE36',carplate='SJC9999Z',carparkType='opencarpark',vehicle_type='CAR',owner_type='owner',purchase_type='resident',start_date='2012-01-01',end_date='2012-03-01')
        hdb4.put()
        hdb5.put()
        #hdb2 = hdb(carparkID='1',carplate='1111',seasonExpiry='2012-03-29')
    	#hdb2.put()
    	#hdb3 = hdb(carparkID='2',carplate='2222',seasonExpiry='2012-03-29')
    	#hdb3.put()
        self.response.out.write("Done Creating data into Datastore")

class MainPage(webapp.RequestHandler):
    def get(self):
        
        carplate = topicid = self.request.get('vehicleno')
        carparkID = topicid = self.request.get('carpark')
        self.response.headers['Content-Type'] = 'application/xml'
        #self.response.out.write('hello')
        #bootstrap()
        #dom1 = parse('order.xml') # parse an XML file by name

        datasource = open('template.xml')
        dom = parse(datasource)   # parse an open file

        if (getSeasonParking(carparkID,carplate).count() > 0):
            veh = getSeasonParking(carparkID,carplate).get()
            #dom3 = parseString('<myxml>Some data<empty/> some more data</myxml>')
            
            x = dom.createElement("vehicle_no")
            txt = dom.createTextNode(veh.carplate)
            x.appendChild(txt) 
            dom.childNodes[0].appendChild(x) 
            
            
            x = dom.createElement("vehicle_type")
            txt = dom.createTextNode(veh.vehicle_type)
            x.appendChild(txt)
            dom.childNodes[0].appendChild(x)
            
            x = dom.createElement("owner_type")
            txt = dom.createTextNode(veh.owner_type)
            x.appendChild(txt)
            dom.childNodes[0].appendChild(x)
            
            x = dom.createElement("parking")
            x.setAttribute("id", veh.carparkID)
            x.setAttribute("type", veh.carparkType)

            x2 = dom.createElement("purchase_type")
            txt = dom.createTextNode(veh.purchase_type)
            x2.appendChild(txt)
            x.appendChild(x2)
            
            x2 = dom.createElement("start_date")
            txt = dom.createTextNode(veh.start_date)
            x2.appendChild(txt)
            x.appendChild(x2)
            
            x2 = dom.createElement("end_date")
            txt = dom.createTextNode(veh.end_date)
            x2.appendChild(txt)
            x.appendChild(x2)

               
            dom.childNodes[0].appendChild(x) 

            self.response.out.write(dom.toxml())
        else:
             self.response.out.write("null")
        
        #self.response.out.write(dom.toxml())
        #bootstrap()

        #abc = getSeasonParking("1","1111")
        #self.response.out.write("Zhenguang" + str(abc.carplate))

application = webapp.WSGIApplication(
                                     [('/', MainPage),('/bootstrap', bootstrap)],
                                     debug=True)

def main():
    run_wsgi_app(application)

if __name__ == "__main__":
    main()
