color 3F
TITLE URA
CALL C:\tibco\ems\6.0\samples\java\setup.bat

javac CarparkSummonApplication.java

java URA -server 127.0.0.1 -queue q.ura.summon
