package com.paypal;

/*==================================================================
 PayPal Express Checkout Call
 ===================================================================
 */

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ExpressCheckout extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Use "request" to read incoming HTTP headers (e.g. cookies)
		// and HTML form data (e.g. data the user entered and submitted)

		// Use "response" to specify the HTTP response line and headers
		// (e.g. specifying the content type, setting cookies).

		HttpSession session = request.getSession(true);

		/*
		 * The paymentAmount is the total value of ' the shopping cart, that was
		 * set ' earlier in a session variable ' by the shopping cart page
		 * '-------------------------------------------
		 */

		String paymentAmount = (String) session.getAttribute("Payment_Amount");

		/*
		 * The returnURL is the location where buyers return to when a ' payment
		 * has been succesfully authorized. ' ' This is set to the value entered
		 * on the Integration Assistant '------------------------------------
		 */

		String returnURL = request.getScheme() + "://"
				+ request.getServerName() + ":" + request.getServerPort()
				+ request.getContextPath() + "/Return";

		/*
		 * The cancelURL is the location buyers are sent to when they hit the '
		 * cancel button during authorization of payment during the PayPal flow
		 * ' ' This is set to the value entered on the Integration Assistant
		 * '------------------------------------
		 */
		String cancelURL = request.getScheme() + "://"
				+ request.getServerName() + ":" + request.getServerPort()
				+ request.getContextPath() + "/cancel.jsp";

		Properties prop = new Properties();
		InputStream input = null;

		try {
			
			//loading configuration setting from config file
			
			String filename = "config/config.properties";
			input = this.getClass().getClassLoader()
					.getResourceAsStream(filename);
			if (input == null) {
				System.out.println("Sorry, unable to find " + filename);
				return;
			}

			/*load a properties file from class path, inside static method */
			prop.load(input);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*Checking if sandbox mode is set or not .*/
		String strSandbox = "";
		if (prop.getProperty("SANDBOX_FLAG").equals("true")) {
			strSandbox = "_SANDBOX";
		}
		
		/*getting the sandbox or live checkout Url from config file .*/
		final String PP_CHECKOUT_URL = prop
				.getProperty("PP_CHECKOUT_URL_SANDBOX") + strSandbox;
		
		/*getting access token returned by Log In With PayPal and storing it in a variable.*/
		HashMap<String, String> user = (HashMap<String, String>) session
				.getAttribute("user");
		String identityAccessToken = "";
		if (user.get("access_token") != null)
			identityAccessToken  += user.get("access_token");
		
		/* Calling ShortcutExpressCheckout to get the TOKEN from PayPal */  
		PayPal ppf = new PayPal();
		HashMap nvp = ppf.callShortcutExpressCheckout(request, returnURL, cancelURL, identityAccessToken);
		
		/* Setting the value of Token returned in the session */
		String strAck = nvp.get("ACK").toString().toUpperCase();
		if (strAck != null
				&& (strAck.equals("SUCCESS") || strAck
						.equals("SUCCESSWITHWARNING"))) {
			session.setAttribute("token", nvp.get("TOKEN").toString());
			
			/*Redirecting to the Checkout Url appending the Token received */
			
			response.sendRedirect(response.encodeRedirectURL(PP_CHECKOUT_URL
					+ nvp.get("TOKEN").toString() + "&useraction=commit"));
		} else {
			/* Display a user friendly Error on the page using any of the
			   following error information returned by PayPal */

			String ErrorCode = nvp.get("L_ERRORCODE0").toString();
			String ErrorShortMsg = nvp.get("L_SHORTMESSAGE0").toString();
			String ErrorLongMsg = nvp.get("L_LONGMESSAGE0").toString();
			String ErrorSeverityCode = nvp.get("L_SEVERITYCODE0").toString();
			
			//Redirect to the Home Page incase of any errors.
			response.sendRedirect("final.jsp");
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}