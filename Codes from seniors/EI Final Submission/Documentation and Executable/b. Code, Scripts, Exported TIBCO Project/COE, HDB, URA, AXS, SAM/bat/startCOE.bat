color 0E
TITLE COE
CALL c:\tibco\ems\6.0\samples\java\setup.bat

javac COEAppRRConsumer.java

java -cp "%classpath%";c:\tibco\tpcl\5.7\jdbc\mysql-connector-java-5.1.16-bin.jar COEAppRRConsumer -server 127.0.0.1 -queue request.coeinfo