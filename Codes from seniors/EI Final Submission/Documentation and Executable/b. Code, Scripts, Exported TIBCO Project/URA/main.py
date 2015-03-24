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
from model import ura
from google.appengine.ext import webapp
from google.appengine.ext.webapp.util import run_wsgi_app

from xml.dom.minidom import parse, parseString
#http://wiki.python.org/moin/MiniDom
#http://localhost:8086/?vehicleno=1&carpark=1111
#http://localhost:8086/?vehicleno=1&carpark=1111

def getSeasonParking(id,plate) :
	query= ura.gql('where parking_id = :1 and vehicle_no = :2',id,plate)
	#query2 = ura().gql('where carplate = :1', plate)
	return query

			
#@staticmethod		
class bootstrap(webapp.RequestHandler):
    def get(self):
	ura1 = ura(parking_id='U001',vehicle_no='SJA1234Z',relationship_owner='owner',owner_type='resident',parking_place='ANG MO KIO AVENUE 1 PARK',session_class='E1',session_time='7PM to 7AM (24hrs)',session_rate='$60',commencement_date='2012-01-01',end_date='2012-06-01')
	ura2 = ura(parking_id='U002',vehicle_no='SJA1234Z',relationship_owner='owner',owner_type='resident',parking_place='ANG MO KIO AVENUE 2 PARK',session_class='E1',session_time='7PM to 7AM (24hrs)',session_rate='$60',commencement_date='2012-01-01',end_date='2012-06-01')
	ura3 = ura(parking_id='U003',vehicle_no='SJA1234Z',relationship_owner='owner',owner_type='resident',parking_place='ANG MO KIO AVENUE 3 PARK',session_class='E1',session_time='7PM to 7AM (24hrs)',session_rate='$60',commencement_date='2012-01-01',end_date='2012-06-01')
	ura1.put()
	ura2.put()
	ura3.put()
	#ura2 = ura(carparkID='1',carplate='1111',seasonExpiry='2012-03-29')
	#ura2.put()
	#ura3 = ura(carparkID='2',carplate='2222',seasonExpiry='2012-03-29')
	#ura3.put()
	self.response.out.write("Done Creating data into Datastore")

class MainPage(webapp.RequestHandler):
    def get(self):
        
        carparkID = topicid = self.request.get('vno')
        carplate = topicid = self.request.get('ppcode')
        self.response.headers['Content-Type'] = 'application/xml'
        #self.response.out.write('hello')
        #bootstrap()
        #dom1 = parse('order.xml') # parse an XML file by name

        datasource = open('template.xml')
        dom = parse(datasource)   # parse an open file

        if (getSeasonParking(carplate,carparkID).count() > 0):
            veh = getSeasonParking(carplate,carparkID).get()
            #dom3 = parseString('<myxml>Some data<empty/> some more data</myxml>')
            
            x = dom.createElement("vehicle_no")
            txt = dom.createTextNode(veh.vehicle_no)
            x.appendChild(txt) 
            dom.childNodes[0].appendChild(x) 
            
            
            x = dom.createElement("relationship_owner")
            txt = dom.createTextNode(veh.relationship_owner)
            x.appendChild(txt)
            dom.childNodes[0].appendChild(x)
            
            x = dom.createElement("owner_type")
            txt = dom.createTextNode(veh.owner_type)
            x.appendChild(txt)
            dom.childNodes[0].appendChild(x)
            
            x = dom.createElement("parking_id")
            txt = dom.createTextNode(veh.parking_id)
            x.appendChild(txt)
            dom.childNodes[0].appendChild(x)
            
            x = dom.createElement("parking_place")
            txt = dom.createTextNode(veh.parking_place)
            x.appendChild(txt)
            dom.childNodes[0].appendChild(x)
            
            x = dom.createElement("season_class")
            txt = dom.createTextNode(veh.session_class)
            x.appendChild(txt)
            dom.childNodes[0].appendChild(x)
            
            x = dom.createElement("season_time")
            txt = dom.createTextNode(veh.session_time)
            x.appendChild(txt)
            dom.childNodes[0].appendChild(x)
            
            x = dom.createElement("season_rate")
            txt = dom.createTextNode(veh.session_rate)
            x.appendChild(txt)
            dom.childNodes[0].appendChild(x)
            
            x = dom.createElement("commencement_date")
            txt = dom.createTextNode(veh.commencement_date)
            x.appendChild(txt)
            dom.childNodes[0].appendChild(x)
            
            x = dom.createElement("end_date")
            txt = dom.createTextNode(veh.end_date)
            x.appendChild(txt)
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
