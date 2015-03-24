
<%@page import="java.sql.*"%>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <title>:: LTA Summon Application Result Page ::</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta name="description" content="">
        <meta name="author" content="">

        <script type="text/javascript" src="./assets/js/jquery.js"></script>



        <link href="./assets/css/bootstrap.css" rel="stylesheet">
        <style>
            body {
                padding-top: 60px; /* 60px to make the container go all the way to the bottom of the topbar */
            }
        </style>
        <link href="./assets/css/bootstrap-responsive.css" rel="stylesheet">

        <!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
        <!--[if lt IE 9]>
          <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
        <![endif]-->

        <!-- Le fav and touch icons -->
        <link rel="shortcut icon" href="../assets/ico/favicon.ico">
        <link rel="apple-touch-icon-precomposed" sizes="114x114" href="../assets/ico/apple-touch-icon-114-precomposed.png">
        <link rel="apple-touch-icon-precomposed" sizes="72x72" href="../assets/ico/apple-touch-icon-72-precomposed.png">
        <link rel="apple-touch-icon-precomposed" href="../assets/ico/apple-touch-icon-57-precomposed.png">
    </head>

    <body>

        <div class="navbar navbar-fixed-top">
            <div class="navbar-inner">
                <div class="container">
                    <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </a>



                    <a class="brand" href="#">LTA Summon Application</a>
                    <div class="nav-collapse">
                        <ul class="nav">
                            <li class="active"><a href="index.jsp">Summon</a></li>

                        </ul>
                    </div><!--/.nav-collapse -->
                </div>
            </div>
        </div>

        <div class="container">


            <div class="hero-unit">
                <h1>Summon Summary</h1>

                <%


                    String id = request.getParameter("id");

                    try {
                        // Setting up database related variables
                        String dbURL = "jdbc:mysql://localhost:3306/summondb";
                        String userName = "root";
                        String password = "";
                        java.sql.Connection dbConn = null;
                        Statement statement = null;


                        String selectSQL = "SELECT * FROM summon_records WHERE id= '" + id + "'";

                        Class.forName("com.mysql.jdbc.Driver").newInstance();
                        dbConn = DriverManager.getConnection(dbURL, userName, password);

                        statement = dbConn.createStatement();
                        ResultSet result = statement.executeQuery(selectSQL);

                        String sid = "";
                        String cPlate = "";
                        String cID = "";
                        String time = "";
                        String fineApplicable = "";
                        String fine = "";

                        out.println("<table border=1 class='table table-striped'>");
                        out.println("<th>Summon ID</th>");
                        out.println("<th>Vehicle Licence No.</th>");
                        out.println("<th>Car Park ID</th>");
                        out.println("<th>Time</th>");
                        out.println("<th>Fine Applicable?</th>");
                        out.println("<th>Season Parking?</th>");
                        out.println("<th>Offence</th>");
                        out.println("<th>Fine Amount</th>");

                        while (result.next()) { // process results one row at a time
                            out.println("<tr>");
                            out.println("<td>" + result.getString(1) + "</td>");
                            out.println("<td>" + result.getString(2) + "</td>");
                            out.println("<td>" + result.getString(3) + "</td>");
                            out.println("<td>" + result.getString(4) + "</td>");
                            //out.println("<td>" + result.getString(5) + "</td>");
                            if(result.getString(5).equals("0")) {
                               out.println("<td>No</td>");
                            } else {
                                out.println("<td>Yes</td>");
                            }
                            if(result.getString(6).equals("false")) {
                               out.println("<td>No</td>");
                            } else if(result.getString(6).equals("true")) {
                               out.println("<td>Yes</td>");
                            } else {
                                out.println("<td>" + result.getString(6) + "</td>");
                            }
                            //out.println("<td>" + result.getString(6) + "</td>");
 
                            out.println("<td>" + result.getString(7) + "</td>");
                            out.println("<td>$" + result.getString(8) + "</td>");
                            out.println("</tr>");


                        }
                        out.println("</table>");


                        statement.close(); // close the Statement
                        dbConn.close(); // close the Connection
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                %>

            </div>


        </div> <!-- /container -->

        <!-- Le javascript
        ================================================== -->
        <!-- Placed at the end of the document so the pages load faster -->
        <script src="./assets/js/jquery.js"></script>
        <script src="./assets/js/bootstrap-transition.js"></script>
        <script src="./assets/js/bootstrap-alert.js"></script>
        <script src="./assets/js/bootstrap-modal.js"></script>
        <script src="./assets/js/bootstrap-dropdown.js"></script>
        <script src="./assets/js/bootstrap-scrollspy.js"></script>
        <script src="./assets/js/bootstrap-tab.js"></script>
        <script src="./assets/js/bootstrap-tooltip.js"></script>
        <script src="./assets/js/bootstrap-popover.js"></script>
        <script src="./assets/js/bootstrap-button.js"></script>
        <script src="./assets/js/bootstrap-collapse.js"></script>
        <script src="./assets/js/bootstrap-carousel.js"></script>
        <script src="./assets/js/bootstrap-typeahead.js"></script>

    </body>
</html>
