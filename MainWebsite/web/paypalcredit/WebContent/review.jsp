<%@page import="java.util.HashMap"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<%@ include file="header.jsp" %> 

<div class="span4">
	</div>
	<div class="span5">
		<table>
			<tbody>
				<tr><td><h4>Shipping Address</h5></td><td><h4>Billing Address</h4></td></tr>
				<% HashMap result = (HashMap) request.getAttribute("result");  %>
				<tr><td><%=result.get("PAYMENTREQUEST_0_SHIPTONAME")%></td><td><%=result.get("PAYMENTREQUEST_0_SHIPTONAME")%></td></tr>
				<tr><td><%=result.get("PAYMENTREQUEST_0_SHIPTOSTREET")%></td><td><%=result.get("PAYMENTREQUEST_0_SHIPTOSTREET")%></td></tr>
				<tr><td><%=result.get("PAYMENTREQUEST_0_SHIPTOCITY")%></td><td><%=result.get("PAYMENTREQUEST_0_SHIPTOCITY")%></td></tr>
				<tr><td><%=result.get("PAYMENTREQUEST_0_SHIPTOSTATE")%></td><td><%=result.get("PAYMENTREQUEST_0_SHIPTOSTATE")%></td></tr>
				<tr><td><%=result.get("PAYMENTREQUEST_0_SHIPTOCOUNTRYCODE")%></td><td><%=result.get("PAYMENTREQUEST_0_SHIPTOCOUNTRYCODE")%></td></tr>
				<tr><td><%=result.get("PAYMENTREQUEST_0_SHIPTOZIP")%></td><td><%=result.get("PAYMENTREQUEST_0_SHIPTOZIP")%></td></tr>

				<tr><td colspan="2">&nbsp;</td></tr>
				<tr><td colspan="2">&nbsp;</td></tr>
				<tr><td>Total Amount:</td><td id='amount'><%=result.get("PAYMENTREQUEST_0_AMT")%></td></tr>
				<tr><td>Currency Code:</td><td><%=result.get("CURRENCYCODE")%></td></tr>
				<tr><td>&nbsp;</td></tr>
				<script >
				var origAmt=<%=result.get("PAYMENTREQUEST_0_AMT")%>;
				var oldshipAmt=<%=session.getAttribute("PAYMENTREQUEST_0_SHIPPINGAMT")%>;
				function updateAmount(){
					var e = document.getElementById("shipping_method");
					var shipVal = e.options[e.selectedIndex].value;
				if(shipVal == 'ups_XPD')
						shipAmt=25.43;
					else if(shipVal == 'ups_WXS')
						shipAmt=15.67;
					else 
						shipAmt=5.00;
					
					var newAmt = shipAmt+origAmt-oldshipAmt;
					document.getElementById("amount").innerHTML=newAmt;
				}
				</script>
				<tr><td><h3>Shipping Method</h3></td></tr>
					<tr><td>
					<form action="Return?page=return" name="order_confirm" method="POST">
				Shipping methods: <select onchange="updateAmount();" name="shipping_method" id="shipping_method" style="width: 250px;" class="required-entry">
					<optgroup label="United Parcel Service" style="font-style:normal;">
					<option value="ups_XPD">
					Worldwide Expedited - 25.43</option>
					<option value="ups_WXS">
					Worldwide Express Saver - 15.67</option>
					</optgroup>
					<optgroup label="Flat Rate" style="font-style:normal;">
					<option selected value="flatrate_flatrate">
					Fixed - 5.00</option>
					</optgroup>
					</select><br>
				<input type="submit" class ="btn btn-primary btn-large" value="Place Order"></input>
			</form>
					
					</td></tr>
					<tr><td></td></tr>
				
			</tbody>
		</table>
	</div>
	<div class="span3">
	</div>

			
<%@ include file="footer.jsp" %> 