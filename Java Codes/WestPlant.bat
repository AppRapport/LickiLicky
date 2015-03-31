CALL C:\tibco\ems\6.0\samples\java\setup.bat

javac WestPlant.java

rem # You need to change the server name to machine A's hostname

java -cp %classpath%;c:\tibco\tpcl\5.8\jdbc\mysql-connector-java-5.1.34-bin.jar WestPlant -server 10.124.128.104 -queue q.West.plant
