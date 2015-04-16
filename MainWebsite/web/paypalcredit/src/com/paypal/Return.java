package com.paypal;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class Return extends HttpServlet {
	private static final long serialVersionUID = 1L;
	   
    
    public Return() {
        super();
    
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            		throws ServletException, IOException {
    	HttpSession session = request.getSession(true);
    	String finalPaymentAmount = (String)session.getAttribute("Payment_Amount");

    	if (isSet(request.getParameter("PayerID")))
    		session.setAttribute("payer_id", request.getParameter("PayerID"));
    	String token = "";
    	if (isSet(request.getParameter("token"))){
    		session.setAttribute("TOKEN", request.getParameter("token"));
    		token = request.getParameter("token");
    	}else{
    		token = (String) session.getAttribute("TOKEN");
    	}

    	// Check to see if the Request object contains a variable named 'token'	
    	PayPal pp = new PayPal();   	
    	Map<String, String> result = new HashMap<String, String>();
    	// If the Request object contains the variable 'token' then it means that the user is coming from PayPal site.	
    	if (isSet(token))
    	{
    		/*
    		* Calls the GetExpressCheckoutDetails API call
    		*/
    		Map<String, String> getec = pp.getShippingDetails(session, token );
    		String strAck = getec.get("ACK");	 
    		if(strAck !=null && (strAck.equalsIgnoreCase("SUCCESS") || strAck.equalsIgnoreCase("SUCCESSWITHWARNING") ))
    		{
    			result.putAll(getec);
    			/*
    			* The information that is returned by the GetExpressCheckoutDetails call should be integrated by the partner into his Order Review 
    			* page		
    			*/
    			String email 				= getec.get("EMAIL"); // ' Email address of payer.
    			String payerId 			= getec.get("PAYERID"); // ' Unique PayPal customer account identification number.
    			String payerStatus		= getec.get("PAYERSTATUS"); // ' Status of payer. Character length and limitations: 10 single-byte alphabetic characters.
    			String firstName			= getec.get("FIRSTNAME"); // ' Payer's first name.
    			String lastName			= getec.get("LASTNAME"); // ' Payer's last name.
    			String cntryCode			= getec.get("COUNTRYCODE"); // ' Payer's country of residence in the form of ISO standard 3166 two-character country codes.
    			String shipToName			= getec.get("PAYMENTREQUEST_0_SHIPTONAME"); // ' Person's name associated with this address.
    			String shipToStreet		= getec.get("PAYMENTREQUEST_0_SHIPTOSTREET"); // ' First street address.
    			String shipToCity			= getec.get("PAYMENTREQUEST_0_SHIPTOCITY"); // ' Name of city.
    			String shipToState		= getec.get("PAYMENTREQUEST_0_SHIPTOSTATE"); // ' State or province
    			String shipToCntryCode	= getec.get("PAYMENTREQUEST_0_SHIPTOCOUNTRYCODE"); // ' Country code. 
    			String shipToZip			= getec.get("PAYMENTREQUEST_0_SHIPTOZIP"); // ' U.S. Zip code or other country-specific postal code.
    			String addressStatus 		= getec.get("ADDRESSSTATUS"); // ' Status of street address on file with PayPal 
    			String totalAmt   		= getec.get("PAYMENTREQUEST_0_AMT"); // ' Total Amount to be paid by buyer
    			String currencyCode       = getec.get("CURRENCYCODE"); // 'Currency being used 
    			
    		} 
    		else  
    		{
    			//Display a user friendly Error on the page using any of the following error information returned by PayPal
                String ErrorCode = getec.get("L_ERRORCODE0").toString();
                String ErrorShortMsg = getec.get("L_SHORTMESSAGE0").toString();
                String ErrorLongMsg = getec.get("L_LONGMESSAGE0").toString();
                String ErrorSeverityCode = getec.get("L_SEVERITYCODE0").toString();
                
                String errorString = "SetExpressCheckout API call failed. "+

               		"<br>Detailed Error Message: " + ErrorLongMsg +
    		        "<br>Short Error Message: " + ErrorShortMsg +
    		        "<br>Error Code: " + ErrorCode +
    		        "<br>Error Severity Code: " + ErrorSeverityCode;
                request.setAttribute("error", errorString);
            	RequestDispatcher dispatcher = request.getRequestDispatcher("error.jsp");
            	if (dispatcher != null){
            		dispatcher.forward(request, response);
            	}
            }
    	}   
    	
    	
		if(isSet(request.getParameter("shipping_method"))){
			String shipMethod = request.getParameter("shipping_method");
			BigDecimal new_shipping =new BigDecimal("0.00");
			if(shipMethod.equals("ups_XPD"))
				new_shipping = new BigDecimal("25.43");
			else if(shipMethod.equals("ups_WXS"))
				new_shipping = new BigDecimal("15.67");
			else if(shipMethod.equals("flatrate_flatrate"))
				new_shipping = new BigDecimal("5.00");
    		BigDecimal shippingamt = new BigDecimal(session.getAttribute("PAYMENTREQUEST_0_SHIPPINGAMT").toString());
    		BigDecimal paymentAmount = new BigDecimal((String)session.getAttribute("PAYMENTREQUEST_0_AMT"));
    		if(shippingamt.compareTo(new BigDecimal(0)) > 0){
    			finalPaymentAmount = paymentAmount.add(new_shipping).subtract(shippingamt).toString().replace(".00", "") ;
    			session.setAttribute("shippingAmt",new_shipping.toString());
    		}else{
    			finalPaymentAmount = paymentAmount.add(new_shipping).toString().replace(".00", "") ;
    		}
		}
    	
    	/*
    	* Calls the DoExpressCheckoutPayment API call
    	*/
       	String page="return.jsp";
    	if (isSet(request.getParameter("page")) && request.getParameter("page").equals("return")){  
	    	Map doEC = pp.confirmPayment (session,finalPaymentAmount,request.getServerName() );
	    	String strAck = doEC.get("ACK").toString().toUpperCase();
	    	if(strAck !=null && (strAck.equalsIgnoreCase("Success") || strAck.equalsIgnoreCase("SuccessWithWarning"))){
		    	result.putAll(doEC);
		    	request.setAttribute("ack", strAck);
		    	session.invalidate();
	    	}else{
		    	//Display a user friendly Error on the page using any of the following error information returned by PayPal
	            String ErrorCode = doEC.get("L_ERRORCODE0").toString();
	            String ErrorShortMsg = doEC.get("L_SHORTMESSAGE0").toString();
	            String ErrorLongMsg = doEC.get("L_LONGMESSAGE0").toString();
	            String ErrorSeverityCode = doEC.get("L_SEVERITYCODE0").toString();	            
	            String errorString = "SetExpressCheckout API call failed. "+	
	           		"<br>Detailed Error Message: " + ErrorLongMsg +
			        "<br>Short Error Message: " + ErrorShortMsg +
			        "<br>Error Code: " + ErrorCode +
			        "<br>Error Severity Code: " + ErrorSeverityCode;
	            request.setAttribute("error", errorString);
	        	RequestDispatcher dispatcher = request.getRequestDispatcher("error.jsp");
	        	if (dispatcher != null){
	        		dispatcher.forward(request, response);
	        	}
	        	return;
	    	}
	        page="return.jsp";
    	}else{
    		page="review.jsp";
    	}
    	request.setAttribute("result", result);

    	RequestDispatcher dispatcher = request.getRequestDispatcher(page);
    	if (dispatcher != null){
    		dispatcher.forward(request, response);
    	}
    
    }

	public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		doGet(request, response);
	}
	
	private boolean isSet(Object value){
		return (value !=null && value.toString().length()!=0);
	}
}
