package com.paypal;

import java.io.IOException;
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
    
    	session.setAttribute("payer_id", request.getParameter("PayerID"));

    	// Check to see if the Request object contains a variable named 'token'	
    	String token = "";
    	PayPal pp = new PayPal();   	
    	Map<String, String> result = new HashMap<String, String>();
    	// If the Request object contains the variable 'token' then it means that the user is coming from PayPal site.	
    	if (isSet(request.getParameter("token")))
    	{
    		token = request.getParameter("token");
    		/*
    		* Calls the GetExpressCheckoutDetails API call
    		*/
    		HashMap<String, String> getec = pp.getShippingDetails(session, token );
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
    			response.getWriter().println("GetExpressCheckoutDetails API call failed. ");
    			response.getWriter().println("Detailed Error Message: " +ErrorLongMsg);
    			response.getWriter().println("Short Error Message: " + ErrorShortMsg);
    			response.getWriter().println("Error Code: " + ErrorCode);
    			response.getWriter().println("Error Severity Code: " + ErrorSeverityCode);
    		}
    	}   
    	

    	/*
    	* Calls the DoExpressCheckoutPayment API call
    	*/
    	HashMap doEC = pp.confirmPayment (session,finalPaymentAmount,request.getServerName() );
    	String ackDoExpressCheckout = doEC.get("ACK").toString().toUpperCase();
    	result.putAll(doEC);
    	request.setAttribute("result", result);
    	request.setAttribute("ack", ackDoExpressCheckout);
    	session.invalidate();
    	
    	RequestDispatcher dispatcher = request.getRequestDispatcher("return.jsp");
    	if (dispatcher != null){
    		dispatcher.forward(request, response);
    	}
    	//response.sendRedirect("return.jsp");
    }

	public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		doGet(request, response);
	}
	
	private Boolean isSet(Object value){
		return (value !=null && value.toString().length()!=0);
	}
}
