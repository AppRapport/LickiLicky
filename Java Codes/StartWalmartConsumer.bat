CALL C:\tibco\ems\6.0\samples\java\setup.bat

javac WalmartConsumer.java

rem # You need to change the server name to machine A's hostname

java WalmartConsumer -server 10.124.128.104 -queue walmart.invoice