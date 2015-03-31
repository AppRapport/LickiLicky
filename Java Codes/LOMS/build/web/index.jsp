<%-- 
    Document   : index
    Created on : Mar 19, 2015, 1:29:53 PM
    Author     : USER
--%>

<%@page import="ei.g2t6.servlet.LOMSOrderMsgConsumer"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="css/style.css" rel="stylesheet">
        <title>LickiLicky Ordering Management System</title>
    </head>
    <body>
        
        <!--content goes here -->
        
        <!--Navigation bar-->
        <div class = "navbar navbar-inverse navbar-static-top">
            <div class="container">
                <a href = "#" class ="navbar-brand">
                    <img src="images/lickilicky_logo.png" width = "200px" height="65px">
                </a>

                <div class="collapse navbar-collapse navHeaderCollapse">
                    <ul class="nav navbar-nav navbar-right navbar-text">
                        
                        <li><a href="#" style="color: #aaaaaa;"><b>Order with us now!</b></a></li>
                    </ul>
                </div>

            </div>

        </div>
        
        <!--Content of body-->
        <div class="container">
            <div class="row">
            <div class="col-md-6 grid-overlay">
                <form action="OrderServlet" method="POST">
                <h2>Place new order</h2>
                
                <div class="form-group">                       
                        <label>Email:</label>
                        <input type="email" class="form-control" name="email">               
                </div>
                
                <div class="form-group">                       
                        <label>Mobile(omit country code (+1)):</label>
                        <input type="text" class="form-control" name="mobile">               
                </div>
                
                <div class="form-group">                       
                        <label>First Name:</label>
                        <input type="text" class="form-control" name="firstName">               
                </div>
                
                <div class="form-group">                       
                        <label>Last Name:</label>
                        <input type="text" class="form-control" name="lastName">               
                </div>
                
                <label>Company:</label>
                <table>
                    <tr>
                        <td><input type="radio" name="company" value="walmart" class="radio-inline"><br></td>
                        <th>Wal-Mart Stores, Inc</th>
                        <td><input type="radio" name="company" value="target" class="radio-inline"><br></td>
                        <th>Target Corporation</th>
                    </tr>
                </table>
                
                <div class="form-group">                       
                        <label>Street Address:</label>
                        <input type="text" class="form-control" name="street">               
                </div>
                
                <div class="form-group">                       
                        <label>City:</label>
                        <input type="text" class="form-control" name="city">               
                </div>
                
                <div class="form-group">                       
                        <label>Postal Code:</label>
                        <input type="text" class="form-control" name="postal">               
                </div>
                
                <div class="form-group">                       
                        <label>State:</label>
                        <select name="state" class="form-control">
                            <option value="Alabama">Alabama</option>
                            <option value="Arizona">Arizona</option>
                            <option value="Arkansas">Arkansas</option>
                            <option value="California">California</option>
                            <option value="Colorado">Colorado</option>
                            <option value="Connecticut">Connecticut</option>
                            <option value="Delaware">Delaware</option>
                            <option value="Florida">Florida</option>
                            <option value="Georgia">Georgia</option>
                            <option value="Idaho">Idaho</option>
                            <option value="Illinois">Illinois</option>
                            <option value="Indiana">Indiana</option>
                            <option value="Iowa">Iowa</option>
                            <option value="Kansas">Kansas</option>
                            <option value="Kentucky">Kentucky</option>
                            <option value="Louisiana">Louisiana</option>
                            <option value="Maine">Maine</option>
                            <option value="Maryland">Maryland</option>
                            <option value="Massachusetts">Massachusetts</option>
                            <option value="Michigan">Michigan</option>
                            <option value="Minnesota">Minnesota</option>
                            <option value="Mississippi">Mississippi</option>
                            <option value="Missouri">Missouri</option>
                            <option value="Montana">Montana</option>
                            <option value="Nebraska">Nebraska</option>
                            <option value="Nevada">Nevada</option>
                            <option value="New Hampshire">New Hampshire</option>
                            <option value="New Jersey">New Jersey</option>
                            <option value="New Mexico">New Mexico</option>
                            <option value="New York">New York</option>
                            <option value="North Carolina">North Carolina</option>
                            <option value="North Dakota">North Dakota</option>
                            <option value="Ohio">Ohio</option>
                            <option value="Oklahoma">Oklahoma</option>
                            <option value="Oregon">Oregon</option>
                            <option value="Pennsylvania">Pennsylvania</option>
                            <option value="South Carolina">South Carolina</option>
                            <option value="South Dakota">South Dakota</option>
                            <option value="Tennessee">Tennessee</option>
                            <option value="Texas">Texas</option>
                            <option value="Utah">Utah</option>
                            <option value="Vermont">Vermont</option>
                            <option value="Virginia">Virginia</option>
                            <option value="Washington">Washington</option>
                            <option value="West Virginia">West Virginia</option>
                            <option value="Wisconsin">Wisconsin</option>
                            <option value="Wyoming">Wyoming</option>
                        </select>             
                </div>
                <div class="form-group">
                    <label>Flavours:</label><br/>
                <table>
                    <tr>
                        <td><input type="checkbox" name="flavours" value="cow" class="checkbox-inline"></td>
                        <td><b>Cow Tongue</b> - (US$40 per Carton)</td>
                        <td><select name="cow-quantity" class="form-control">
                                <option value="0"> -- Select --  </option>
                                <option value="50">50 Carton</option>
                                <option value="100">100 Carton</option>
                                <option value="150">150 Carton</option>
                                <option value="200">200 Carton</option>
                                <option value="250">250 Carton</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td><input type="checkbox" name="flavours" value="bird" class="checkbox-inline"></td>
                        <td><b>Bird Shit</b> - (US$50 per Carton)</td>
                        <td><select name="bird-quantity" class="form-control">
                                <option value="0"> -- Select -- </option>
                                <option value="50">50 Carton</option>
                                <option value="100">100 Carton</option>
                                <option value="150">150 Carton</option>
                                <option value="200">200 Carton</option>
                                <option value="250">250 Carton</option>
                            </select></td>
                    </tr>
                    <tr>
                        <td><input type="checkbox" name="flavours" value="mango" class="checkbox-inline"></td>
                        <td><b>Mango Ruby Deluxe</b> - (US$55 per Carton)</td>
                        <td><select name="mango-quantity" class="form-control">
                                <option value="0"> -- Select -- </option>
                                <option value="50">50 Carton</option>
                                <option value="100">100 Carton</option>
                                <option value="150">150 Carton</option>
                                <option value="200">200 Carton</option>
                                <option value="250">250 Carton</option>
                            </select></td>
                    </tr>           
                    
                </table>
                 </div>    
                
                <div align="right">
                    <input type="submit" class="btn btn-danger" value="Submit Order">
                    <input type="submit" class="btn btn-primary" value="Reset Order">
                </div>
           
        </form>
            </div>
            
            <div class="col-md-6 grid-overlay">
                <img src="images/lickilicky_pic.png" width="480" height="529" align="right">
            </div>    
            </div>
        </div>
        
        <br /><br /><br /><br /><br />
        <!-- Footer -->
        <div class="navbar navbar-default navbar-fixed-bottom">
            <div class="container">
                <div class="text-center nav-centertext">&copy 2014 LickiLicky Ice Cream. All rights reserved</div>
            </div>
        </div> 
        
        <script src="js/jquery-1.11.1.min.js"></script>
        <script src="js/bootstrap.js"></script>
        
    </body>
</html>
