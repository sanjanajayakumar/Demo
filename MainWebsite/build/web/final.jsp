<%-- 
    Document   : final
    Created on : Apr 15, 2015, 5:14:10 PM
    Author     : jayakumarramamurthy
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<title>Book Store Sample</title>
<meta name="keywords" content="Book Store Template, Free CSS Template, CSS Website Layout, CSS, HTML, TemplateMo.com" />
<meta name="description" content="Book Store Template, Free CSS Template, Download CSS Website from TemplateMo.com" />
<link href="templatemo_style.css" rel="stylesheet" type="text/css" />
</head>
<body>
<!--  Free CSS Templates from www.templatemo.com -->
<link href="templatemo_style.css" rel="stylesheet" type="text/css"/>
<div id="templatemo_container">
	<div id="templatemo_menu">
    	<ul>
            <li><a href="index.jsp">Home</a></li>
            <li><a href="#" class="current">Search</a></li>
            <li><a href="index.jsp">Books</a></li>            
            <li><a href="index.jsp">New Releases</a></li>  
            <li><a href="#">Company</a></li> 
            <li><a href="#">Contact</a></li>
    	</ul>
    </div> <!-- end of menu -->
    
    <div id="templatemo_header">
    	<div id="templatemo_special_offers">
        	<p>
                <span>25%</span> discounts for
        purchase over $ 40
        	</p>
			<a href="#" style="margin-left: 50px;">Read more...</a>
        </div>
        
        
        <div id="templatemo_new_books">
        	<ul>
                <li>Suspen disse</li>
                <li>Maece nas metus</li>
                <li>In sed risus ac feli</li>
            </ul>
            <a href="#" style="margin-left: 50px;">Read more...</a>
        </div>
    </div> <!-- end of header -->
    
    <div>
        
        <h3> XYZ Book </h3>
          
        <table> 
                         
            <form  action="ExpressCheckout" method="POST">
                        <tr><img src="images/templatemo_image_02.jpg" alt="CSS Template" width="100" height="150" /></tr>
                        <tr><p class="lead"> Item Specifications:</p></tr>
                        <tr><td>Item Name:</td><td><input type="text" name="L_PAYMENTREQUEST_0_NAME0" value="XYZ Book"></input></td></tr>
                        <tr><td>Item ID: </td><td><input type="text" name="L_PAYMENTREQUEST_0_NUMBER0" value="A0123"></input></td></tr>
                        <tr><td>Description:</td><td><input type="text" name="L_PAYMENTREQUEST_0_DESC0" value="XYZ Book"></input></td></tr>
                        <tr><td>Quantity:</td><td><input type="text" name="L_PAYMENTREQUEST_0_QTY0" value="1" readonly></input></td></tr>
                        <tr><td>Price:</td><td><input type="text" name="PAYMENTREQUEST_0_ITEMAMT" value="50.00" readonly></input></td></tr>
                        <tr><td>Tax:</td><td><input type="text" name="PAYMENTREQUEST_0_TAXAMT" value="2" readonly></input></td></tr>
                        <tr><td>Shipping Amount:</td><td><input type="text" name="PAYMENTREQUEST_0_SHIPPINGAMT" value="5" readonly></input></td></tr>
                        <tr><td>Handling Amount:</td><td><input type="text" name="PAYMENTREQUEST_0_HANDLINGAMT" value="1" readonly></input></td></tr>
                        <tr><td>Shipping Discount:</td><td><input type="text" name="PAYMENTREQUEST_0_SHIPDISCAMT" value="-3" readonly></input></td></tr>
                        <tr><td>Insurance Amount:</td><td><input type="text" name="PAYMENTREQUEST_0_INSURANCEAMT" value="2" readonly></input></td></tr>
                        <tr><td>Total Amount:</td><td><input type="text" name="PAYMENTREQUEST_0_AMT" value="50" readonly></input></td></tr>
                        <tr><td><%
						String path = request.getContextPath();
						String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
						%>
                        </td></tr>
						<tr><td>Currency Code:</td><td>						
						    <select id="currencyCodeType" name="currencyCodeType">
							<option selected value="USD">USD</option>
							<option value="BRL">BRL</option>
							<option value="CAD">CAD</option>
							<option value="CZK">CZK</option>
							<option value="DKK">DKK</option>
							<option value="EUR">EUR</option>
							<option value="HKD">HKD</option>
							<option value="HUF">HUF</option>
							<option value="ILS">ILS</option>
							<option value="JPY">JPY</option>
							<option value="NOK">NOK</option>
							<option value="MXN">MXN</option>
							<option value="NZD">NZD</option>
							<option value="PHP">PHP</option>
							<option value="PLN">PLN</option>
							<option value="GBP">GBP</option>
							<option value="SGD">SGD</option>
							<option value="SEK">SEK</option>
							<option value="CHF">CHF</option>
							<option value="TWD">TWD</option>
							<option value="THB">THB</option>
							<option value="TRY">TRY</option>
                        </select>					
						
						<br></td></tr>
                        <tr><td>Payment Type: </td><td><select name="paymentType">
                                  <option value="Sale">Sale</option>
                                  </select><br></td></tr>
                        <tr><td colspan='2' align=right><input type='image' src="https://www.paypalobjects.com/webstatic/en_US/i/buttons/ppcredit-logo-large.png" alt="PayPal Credit"/></td></tr>
                        </form>
            <!--Form containing item parameters and seller credentials needed for SetExpressCheckout Call-->
                       
                        </table>
    
                    <table>
                        <tr><td><h4> Buyer Credentials:</h4></td></tr>
                        <tr><td>Email-id:</td><td><input type="text" id="buyer_email" name="buyer_email" readonly></input> </td></tr>
                        <tr><td>Password:</td><td><input type="text" id="buyer_password" name="buyer_password" readonly></input></td></tr>
                        </table>


</div>
   <!--Script to dynamically choose a buyer account to render on index page-->
                                
    
    <div id="templatemo_footer">
	       <a href="index.jsp">Home</a> | <a href="index.jsp">Search</a> | <a href="index.jsp">Books</a> | <a href="#">New Releases</a> | <a href="#">FAQs</a> | <a href="#">Contact Us</a><br />
        Copyright © 2048 <a href="#"><strong>Your Company Name</strong></a> | <a href="http://www.iwebsitetemplate.com" target="_parent">Website Templates</a> by <a href="http://www.templatemo.com" target="_parent">Free CSS Templates</a>
	</div> <!-- end of footer -->
<!--  Free CSS Template www.templatemo.com -->
</div> <!-- end of container -->
<div align=center>This template  downloaded form <a href='http://all-free-download.com/free-website-templates/'>free website templates</a></div></body>
</html>

 <script type="text/javascript">
      function getRandomNumberInRange(min, max) {
          return Math.floor(Math.random() * (max - min) + min);
      }
     
      
      var buyerCredentials = [{"email":"ron@hogwarts.com", "password":"qwer1234"},
                        {"email":"sallyjones1234@gmail.com", "password":"p@ssword1234"},
                        {"email":"joe@boe.com", "password":"123456789"},
                        {"email":"hermione@hogwarts.com", "password":"123456789"},
                        {"email":"lunalovegood@hogwarts.com", "password":"123456789"},
                        {"email":"ginnyweasley@hogwarts.com", "password":"123456789"},
                        {"email":"bellaswan@awesome.com", "password":"qwer1234"},
                        {"email":"edwardcullen@gmail.com", "password":"qwer1234"}];
      var randomBuyer = getRandomNumberInRange(0,buyerCredentials.length);
      
      document.getElementById("buyer_email").value =buyerCredentials[randomBuyer].email;
      document.getElementById("buyer_password").value =buyerCredentials[randomBuyer].password;
   </script> 