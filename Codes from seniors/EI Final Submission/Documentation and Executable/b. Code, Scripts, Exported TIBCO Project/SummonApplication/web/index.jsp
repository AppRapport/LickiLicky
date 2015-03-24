
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <title>:: LTA Summon Application Home Page ::</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta name="description" content="">
        <meta name="author" content="">

        <script type="text/javascript" src="./assets/js/jquery.js"></script>
        <script type="text/javascript">
            $(document).ready(function(){
                $("div#slide").click(function(){
                    $("div#info").slideDown(1000);
                    $("div#close").show();
                });
        
                $("div#close").click(function(){
                    $("div#info").slideUp(1000);
                    $("div#close").hide();
                });
            });

            
            function onChange(v){
                if(v == "mercedes"){
                    alert(v);
                    $("tr#couponID").show();
                    
                }else{
                    $("tr#couponID").hide();
                    document.getElementById("txtcoupon").value = "";
                }
             
                
                
            };
        </script>


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
                <h1>Summon Form</h1>
                <p>Enter Car park ID and Car Plate number:</p>
                <br />
                <table border ="0">
                    <tr>
                        <td width="300" valign="top">
                            <form method ="POST" action="SummonServlet" >

                                <table border="0">
                                    <!--                                    <tr>
                                                                            <td >Season Parking</td>
                                                                            <td>
                                                                                <input type="radio" name="sParking" value="Y" />Yes
                                                                                <input type="radio" name="sParking" value="N" />No
                                                                            </td>
                                                                        </tr>-->
                                    <tr>
                                        <td><b>Vehicle Licence No.: </td>
                                        <td><input type="text" name="carplate" /></td>
                                    </tr>

                                    <tr>
                                        <td><b>Car Park ID: </td>
                                        <td><select name="cpid">
                                                <option value="BIBE36">BIBE36</option>
                                                <option value="BIBE37">BIBE37</option>
                                                <option value="BIBE38">BIBE38</option>
                                                <option value="U001">U001</option>
                                                <option value="U002">U002</option>
                                                <option value="U003">U003</option>
                                            </select>
                                        </td>
                                    </tr>






                                    <tr>
                                        <td><b>Offence: </td>
                                        <td><select name="lstOffence" onchange="onChange(this.options[this.selectedIndex].value);">
                                                <option value="3">3</option>
                                                <option value="13">13</option>
                                                <option value="18(1)">18(1)</option>
                                                <option value="18(2)">18(2)</option>
                                                <option value="18(3)">18(3)</option>
                                                <option value="12">12</option>


                                            </select>
                                        </td>
                                    </tr>

                                    <tr id="couponID" style="display:none;">
                                        <td><b>Coupon ID: </td>
                                        <td><input type="text" id="txtcoupon" name="coupID" /></td>
                                    </tr>

                                    <tr>
                                        <td colspan="2" align="right">
                                            <input class="btn btn-primary btn-large" type="submit" value="submit" name="Submit" />
                                        </td>
                                    </tr>
                                </table>


                            </form>
                        </td>

                        <td width="300" valign="top" cellpadding="20">

                            <div id="slide">
                                <font style="color:blue"><u>Information on offence:</u></font>
                                <div id="info" style="display:none;">
                                    <table border="1">
                                        <th>Code</th>
                                        <th>Offence</th>
                                        <tr>
                                            <td>3</td>
                                            <td>Parking other than in a parking lot</td>
                                        </tr>
                                        <tr>
                                            <td>13</td>	
                                            <td>Parking beyond the boundaries of parking lot thereby causing obstruction. </td>
                                        </tr>
                                        <tr>
                                            <td>18(1)</td>	
                                            <td>Parking in a season parking area without possession of a valid season parking ticket.</td>
                                        </tr>
                                        <tr>
                                            <td>18(2)</td>	
                                            <td>Parking in season parking place without displaying the season parking ticket. </td>
                                        </tr>
                                        <tr>
                                            <td>18(3)</td>	
                                            <td>Displaying a season parking ticket that has alteration, erasure or other irregularity therein indicating that it has been tampered with.</td>
                                        </tr>

                                        <tr>
                                            <td>12</td>	
                                            <td>Displaying coupon that has alteration, erasure or other irregularity therein indicating that it has been tampered with.</td>
                                        </tr>
                                    </table>

                                </div>

                            </div>
                            <div id="close" style="display:none;"><font style="color:red"><u>Close</u></font></div>
                        </td>
                    </tr>
                </table>

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
