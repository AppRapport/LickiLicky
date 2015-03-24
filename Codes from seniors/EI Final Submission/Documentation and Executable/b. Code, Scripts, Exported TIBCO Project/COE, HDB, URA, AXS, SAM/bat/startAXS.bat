color 3F
TITLE AXS
CALL C:\tibco\ems\6.0\samples\java\setup.bat

javac AXSSubscriber.java

java AXSSubscriber -server 127.0.0.1 -topic t.payment -durable AXS
