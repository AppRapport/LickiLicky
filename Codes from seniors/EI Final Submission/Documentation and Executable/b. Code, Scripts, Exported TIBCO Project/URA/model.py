from google.appengine.ext import db

class ura(db.Model) :
	vehicle_no = db.StringProperty()
	relationship_owner = db.StringProperty()
	owner_type = db.StringProperty()
	parking_id = db.StringProperty()
	parking_place = db.StringProperty()
	session_class = db.StringProperty()
	session_time = db.StringProperty()
	session_rate = db.StringProperty()
	commencement_date = db.StringProperty()
	end_date = db.StringProperty()