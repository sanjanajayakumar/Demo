<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE HTML>
<%@ include file="header.jsp" %> 
                       
    <div class="span1">
   </div>
   <div class="span5">
                        <h3> DIGITAL SLR CAMERA </h3>
                        <table>
                        <!--Form containing item parameters and seller credentials needed for SetExpressCheckout Call-->
                        <form class="form" action="ExpressCheckout" method="POST">
                        <tr><img src="img/camera.jpg" width="160" height="100"/></tr>
                        <tr><p class="lead"> Item Specifications:</p></tr>
                        <tr><td>Item Name:</td><td><input type="text" name="L_PAYMENTREQUEST_0_NAME0" value="DSLR Camera"></input></td></tr>
                        <tr><td>Item ID: </td><td><input type="text" name="L_PAYMENTREQUEST_0_NUMBER0" value="A0123"></input></td></tr>
                        <tr><td>Description:</td><td><input type="text" name="L_PAYMENTREQUEST_0_DESC0" value="Autofocus Camera"></input></td></tr>
                        <tr><td>Quantity:</td><td><input type="text" name="L_PAYMENTREQUEST_0_QTY0" value="1" readonly></input></td></tr>
                        <tr><td>Price:</td><td><input type="text" name="PAYMENTREQUEST_0_ITEMAMT" value="10.00" readonly></input></td></tr>
                        <tr><td>Tax:</td><td><input type="text" name="PAYMENTREQUEST_0_TAXAMT" value="2" readonly></input></td></tr>
                        <tr><td>Shipping Amount:</td><td><input type="text" name="PAYMENTREQUEST_0_SHIPPINGAMT" value="5" readonly></input></td></tr>
                        <tr><td>Handling Amount:</td><td><input type="text" name="PAYMENTREQUEST_0_HANDLINGAMT" value="1" readonly></input></td></tr>
                        <tr><td>Shipping Discount:</td><td><input type="text" name="PAYMENTREQUEST_0_SHIPDISCAMT" value="-3" readonly></input></td></tr>
                        <tr><td>Insurance Amount:</td><td><input type="text" name="PAYMENTREQUEST_0_INSURANCEAMT" value="2" readonly></input></td></tr>
                        <tr><td>Total Amount:</td><td><input type="text" name="PAYMENTREQUEST_0_AMT" value="17" readonly></input></td></tr>
                        <tr><td><%
						String path = request.getContextPath();
						String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
						%>
                        <input type="hidden" name="LOGOIMG" value="<%=basePath %>img/logo.jpg"></input></td></tr>
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
                        </table>
   </div>
   <div class="span6">
      <h4>README</h4>
          <p>
                  1) Click on ‘PayPal Credit' button and see the experience.  
                  <br>
                  2) If you get any Firewall warning, add rule to the Firewall to allow incoming connections for your application.
                  <br>
                  3) Complete the order with PayPal using a buyer sandbox account provided on this page. And you're done!
                  <br>
                  4) The sample code uses default sandbox Seller credentials which are set in config.php. You can create your own credentials by creating PayPal Seller and Buyer accounts on Sandbox  <i><a href="https://developer.paypal.com/webapps/developer/applications/accounts/create" target="_blank">here</a></i>.
                  <br>
                  5) Make following changes in config.php:<br>
                  - If using your own Sandbox seller account, update PP_USER_SANDBOX, PP_PASSWORD_SANDBOX and PP_SIGNATURE_SANDBOX values with your sandbox credentials<br>
                  - SANDBOX_FLAG: Kept true for working with Sandbox, it will be false for live.<br> 
                  6) For trying out Mobile-EC, change the user-agent of your browser by installing a user agent switcher plugin and see how your website will appear on mobile devices.
               </p>
     
          <h4>Instructions to integrate on your website</h4>
        
         1) Copy the files and folders under 'PaypalCredit' package to the same location where you have your shopping cart page.
               <br>
               2) Copy the below  &lt;form&gt; .. &lt;/form&gt; to your shopping cart page. 
               <br><br>            
   <pre><code>&lt;form action="paypal_ec_redirect.php" method="POST"&gt;
      &lt;input type="hidden" name="PAYMENTREQUEST_0_AMT" value="10.00"&gt;&lt;/input&gt;
      &lt;input type="hidden" name="currencyCodeType" value="USD"&gt;&lt;/input&gt;
      &lt;input type="hidden" name="paymentType" value="Sale"&gt;&lt;/input&gt;
      <i>&lt;!--Pass additional input parameters based on your shopping cart. For complete list of all the parameters <a href="https://developer.paypal.com/webapps/developer/docs/classic/api/merchant/SetExpressCheckout_API_Operation_NVP/" target=_blank>click here</a></i> --&gt;
      &lt;input type="image" src="https://www.paypalobjects.com/webstatic/en_US/i/buttons/ppcredit-logo-large.png" alt="PayPal Credit"&gt;&lt;/input&gt;
&lt;/form&gt;</code></pre>
               3) Open your browser and navigate to your Shopping cart page. Click on 'PayPal Credit' button and complete the flow.<br>
               4) Read more details on Express Check out API <a href="https://developer.paypal.com/webapps/developer/docs/classic/products/#ec" target=_blank>here</a>                    


       <!--Demo Product details -->
                        <table>
                        <tr><td><h4> Buyer Credentials:</h4></td></tr>
                        <tr><td>Email-id:</td><td><input type="text" id="buyer_email" name="buyer_email" readonly></input> </td></tr>
                        <tr><td>Password:</td><td><input type="text" id="buyer_password" name="buyer_password" readonly></input></td></tr>
                        </table>
  
</div>

   <!--Script to dynamically choose a buyer account to render on index page-->
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
            
<%@ include file="footer.jsp" %> 