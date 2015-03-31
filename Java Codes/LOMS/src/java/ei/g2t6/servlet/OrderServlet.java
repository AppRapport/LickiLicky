package ei.g2t6.servlet;

import ei.g2t6.dao.OrderDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author USER
 */
@WebServlet(name = "OrderServlet", urlPatterns = {"/OrderServlet"})
public class OrderServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {

            OrderDAO oDao = new OrderDAO();
            
            //PrintWriter writer = new PrintWriter("C:\\Users\\USER\\Desktop\\ordersheet.xml", "UTF-8");
            
            //assigning item id
            int cowId = 113;
            int birdId = 114;
            int mangoId = 115;
            
            //assigning item price
            double cowPrice = 40;
            double birdPrice = 50;
            double mangoPrice= 55;
            
            double totalPrice= 0;
            
            //assigning description
            String cowD = "Cow Tongue";
            String birdD = "Bird Bird (including java sparrow)";
            String mangoD = "Mango Ruby";

            /* Process order to update database */
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String email = request.getParameter("email");
            String mobile = request.getParameter("mobile");
            String company = request.getParameter("company");

            String street = request.getParameter("street");
            String city = request.getParameter("city");
            String state = request.getParameter("state");
            String postal = request.getParameter("postal");
            String[] flavoursList = request.getParameterValues("flavours");

            int cowQuantity = Integer.parseInt(request.getParameter("cow-quantity"));
            int birdQuantity = Integer.parseInt(request.getParameter("bird-quantity"));
            int mangoQuantity = Integer.parseInt(request.getParameter("mango-quantity"));
            
            int mobileNo = Integer.parseInt(mobile);

            //order status - N/C/D
            /*for (String f : flavoursList) {

                out.println(f);

            }*/

            int orderNo = oDao.createOrder(firstName, lastName, email, company, street, city, state, postal, flavoursList, cowQuantity, birdQuantity, mangoQuantity, mobileNo);
            
            String xmlMessage = "";
            
            //create XML message
            if (cowQuantity > 0 && birdQuantity == 0 && mangoQuantity == 0) { //cow only
                
                totalPrice = cowQuantity * cowPrice;
                
                xmlMessage = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                        + "<order xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"C:/Users/USER/Desktop/LickiLicky/XSDs/order_from_LOMS.xsd\">\n"
                        + "    <order_id>" + orderNo + "</order_id>\n"
                        + "    <customer_company>" + company + "</customer_company>\n"
                        + "    <first_name>" + firstName + "</first_name>\n"
                        + "    <last_name>" + lastName + "</last_name>\n"
                        + "    <customer_email>" + email + "</customer_email>\n"
                        + "    <mobile>" + mobile + "</mobile>\n"
                        + "    <ship_to_addr>\n"
                        + "        <ship_to_street>" + street + "</ship_to_street>\n"
                        + "        <ship_to_city>" + city + "</ship_to_city>\n"
                        + "        <ship_to_state>" + state + "</ship_to_state>\n"
                        + "        <ship_to_zip_code>" + postal + "</ship_to_zip_code>\n"
                        + "    </ship_to_addr>\n"
                        + "    <item id=\"" + cowId + "\" price=\"" + cowPrice + "\">\n"
                        + "        <description>" + cowD + "</description>\n"
                        + "        <order_quantity>" + cowQuantity + "</order_quantity>\n"
                        + "    </item>\n"
                        + "    <total_price>" + totalPrice + "</total_price>\n"
                        + "</order>";
                
                //writer.println(xmlMessage);
            
            } else if (cowQuantity == 0 && birdQuantity > 0 && mangoQuantity == 0) { //bird
            
                totalPrice = birdQuantity * birdPrice;
                
                xmlMessage = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                        + "<order xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"C:/Users/USER/Desktop/LickiLicky/XSDs/order_from_LOMS.xsd\">\n"
                        + "    <order_id>" + orderNo + "</order_id>\n"
                        + "    <customer_company>" + company + "</customer_company>\n"
                        + "    <first_name>" + firstName + "</first_name>\n"
                        + "    <last_name>" + lastName + "</last_name>\n"
                        + "    <ship_to_addr>\n"
                        + "        <ship_to_street>" + street + "</ship_to_street>\n"
                        + "        <ship_to_city>" + city + "</ship_to_city>\n"
                        + "        <ship_to_state>" + state + "</ship_to_state>\n"
                        + "        <ship_to_zip_code>" + postal + "</ship_to_zip_code>\n"
                        + "    <customer_email>" + email + "</customer_email>\n"
                        + "    <mobile>" + mobile + "</mobile>\n"
                        + "    </ship_to_addr>\n"
                        + "    <item id=\"" + birdId + "\" price=\"" + birdPrice + "\">\n"
                        + "        <description>" + birdD + "</description>\n"
                        + "        <order_quantity>" + birdQuantity + "</order_quantity>\n"
                        + "    </item>\n"
                        + "    <total_price>" + totalPrice + "</total_price>\n"
                        + "</order>";
                
//                writer.println(xmlMessage);
                
            } else if (cowQuantity == 0 && birdQuantity == 0 && mangoQuantity > 0) { //mango
                
                totalPrice = mangoQuantity * mangoPrice;
                
                xmlMessage = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                        + "<order xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"C:/Users/USER/Desktop/LickiLicky/XSDs/order_from_LOMS.xsd\">\n"
                        + "    <order_id>" + orderNo + "</order_id>\n"
                        + "    <customer_company>" + company + "</customer_company>\n"
                        + "    <first_name>" + firstName + "</first_name>\n"
                        + "    <last_name>" + lastName + "</last_name>\n"
                        + "    <ship_to_addr>\n"
                        + "        <ship_to_street>" + street + "</ship_to_street>\n"
                        + "        <ship_to_city>" + city + "</ship_to_city>\n"
                        + "        <ship_to_state>" + state + "</ship_to_state>\n"
                        + "        <ship_to_zip_code>" + postal + "</ship_to_zip_code>\n"
                        + "    <customer_email>" + email + "</customer_email>\n"
                        + "    <mobile>" + mobile + "</mobile>\n"
                        + "    </ship_to_addr>\n"
                        + "    <item id=\"" + mangoId + "\" price=\"" + mangoPrice + "\">\n"
                        + "        <description>" + mangoD + "</description>\n"
                        + "        <order_quantity>" + mangoQuantity + "</order_quantity>\n"
                        + "    </item>\n"
                        + "    <total_price>" + totalPrice + "</total_price>\n"
                        + "</order>";
                
               // writer.println(xmlMessage);
                
            } else if (cowQuantity > 0 && birdQuantity > 0 && mangoQuantity == 0) { //cow bird
                
                totalPrice = (cowQuantity * cowPrice) + (birdQuantity * birdPrice);
                
                xmlMessage = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                        + "<order xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"C:/Users/USER/Desktop/LickiLicky/XSDs/order_from_LOMS.xsd\">\n"
                        + "    <order_id>" + orderNo + "</order_id>\n"
                        + "    <customer_company>" + company + "</customer_company>\n"
                        + "    <first_name>" + firstName + "</first_name>\n"
                        + "    <last_name>" + lastName + "</last_name>\n"
                        + "    <customer_email>" + email + "</customer_email>\n"
                        + "    <mobile>" + mobile + "</mobile>\n"
                        + "    <ship_to_addr>\n"
                        + "        <ship_to_street>" + street + "</ship_to_street>\n"
                        + "        <ship_to_city>" + city + "</ship_to_city>\n"
                        + "        <ship_to_state>" + state + "</ship_to_state>\n"
                        + "        <ship_to_zip_code>" + postal + "</ship_to_zip_code>\n"
                        + "    </ship_to_addr>\n"
                        + "    <item id=\"" + cowId + "\" price=\"" + cowPrice + "\">\n"
                        + "        <description>" + cowD + "</description>\n"
                        + "        <order_quantity>" + cowQuantity + "</order_quantity>\n"
                        + "    </item>\n"
                        + "    <item id=\"" + birdId + "\" price=\"" + birdPrice + "\">\n"
                        + "        <description>" + birdD + "</description>\n"
                        + "        <order_quantity>" + birdQuantity + "</order_quantity>\n"
                        + "    </item>\n"
                        + "    <total_price>" + totalPrice + "</total_price>\n"
                        + "</order>";
                
//                writer.println(xmlMessage);
                
            } else if (cowQuantity > 0 && birdQuantity == 0 && mangoQuantity > 0) { //cow mango
                
                totalPrice = (cowQuantity * cowPrice) + (mangoQuantity * mangoPrice);
                
                xmlMessage = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                        + "<order xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"C:/Users/USER/Desktop/LickiLicky/XSDs/order_from_LOMS.xsd\">\n"
                        + "    <order_id>" + orderNo + "</order_id>\n"
                        + "    <customer_company>" + company + "</customer_company>\n"
                        + "    <first_name>" + firstName + "</first_name>\n"
                        + "    <last_name>" + lastName + "</last_name>\n"
                        + "    <customer_email>" + email + "</customer_email>\n"
                        + "    <mobile>" + mobile + "</mobile>\n"
                        + "    <ship_to_addr>\n"
                        + "        <ship_to_street>" + street + "</ship_to_street>\n"
                        + "        <ship_to_city>" + city + "</ship_to_city>\n"
                        + "        <ship_to_state>" + state + "</ship_to_state>\n"
                        + "        <ship_to_zip_code>" + postal + "</ship_to_zip_code>\n"
                        + "    </ship_to_addr>\n"
                        + "    <item id=\"" + cowId + "\" price=\"" + cowPrice + "\">\n"
                        + "        <description>" + cowD + "</description>\n"
                        + "        <order_quantity>" + cowQuantity + "</order_quantity>\n"
                        + "    </item>\n"
                        + "    <item id=\"" + mangoId + "\" price=\"" + mangoPrice + "\">\n"
                        + "        <description>" + mangoD + "</description>\n"
                        + "        <order_quantity>" + mangoQuantity + "</order_quantity>\n"
                        + "    </item>\n"
                        + "    <total_price>" + totalPrice + "</total_price>\n"
                        + "</order>";
                
//                writer.println(xmlMessage);
                
            } else if (cowQuantity == 0 && birdQuantity > 0 && mangoQuantity > 0) { //bird mango
                
                totalPrice = (mangoQuantity * mangoPrice) + (birdQuantity * birdPrice);
                
                xmlMessage = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                        + "<order xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"C:/Users/USER/Desktop/LickiLicky/XSDs/order_from_LOMS.xsd\">\n"
                        + "    <order_id>" + orderNo + "</order_id>\n"
                        + "    <customer_company>" + company + "</customer_company>\n"
                        + "    <first_name>" + firstName + "</first_name>\n"
                        + "    <last_name>" + lastName + "</last_name>\n"
                        + "    <customer_email>" + email + "</customer_email>\n"
                        + "    <mobile>" + mobile + "</mobile>\n"
                        + "    <ship_to_addr>\n"
                        + "        <ship_to_street>" + street + "</ship_to_street>\n"
                        + "        <ship_to_city>" + city + "</ship_to_city>\n"
                        + "        <ship_to_state>" + state + "</ship_to_state>\n"
                        + "        <ship_to_zip_code>" + postal + "</ship_to_zip_code>\n"
                        + "    </ship_to_addr>\n"
                        + "    <item id=\"" + birdId + "\" price=\"" + birdPrice + "\">\n"
                        + "        <description>" + birdD + "</description>\n"
                        + "        <order_quantity>" + birdQuantity + "</order_quantity>\n"
                        + "    </item>\n"
                        + "    <item id=\"" + mangoId + "\" price=\"" + mangoPrice + "\">\n"
                        + "        <description>" + mangoD + "</description>\n"
                        + "        <order_quantity>" + mangoQuantity + "</order_quantity>\n"
                        + "    </item>\n"
                        + "    <total_price>" + totalPrice + "</total_price>\n"
                        + "</order>";
                
//                writer.println(xmlMessage);
                
            } else if (cowQuantity > 0 && birdQuantity > 0 && mangoQuantity > 0) { //cow bird mango
                
                totalPrice = (mangoQuantity * mangoPrice) + (birdQuantity * birdPrice) + (cowQuantity * cowPrice);
                
                xmlMessage = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                        + "<order xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"C:/Users/USER/Desktop/LickiLicky/XSDs/order_from_LOMS.xsd\">\n"
                        + "    <order_id>" + orderNo + "</order_id>\n"
                        + "    <customer_company>" + company + "</customer_company>\n"
                        + "    <first_name>" + firstName + "</first_name>\n"
                        + "    <last_name>" + lastName + "</last_name>\n"
                        + "    <customer_email>" + email + "</customer_email>\n"
                        + "    <mobile>" + mobile + "</mobile>\n"
                        + "    <ship_to_addr>\n"
                        + "        <ship_to_street>" + street + "</ship_to_street>\n"
                        + "        <ship_to_city>" + city + "</ship_to_city>\n"
                        + "        <ship_to_state>" + state + "</ship_to_state>\n"
                        + "        <ship_to_zip_code>" + postal + "</ship_to_zip_code>\n"
                        + "    </ship_to_addr>\n"
                        + "    <item id=\"" + cowId + "\" price=\"" + cowPrice + "\">\n"
                        + "        <description>" + cowD + "</description>\n"
                        + "        <order_quantity>" + cowQuantity + "</order_quantity>\n"
                        + "    </item>\n"
                        + "    <item id=\"" + birdId + "\" price=\"" + birdPrice + "\">\n"
                        + "        <description>" + birdD + "</description>\n"
                        + "        <order_quantity>" + birdQuantity + "</order_quantity>\n"
                        + "    </item>\n"
                        + "    <item id=\"" + mangoId + "\" price=\"" + mangoPrice + "\">\n"
                        + "        <description>" + mangoD + "</description>\n"
                        + "        <order_quantity>" + mangoQuantity + "</order_quantity>\n"
                        + "    </item>\n"
                        + "    <total_price>" + totalPrice + "</total_price>\n"
                        + "</order>";
                
//                writer.println(xmlMessage);
                
            } 

            //reference: <?xml version="1.0" encoding="utf-8"?>
/*<!-- Created with Liquid XML 2014 Developer Bundle Edition (Education) 12.2.8.5459 (http://www.liquid-technologies.com) -->
             <order xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="C:\Users\USER\Desktop\LickiLicky\XSDs\order_from_LOMS.xsd">
             <first_name>string</first_name>
             <last_name>string</last_name>
             <ship_to_addr>
             <ship_to_city>string</ship_to_city>
             <ship_to_state>string</ship_to_state>
             <ship_to_zip_code>4487</ship_to_zip_code>
             </ship_to_addr>
             <item id="42" price="8871.9949058039">
             <order_quantity>4242534334</order_quantity>
             </item>
             <total_price>5103.8749058039</total_price>
             <order_id>3389218822</order_id>
             <customer_company>string</customer_company>
             <customer_email>string</customer_email>
             </order>*/
            //send msg to queue destination
            String args2[] = {"-server", "localhost", "-queue", "q.loms.neworder", xmlMessage};
            LOMSOrderMsgProducer producer = new LOMSOrderMsgProducer(args2);

            out.println("<!DOCTYPE html>\n" +
"<html>\n" +
"    <head>\n" +
"        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
"        <link href=\"css/bootstrap.min.css\" rel=\"stylesheet\">\n" +
"        <link href=\"css/style.css\" rel=\"stylesheet\">\n" +
"        <title>LickiLicky Ordering Management System</title>\n" +
"    </head>\n" +
"    <body><!--content goes here -->\n" +
"        \n" +
"        <!--Navigation bar-->\n" +
"        <div class = \"navbar navbar-inverse navbar-static-top\">\n" +
"            <div class=\"container\">\n" +
"                <a href = \"#\" class =\"navbar-brand\">\n" +
"                    <img src=\"images/lickilicky_logo.png\" width = \"200px\" height=\"65px\">\n" +
"                </a>\n" +
"\n" +
"                <div class=\"collapse navbar-collapse navHeaderCollapse\">\n" +
"                    <ul class=\"nav navbar-nav navbar-right navbar-text\">\n" +
"                        \n" +
"                        <li><a href=\"#\" style=\"color: #aaaaaa;\"><b>Order with us now!</b></a></li>\n" +
"                    </ul>\n" +
"                </div>\n" +
"\n" +
"            </div>\n" +
"\n" +
"        </div>\n" +
"        \n" +
"        <!--Content of body-->\n" +
"        <div class=\"container\">\n" +
"                <h2>Thank you for ordering with Lickilicky!</h2>\n" +
"            <br/><br/>\n" +
"                <img src=\"images/lickilicky_pic.png\" width=\"480\" height=\"529\" align=\"right\">\n" +
"        </div>\n" +
"        \n" +
"        <br /><br /><br /><br /><br />\n" +
"        <!-- Footer -->\n" +
"        <div class=\"navbar navbar-default navbar-fixed-bottom\">\n" +
"            <div class=\"container\">\n" +
"                <div class=\"text-center nav-centertext\">&copy 2014 LickiLicky Ice Cream. All rights reserved</div>\n" +
"            </div>\n" +
"        </div> \n" +
"        \n" +
"        <script src=\"js/jquery-1.11.1.min.js\"></script>\n" +
"        <script src=\"js/bootstrap.js\"></script>\n" +
"        \n" +
"    </body>\n" +
"</html>");
            
//            writer.close();

        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
