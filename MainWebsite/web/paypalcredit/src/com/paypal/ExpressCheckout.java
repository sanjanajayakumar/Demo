package com.paypal;


/*==================================================================
 PayPal Express Checkout Call
 ===================================================================
*/


import java.io.IOException;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ExpressCheckout  extends HttpServlet {
   
	private static final long serialVersionUID = -2722761580200224133L;
	
    
	public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
        throws ServletException, IOException {

		HttpSession session = request.getSession();
        PayPal pp = new PayPal();
        /*
        '------------------------------------
        ' The returnURL is the location where buyers return to when a
        ' payment has been successfully authorized.
        '
        ' This is set to the value entered on the Integration Assistant
        '------------------------------------
        */
        
        String returnURL = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/Return?page=review";
        
        /*
        '------------------------------------
        ' The cancelURL is the location buyers are sent to when they hit the
        ' cancel button during authorization of payment during the PayPal flow
        '
        ' This is set to the value entered on the Integration Assistant
        '------------------------------------
        */
        String cancelURL = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/cancel.jsp";
      

        for (String key : request.getParameterMap().keySet()) {
			   session.setAttribute(key, request.getParameterMap().get(key)[0]);
			}

        	Map<String, String> nvp;
    		nvp = pp.callShortcutExpressCheckout (request, returnURL, cancelURL);
	        
			String strAck = nvp.get("ACK").toString().toUpperCase();
	        if(strAck !=null && (strAck.equals("SUCCESS") || strAck.equals("SUCCESSWITHWARNING") ))
	        {
	            session.setAttribute("token", nvp.get("TOKEN").toString());
	            // Redirect to paypal.com
	            pp.redirectURL(response, nvp.get("TOKEN").toString());
	        }
	        else
	        {
	            // Display a user friendly Error on the page using any of the following error information returned by PayPal
	            String ErrorCode = nvp.get("L_ERRORCODE0").toString();
	            String ErrorShortMsg = nvp.get("L_SHORTMESSAGE0").toString();
	            String ErrorLongMsg = nvp.get("L_LONGMESSAGE0").toString();
	            String ErrorSeverityCode = nvp.get("L_SEVERITYCODE0").toString();
	            
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

public void doPost(HttpServletRequest request,
                   HttpServletResponse response)
    throws ServletException, IOException {
  doGet(request, response);
}

}