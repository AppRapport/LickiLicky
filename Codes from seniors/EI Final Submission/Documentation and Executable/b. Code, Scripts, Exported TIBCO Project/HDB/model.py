from google.appengine.ext import db

class hdb(db.Model) :
	carparkID = db.StringProperty()
	carplate = db.StringProperty()
	seasonExpiry = db.StringProperty()
	carparkID = db.StringProperty()
	carplate = db.StringProperty()
	carparkType = db.StringProperty()
	vehicle_type = db.StringProperty()
	owner_type = db.StringProperty()
	purchase_type = db.StringProperty()
	start_date = db.StringProperty()
	end_date = db.StringProperty()