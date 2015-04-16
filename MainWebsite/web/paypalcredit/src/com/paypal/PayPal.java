package com.paypal;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class PayPal  {
    private String gvAPIUserName;
    private String gvAPIPassword;
    private String gvAPISignature;

    private String gvAPIEndpoint;
    private String  gvBNCode;

    private String  gvVersion;
    private String paypalUrl;
    public PayPal() {
	final Properties prop = new Properties();
	InputStream input = null;
	try {
	final String filename = "config/config.properties";
	
	input = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename); 
	//input = this.getClass().getClassLoader().getResourceAsStream(filename);  
	if(input==null){
            System.out.println("Sorry, unable to find " + filename);
	    return;
	}

	//load a properties file from class path, inside static method
		prop.load(input);
		//get the property value from config.properties file
		
        String strSandbox = "";
        if(prop.getProperty("SANDBOX_FLAG").equals("true")){
        	strSandbox="_SANDBOX";
        }
      //ButtonSource Tracker Code
        gvBNCode	= prop.getProperty("SBN_CODE");	
    	
        gvAPIUserName	= prop.getProperty("PP_USER"+strSandbox);
        gvAPIPassword	= prop.getProperty("PP_PASSWORD"+strSandbox);
        gvAPISignature = prop.getProperty("PP_SIGNATURE"+strSandbox);

        gvAPIEndpoint = prop.getProperty("PP_NVP_ENDPOINT"+strSandbox);
        paypalUrl = prop.getProperty("PP_CHECKOUT_URL"+strSandbox);

        gvVersion	= prop.getProperty("API_VERSION");
        java.lang.System.setProperty("https.protocols", prop.getProperty("SSL_VERSION_TO_USE"));
        
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}finally{
    	if(input!=null){
    		try {
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	}
    }

}



/*********************************************************************************
  * CallShortcutExpressCheckout: Function to perform the SetExpressCheckout API call
  *
  * Inputs:
  *		request:     the item details, prices and taxes
  *		returnURL:			the page where buyers return to after they are done with the payment review on PayPal
  *		cancelURL:			the page where buyers return to when they cancel the payment review on PayPal
  *
  * Output: Returns a HashMap object containing the response from the server.
*********************************************************************************/
public Map<String, String> callShortcutExpressCheckout( HttpServletRequest request, final String returnURL, final String cancelURL)
{

	// Construct the parameter string that describes the SetExpressCheckout API call in the shortcut implementation
	// For more information on the customizing the parameters passed refer: https://developer.paypal.com/docs/classic/express-checkout/integration-guide/ECCustomizing/
			
	//Mandatory parameters for SetExpressCheckout API call
	
	String nvpstr="";
	HttpSession session = request.getSession();
	if(isSet(request.getParameter("PAYMENTREQUEST_0_AMT") )){
		nvpstr += "&PAYMENTREQUEST_0_AMT=" + request.getParameter("PAYMENTREQUEST_0_AMT");
		session.setAttribute("Payment_Amount", request.getParameter("PAYMENTREQUEST_0_AMT"));
	}
	if(isSet(request.getParameter("paymentType"))){
		nvpstr += "&PAYMENTREQUEST_0_PAYMENTACTION=" + request.getParameter("paymentType");
		session.setAttribute("PaymentType", request.getParameter("paymentType"));
	}
	
	if(isSet(returnURL))
		nvpstr +=  "&RETURNURL=" + returnURL;

	if(isSet(cancelURL))
		nvpstr += "&CANCELURL=" + cancelURL;

	
	//Optional parameters for SetExpressCheckout API call
	if(isSet(request.getParameter("currencyCodeType") )){
		nvpstr += "&PAYMENTREQUEST_0_CURRENCYCODE=" + request.getParameter("currencyCodeType");
		session.setAttribute("currencyCodeType", request.getParameter("currencyCodeType"));
	}
	
	if(isSet(request.getParameter("PAYMENTREQUEST_0_ITEMAMT") )){
		nvpstr += "&PAYMENTREQUEST_0_ITEMAMT=" + request.getParameter("PAYMENTREQUEST_0_ITEMAMT");
		session.setAttribute("itemAmt", request.getParameter("PAYMENTREQUEST_0_ITEMAMT"));
	}
	
	if(isSet(request.getParameter("PAYMENTREQUEST_0_TAXAMT") )){
		nvpstr += "&PAYMENTREQUEST_0_TAXAMT=" + request.getParameter("PAYMENTREQUEST_0_TAXAMT");
		session.setAttribute("taxAmt", request.getParameter("PAYMENTREQUEST_0_TAXAMT"));
	}

	if(isSet(request.getParameter("PAYMENTREQUEST_0_SHIPPINGAMT") )){
		nvpstr += "&PAYMENTREQUEST_0_SHIPPINGAMT=" + request.getParameter("PAYMENTREQUEST_0_SHIPPINGAMT");
		session.setAttribute("shippingAmt", request.getParameter("PAYMENTREQUEST_0_SHIPPINGAMT"));
	}

	if(isSet(request.getParameter("PAYMENTREQUEST_0_HANDLINGAMT") )){
		nvpstr += "&PAYMENTREQUEST_0_HANDLINGAMT=" + request.getParameter("PAYMENTREQUEST_0_HANDLINGAMT");
		session.setAttribute("handlingAmt", request.getParameter("PAYMENTREQUEST_0_HANDLINGAMT"));
	}

	if(isSet(request.getParameter("PAYMENTREQUEST_0_SHIPDISCAMT") )){
		nvpstr += "&PAYMENTREQUEST_0_SHIPDISCAMT=" + request.getParameter("PAYMENTREQUEST_0_SHIPDISCAMT");
		session.setAttribute("shippingDiscAmt", request.getParameter("PAYMENTREQUEST_0_SHIPDISCAMT"));
	}
	
	if(isSet(request.getParameter("PAYMENTREQUEST_0_INSURANCEAMT") )){
		nvpstr += "&PAYMENTREQUEST_0_INSURANCEAMT=" + request.getParameter("PAYMENTREQUEST_0_INSURANCEAMT");
		session.setAttribute("insuranceAmt", request.getParameter("PAYMENTREQUEST_0_INSURANCEAMT"));
	}

	if(isSet(request.getParameter("L_PAYMENTREQUEST_0_NAME0")))
	nvpstr += "&L_PAYMENTREQUEST_0_NAME0=" + request.getParameter("L_PAYMENTREQUEST_0_NAME0");

	if(isSet(request.getParameter("L_PAYMENTREQUEST_0_NUMBER0")))
	nvpstr += "&L_PAYMENTREQUEST_0_NUMBER0=" + request.getParameter("L_PAYMENTREQUEST_0_NUMBER0");

	if(isSet(request.getParameter("L_PAYMENTREQUEST_0_DESC0")))
	nvpstr += "&L_PAYMENTREQUEST_0_DESC0=" + request.getParameter("L_PAYMENTREQUEST_0_DESC0");

	if(isSet(request.getParameter("PAYMENTREQUEST_0_ITEMAMT")))
	nvpstr += "&L_PAYMENTREQUEST_0_AMT0=" + request.getParameter("PAYMENTREQUEST_0_ITEMAMT");

	if(isSet(request.getParameter("L_PAYMENTREQUEST_0_QTY0")))
	nvpstr += "&L_PAYMENTREQUEST_0_QTY0=" + request.getParameter("L_PAYMENTREQUEST_0_QTY0");

	if(isSet(request.getParameter("LOGOIMG")))
	nvpstr += "&LOGOIMG="+ request.getParameter("LOGOIMG");
	
	/*Bill Me Later Specific Parameters*/
	nvpstr += "&LANDINGPAGE=Billing";
	nvpstr += "&USERSELECTEDFUNDINGSOURCE=BML";
	
	/*
    Make the call to PayPal to get the Express Checkout token
    If the API call succeeded, then redirect the buyer to PayPal
    to begin to authorize payment.  If an error occurred, show the
    resulting errors
    */
	Map<String, String> nvp = httpcall("SetExpressCheckout", nvpstr);
    String strAck = nvp.get("ACK").toString();
    if(strAck !=null && (strAck.equalsIgnoreCase("SUCCESS") || strAck.equalsIgnoreCase("SUCCESSWITHWARNING")  ))
    {
		session.setAttribute("TOKEN",decode(nvp.get("TOKEN")));
    }
    return nvp;
}

private boolean isSet(Object value){
	return (value !=null && value.toString().length()!=0);
}
private String encode(Object object){
	try {
		return URLEncoder.encode((String) object, "UTF-8");
	} catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return (String) object;
}
private String decode(Object object){
	try {
		return URLDecoder.decode((String) object, "UTF-8");
	} catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return (String) object;
}

/*********************************************************************************
  * GetShippingDetails: Function to perform the GetExpressCheckoutDetails API call
  *
  * Inputs:  token
  *
  * Output: Returns a HashMap object containing the response from the server.
*********************************************************************************/
public Map<String, String> getShippingDetails(HttpSession session, String token)
{
    /*
    Build a second API request to PayPal, using the token as the
    ID to get the details on the payment authorization
    */
    
   /*
    Make the API call and store the results in an array.  If the
    call was a success, show the authorization details, and provide
    an action to complete the payment.  If failed, show the error
    */

    Map<String, String> nvp = httpcall("GetExpressCheckoutDetails", "&TOKEN=" + token);
    String strAck = nvp.get("ACK").toString();
    if(strAck !=null && (strAck.equalsIgnoreCase("Success") || strAck.equalsIgnoreCase("SuccessWithWarning")))
    {
    	session.setAttribute("payerid", nvp.get("PAYERID"));
    	return nvp;
    }
    return null;
}

/*********************************************************************************
	* Purpose: 	Prepares the parameters for the DoExpressCheckoutPayment API Call.
	* Inputs:   FinalPaymentAmount:	The total transaction amount.
	* Returns: 	The NVP Collection object of the DoExpressCheckoutPayment Call Response.
*********************************************************************************/
public Map<String, String> confirmPayment(HttpSession session, String finalPaymentAmount, String serverName)
{

	/* Gather the information to make the final call to finalize the PayPal payment.  The variable nvpstr
     * holds the name value pairs
	 */
	String nvpstr="";
	//mandatory parameters in DoExpressCheckoutPayment call
	if(isSet(session.getAttribute("TOKEN")))
	nvpstr = "&TOKEN=" + encode(session.getAttribute("TOKEN"));

	if(isSet(session.getAttribute("payer_id")))
	nvpstr += "&PAYERID=" + encode(session.getAttribute("payer_id"));

	if(isSet(session.getAttribute("PaymentType")))
	nvpstr += "&PAYMENTREQUEST_0_PAYMENTACTION=" + encode(session.getAttribute("PaymentType")); 
	
	if(isSet(serverName))
	nvpstr += "&IPADDRESS=" + encode(serverName);

	nvpstr += "&PAYMENTREQUEST_0_AMT=" + finalPaymentAmount;
	

	//Check for additional parameters that can be passed in DoExpressCheckoutPayment API call
	if(isSet(session.getAttribute("currencyCodeType")))
	nvpstr += "&PAYMENTREQUEST_0_CURRENCYCODE=" + encode(session.getAttribute("currencyCodeType").toString());
	
	if(isSet(session.getAttribute("itemAmt")))
	nvpstr += "&PAYMENTREQUEST_0_ITEMAMT=" + encode(session.getAttribute("itemAmt").toString());

	if(isSet(session.getAttribute("taxAmt")))
	nvpstr += "&PAYMENTREQUEST_0_TAXAMT=" + encode(session.getAttribute("taxAmt").toString());

	if(isSet(session.getAttribute("shippingAmt")))
	nvpstr += "&PAYMENTREQUEST_0_SHIPPINGAMT=" + encode(session.getAttribute("shippingAmt").toString());

	if(isSet(session.getAttribute("handlingAmt")))
	nvpstr += "&PAYMENTREQUEST_0_HANDLINGAMT=" + encode(session.getAttribute("handlingAmt").toString());

	if(isSet(session.getAttribute("shippingDiscAmt")))
	nvpstr += "&PAYMENTREQUEST_0_SHIPDISCAMT=" + encode(session.getAttribute("shippingDiscAmt").toString());

	if(isSet(session.getAttribute("insuranceAmt")))
	nvpstr += "&PAYMENTREQUEST_0_INSURANCEAMT=" + encode(session.getAttribute("insuranceAmt").toString());

	 /*
	    Make the call to PayPal to finalize payment
	    If an error occurred, show the resulting errors
	  */
    Map<String, String> nvp = httpcall("DoExpressCheckoutPayment", nvpstr);
	/* Display the API response back to the browser.
	 * If the response from PayPal was a success, display the response parameters'
	 * If the response was an error, display the errors.
	 */
    String strAck = nvp.get("ACK").toString();
    if(strAck !=null && (strAck.equalsIgnoreCase("Success") || strAck.equalsIgnoreCase("SuccessWithWarning")))
    {
        return nvp;
    }
    return nvp;
    
}


/*********************************************************************************
  * httpcall: Function to perform the API call to PayPal using API signature
  * 	@ methodName is name of API  method.
  * 	@ nvpStr is nvp string.
  * returns a NVP string containing the response from the server.
*********************************************************************************/
public Map<String, String> httpcall( String methodName, String nvpStr)
{

    String agent = "Mozilla/4.0";
    String respText = "";
    Map<String, String> nvp = null; 

    //deformatNVP( nvpStr );
    String encodedData = "METHOD=" + methodName + "&VERSION=" + gvVersion + "&PWD=" + gvAPIPassword + "&USER=" + gvAPIUserName + "&SIGNATURE=" + gvAPISignature + nvpStr + "&BUTTONSOURCE=" + gvBNCode;

    try
    {
        URL postURL = new URL( gvAPIEndpoint );
        HttpURLConnection conn = (HttpURLConnection)postURL.openConnection();

        // Set connection parameters. We need to perform input and output,
        // so set both as true.
        conn.setDoInput (true);
        conn.setDoOutput (true);

        // Set the content type we are POSTing. We impersonate it as
        // encoded form data
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty( "User-Agent", agent );

        //conn.setRequestProperty( "Content-Type", type );
        conn.setRequestProperty( "Content-Length", String.valueOf( encodedData.length()) );
        conn.setRequestMethod("POST");

        // get the output stream to POST to.
        DataOutputStream output = new DataOutputStream( conn.getOutputStream());
        output.writeBytes( encodedData );
        output.flush();
        output.close ();

        new DataInputStream (conn.getInputStream());
        int rcode = conn.getResponseCode();
        if ( rcode != -1)
        {
            BufferedReader istream = new BufferedReader(new InputStreamReader( conn.getInputStream()));
            String line = null;
            while(((line = istream.readLine()) !=null))
            {
                respText = respText + line;
            }
            nvp = deformatNVP( respText );
        }
        return nvp;
    }
    catch( IOException e )
    {
        // handle the error here
        return null;
    }
}

/*********************************************************************************
  * deformatNVP: Function to break the NVP string into a HashMap
  * 	pPayLoad is the NVP string.
  * returns a HashMap object containing all the name value pairs of the string.
*********************************************************************************/
public Map<String, String> deformatNVP( String pPayload )
{
    Map<String, String> nvp = new HashMap<String, String>();
    StringTokenizer stTok = new StringTokenizer( pPayload, "&");
    
    while (stTok.hasMoreTokens())
    {
        StringTokenizer sinToken = new StringTokenizer( stTok.nextToken(), "=");
        if (sinToken.countTokens() == 2)
        {           String key;
			try {
				key = URLDecoder.decode(sinToken.nextToken(), "UTF-8");
	            String value;
				value = URLDecoder.decode(sinToken.nextToken(), "UTF-8");
				nvp.put( key.toUpperCase(), value );
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
            
        }
    }
    return nvp;
}

/*********************************************************************************
  * RedirectURL: Function to redirect the user to the PayPal site
  * 	token is the parameter that was returned by PayPal
  * returns a HashMap object containing all the name value pairs of the string.
*********************************************************************************/
public void redirectURL( HttpServletResponse response, String token )
{
    String payPalURL = paypalUrl + token;
    response.setStatus(302);
    response.setHeader( "Location", payPalURL );
    response.setHeader( "Connection", "close" );
     
}




//end class
}

