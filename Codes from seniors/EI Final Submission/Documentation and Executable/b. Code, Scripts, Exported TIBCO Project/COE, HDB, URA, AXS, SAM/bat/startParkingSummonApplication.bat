color 3F
TITLE HDB
CALL C:\tibco\ems\6.0\samples\java\setup.bat

javac ParkingSummonApplication.java

java HDB -server 127.0.0.1 -queue q.hdb.summon
