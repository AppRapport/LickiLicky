color 3F
TITLE SAM
CALL C:\tibco\ems\6.0\samples\java\setup.bat

javac SAMSubscriber.java

java SAMSubscriber -server 127.0.0.1 -topic t.payment -durable SAM
