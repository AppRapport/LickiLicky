package ei.g2t6.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lickilicky
 */
public class OrderDAO {

    /*private void handleSQLException(SQLException e, String sql, String... parameters) {

     String msg = "Unable to access data. SQL : " + sql + "\n";

     for (String para : parameters) {

     msg += para + "\n";

     }

     Logger.getLogger(OrderDAO.class.getName()).log(Level.SEVERE, msg, e);
     throw new RuntimeException(msg, e);

     }*/
    public int createOrder(String firstName, String lastName, String email, String company,
            String street, String city, String state, String postal, String[] flavours,
            int cowQuantity, int birdQuantity, int mangoQuantity, int mobile) {

        String test = "";
        int orderNo = 0;

        Connection conn = null;
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;
        PreparedStatement stmt3 = null;
        ResultSet rs = null;
        String sql = "";

        try {

            //creates the connection to database
            conn = ConnectionManager.getConnection();

            sql = "insert into orderDB (first_name, last_name, company, email, ship_to_street, ship_to_city, ship_to_state, ship_to_postal,  order_status, mobile) values (?,?,?,?,?,?,?,?,?,?)";

            System.out.println(sql);

            int postalInt = Integer.parseInt(postal);
            String newO = "N";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setString(3, company);
            stmt.setString(4, email);
            stmt.setString(5, street);
            stmt.setString(6, city);
            stmt.setString(7, state);
            stmt.setInt(8, postalInt);
            stmt.setString(9, newO); //N to indicate NEW Order
            stmt.setInt(10, mobile);

            stmt.executeUpdate();

            //create new order item
            sql = "SELECT order_id FROM orderdb\n"
                    + "ORDER BY order_id DESC\n"
                    + "LIMIT 1";

            stmt2 = conn.prepareStatement(sql);

            rs = stmt2.executeQuery();

            orderNo = 0;

            while (rs.next()) {

                orderNo = rs.getInt("order_id");

            }
            
            System.out.println(orderNo);

            for (String flavour : flavours) {

                if (flavour.equals("cow")) {

                    double totalPrice = cowQuantity * 40;

                    sql = "insert into order_item values (?,?,?,?,?,?)";

                    stmt3 = conn.prepareStatement(sql);

                    stmt3.setInt(1, orderNo);
                    stmt3.setInt(2, 113);
                    stmt3.setDouble(3, totalPrice);
                    stmt3.setString(4, "Cow Tongue");
                    stmt3.setInt(5, cowQuantity);
                    stmt3.setString(6, null);

                    stmt3.executeUpdate();

                }

                if (flavour.equals("bird")) {

                    double totalPrice = birdQuantity * 50;

                    sql = "insert into order_item values (?,?,?,?,?,?)";

                    stmt3 = conn.prepareStatement(sql);

                    stmt3.setInt(1, orderNo);
                    stmt3.setInt(2, 114);
                    stmt3.setDouble(3, totalPrice);
                    stmt3.setString(4, "Bird (including java sparrow)");
                    stmt3.setInt(5, birdQuantity);
                    stmt3.setString(6, null);

                    stmt3.executeUpdate();
                }

                if (flavour.equals("mango")) {

                    double totalPrice = mangoQuantity * 55;

                    sql = "insert into order_item values (?,?,?,?,?,?)";

                    stmt3 = conn.prepareStatement(sql);

                    stmt3.setInt(1, orderNo);
                    stmt3.setInt(2, 115);
                    stmt3.setDouble(3, totalPrice);
                    stmt3.setString(4, "Mango Ruby Deluxe");
                    stmt3.setInt(5, mangoQuantity);
                    stmt3.setString(6, null);

                    stmt3.executeUpdate();
                }

            }

        } catch (SQLException se) {
            System.out.println(se);
        } catch (Exception e) {
            System.out.println(e);

        } finally {

            ConnectionManager.close(conn, stmt, rs);

        }

        return orderNo;
    }

}
