<%-- 
    Document   : listen
    Created on : Mar 27, 2015, 3:18:18 PM
    Author     : USER
--%>

<%@page import="ei.g2t6.servlet.LOMSOrderMsgConsumer"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Receiving Order...</title>
    </head>
    <body>

        <!-- Listener starts here -->
        <%

            String args1[] = {"-server", "localhost", "-queue", "q.loms.receiveOrder"};
            LOMSOrderMsgConsumer consumer = new LOMSOrderMsgConsumer(args1);

        %>

    </body>
</html>
