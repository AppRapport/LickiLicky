CALL C:\tibco\ems\6.0\samples\java\setup.bat

javac SupplierConsumer.java

rem # You need to change the server name to machine A's hostname

java SupplierConsumer -server 127.0.0.1 -queue supplier.reorder