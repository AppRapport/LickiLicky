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
            double mangoPrice = 55;

            double totalPrice = 0;

            /* Process order to update database */
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String email = request.getParameter("email");
            String company = request.getParameter("company");

            String street = request.getParameter("street");
            String city = request.getParameter("city");
            String state = request.getParameter("state");
            String postal = request.getParameter("postal");
            String[] flavoursList = request.getParameterValues("flavours");

            int cowQuantity = Integer.parseInt(request.getParameter("cow-quantity"));
            int birdQuantity = Integer.parseInt(request.getParameter("bird-quantity"));
            int mangoQuantity = Integer.parseInt(request.getParameter("mango-quantity"));

            String cowD = "Cow Tongue flavour";
            String birdD = "Bird flavour";
            String mangoD = "Mango Ruby Deluxe";

            //order status - N/C/D
            /*for (String f : flavoursList) {

             out.println(f);

             }*/
            int orderNo = oDao.createOrder(firstName, lastName, email, company, street, city, state, postal, flavoursList, cowQuantity, birdQuantity, mangoQuantity);

            String xmlMessage = "";

            //create XML message
            if (cowQuantity > 0 && birdQuantity == 0 && mangoQuantity == 0) { //cow only

                totalPrice = cowQuantity * cowPrice;

                xmlMessage = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                        + "<order xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"C:/Users/USER/Desktop/LickiLicky/XSDs/order_from_LOMS.xsd\">\n"
                        + "    <first_name>" + firstName + "</first_name>\n"
                        + "    <last_name>" + lastName + "</last_name>\n"
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
                        + "    <order_id>" + orderNo + "</order_id>\n"
                        + "    <customer_company>" + company + "</customer_company>\n"
                        + "    <customer_email>" + email + "</customer_email>\n"
                        + "</order>";

            } else if (cowQuantity == 0 && birdQuantity > 0 && mangoQuantity == 0) { //bird

                totalPrice = birdQuantity * birdPrice;

                xmlMessage = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                        + "<order xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"C:/Users/USER/Desktop/LickiLicky/XSDs/order_from_LOMS.xsd\">\n"
                        + "    <first_name>" + firstName + "</first_name>\n"
                        + "    <last_name>" + lastName + "</last_name>\n"
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
                        + "    <total_price>" + totalPrice + "</total_price>\n"
                        + "    <order_id>" + orderNo + "</order_id>\n"
                        + "    <customer_company>" + company + "</customer_company>\n"
                        + "    <customer_email>" + email + "</customer_email>\n"
                        + "</order>";

            } else if (cowQuantity == 0 && birdQuantity == 0 && mangoQuantity > 0) { //mango

                totalPrice = mangoQuantity * mangoPrice;

                xmlMessage = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                        + "<order xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"C:/Users/USER/Desktop/LickiLicky/XSDs/order_from_LOMS.xsd\">\n"
                        + "    <first_name>" + firstName + "</first_name>\n"
                        + "    <last_name>" + lastName + "</last_name>\n"
                        + "    <ship_to_addr>\n"
                        + "        <ship_to_street>" + street + "</ship_to_street>\n"
                        + "        <ship_to_city>" + city + "</ship_to_city>\n"
                        + "        <ship_to_state>" + state + "</ship_to_state>\n"
                        + "        <ship_to_zip_code>" + postal + "</ship_to_zip_code>\n"
                        + "    </ship_to_addr>\n"
                        + "    <item id=\"" + mangoId + "\" price=\"" + mangoPrice + "\">\n"
                        + "        <description>" + mangoD + "</description>\n"
                        + "        <order_quantity>" + mangoQuantity + "</order_quantity>\n"
                        + "    </item>\n"
                        + "    <total_price>" + totalPrice + "</total_price>\n"
                        + "    <order_id>" + orderNo + "</order_id>\n"
                        + "    <customer_company>" + company + "</customer_company>\n"
                        + "    <customer_email>" + email + "</customer_email>\n"
                        + "</order>";

            } else if (cowQuantity > 0 && birdQuantity > 0 && mangoQuantity == 0) { //cow bird

                totalPrice = (cowQuantity * cowPrice) + (birdQuantity * birdPrice);

                xmlMessage = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                        + "<order xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"C:/Users/USER/Desktop/LickiLicky/XSDs/order_from_LOMS.xsd\">\n"
                        + "    <first_name>" + firstName + "</first_name>\n"
                        + "    <last_name>" + lastName + "</last_name>\n"
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
                        + "    <order_id>" + orderNo + "</order_id>\n"
                        + "    <customer_company>" + company + "</customer_company>\n"
                        + "    <customer_email>" + email + "</customer_email>\n"
                        + "</order>";

            } else if (cowQuantity > 0 && birdQuantity == 0 && mangoQuantity > 0) { //cow mango

                totalPrice = (cowQuantity * cowPrice) + (mangoQuantity * mangoPrice);

                xmlMessage = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                        + "<order xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"C:/Users/USER/Desktop/LickiLicky/XSDs/order_from_LOMS.xsd\">\n"
                        + "    <first_name>" + firstName + "</first_name>\n"
                        + "    <last_name>" + lastName + "</last_name>\n"
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
                        + "    <order_id>" + orderNo + "</order_id>\n"
                        + "    <customer_company>" + company + "</customer_company>\n"
                        + "    <customer_email>" + email + "</customer_email>\n"
                        + "</order>";

            } else if (cowQuantity == 0 && birdQuantity > 0 && mangoQuantity > 0) { //bird mango

                totalPrice = (mangoQuantity * mangoPrice) + (birdQuantity * birdPrice);

                xmlMessage = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                        + "<order xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"C:/Users/USER/Desktop/LickiLicky/XSDs/order_from_LOMS.xsd\">\n"
                        + "    <first_name>" + firstName + "</first_name>\n"
                        + "    <last_name>" + lastName + "</last_name>\n"
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
                        + "    <order_id>" + orderNo + "</order_id>\n"
                        + "    <customer_company>" + company + "</customer_company>\n"
                        + "    <customer_email>" + email + "</customer_email>\n"
                        + "</order>";

            } else if (cowQuantity > 0 && birdQuantity > 0 && mangoQuantity > 0) { //cow bird mango

                totalPrice = (mangoQuantity * mangoPrice) + (birdQuantity * birdPrice) + (cowQuantity * cowPrice);

                xmlMessage = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                        + "<order xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"C:/Users/USER/Desktop/LickiLicky/XSDs/order_from_LOMS.xsd\">\n"
                        + "    <first_name>" + firstName + "</first_name>\n"
                        + "    <last_name>" + lastName + "</last_name>\n"
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
                        + "    <order_id>" + orderNo + "</order_id>\n"
                        + "    <customer_company>" + company + "</customer_company>\n"
                        + "    <customer_email>" + email + "</customer_email>\n"
                        + "</order>";

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

            out.println("Thank you for ordering with Lickilicky!");

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
