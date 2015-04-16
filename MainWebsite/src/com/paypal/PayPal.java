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
import java.util.Properties;
import java.util.StringTokenizer;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class PayPal {

	private String gv_APIUserName;
	private String gv_APIPassword;
	private String gv_APISignature;
	private String gv_ClientId;
	private String gv_ClientSecret;
	private String gv_APIEndpoint;
	private String gv_BNCode;
	private String gv_token_service_Endpoint;
	private String gv_user_info_Endpoint;
	private String gv_Version;
	private String gv_nvpHeader;
	private String gv_ProxyServer;
	private String gv_ProxyServerPort;
	private int gv_Proxy;
	private boolean gv_UseProxy;
	private String PAYPAL_URL;

	public PayPal() { // lhuynh - Actions to be Done on init of this class
		Properties prop = new Properties();
		InputStream input = null;
		try {
			String filename = "config/config.properties";
			// input =
			// PayPal.class.getClassLoader().getResourceAsStream(filename);
			input = this.getClass().getClassLoader()
					.getResourceAsStream(filename);
			if (input == null) {
				System.out.println("Sorry, unable to find2 " + filename);
				return;
			}

			// load a properties file from class path, inside static method
			prop.load(input);
			// get the property value from config.properties file
			String strSandbox = "";
			if (prop.getProperty("SANDBOX_FLAG").equals("true")) {
				strSandbox = "_SANDBOX";
			}
			// ButtonSource Tracker Code
			gv_BNCode = prop.getProperty("SBN_CODE");

			gv_APIUserName = prop.getProperty("PP_USER" + strSandbox);
			gv_APIPassword = prop.getProperty("PP_PASSWORD" + strSandbox);
			gv_APISignature = prop.getProperty("PP_SIGNATURE" + strSandbox);

			gv_APIEndpoint = prop.getProperty("PP_NVP_ENDPOINT" + strSandbox);
			gv_token_service_Endpoint = prop
					.getProperty("PP_TOKEN_SERVICE_ENDPOINT" + strSandbox);
			gv_user_info_Endpoint = prop.getProperty("PP_USER_INFO_ENDPOINT"
					+ strSandbox);
			gv_ClientId = prop.getProperty("PP_CLIENT_ID" + strSandbox);
			gv_ClientSecret = prop.getProperty("PP_CLIENT_SECRET" + strSandbox);
			PAYPAL_URL = prop.getProperty("PP_CHECKOUT_URL" + strSandbox);

			gv_Version = prop.getProperty("API_VERSION");
			// WinObjHttp Request proxy settings.
			gv_ProxyServer = prop.getProperty("PROXY_HOST");
			gv_ProxyServerPort = prop.getProperty("PROXY_PORT");
			gv_Proxy = 2; // setting for proxy activation
			gv_UseProxy = false;
			java.lang.System.setProperty("https.protocols", prop.getProperty("SSL_VERSION_TO_USE"));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public String getGv_ClientId() {
		return gv_ClientId;
	}

	public String getGv_APIUserName() {
		return gv_APIUserName;
	}

	public void setGv_APIUserName(String gv_APIUserName) {
		this.gv_APIUserName = gv_APIUserName;
	}

	public String getGv_APIPassword() {
		return gv_APIPassword;
	}

	public void setGv_APIPassword(String gv_APIPassword) {
		this.gv_APIPassword = gv_APIPassword;
	}

	public String getGv_APISignature() {
		return gv_APISignature;
	}

	public void setGv_APISignature(String gv_APISignature) {
		this.gv_APISignature = gv_APISignature;
	}

	public String getGv_ClientSecret() {
		return gv_ClientSecret;
	}

	public void setGv_ClientSecret(String gv_ClientSecret) {
		this.gv_ClientSecret = gv_ClientSecret;
	}

	/*********************************************************************************
	 * CallShortcutExpressCheckout: Function to perform the SetExpressCheckout
	 * API call
	 * 
	 * Inputs: request: the item details, prices and taxes returnURL: the page
	 * where buyers return to after they are done with the payment review on
	 * PayPal cancelURL: the page where buyers return to when they cancel the
	 * payment review on PayPal
	 * 
	 * Output: Returns a HashMap object containing the response from the server.
	 *********************************************************************************/
	public HashMap callShortcutExpressCheckout(HttpServletRequest request, String returnURL, String cancelURL, String identityAccessToken) {

		// Construct the parameter string that describes the SetExpressCheckout
		// API call in the shortcut implementation
		// For more information on the customizing the parameters passed refer:
		// https://developer.paypal.com/docs/classic/express-checkout/integration-guide/ECCustomizing/

		// Mandatory parameters for SetExpressCheckout API call

		String nvpstr = "";
		HttpSession session = request.getSession();
		if (session.getAttribute("PAYMENTREQUEST_0_AMT") != null)
			nvpstr += "&PAYMENTREQUEST_0_AMT="
					+ session.getAttribute("PAYMENTREQUEST_0_AMT");

		if (session.getAttribute("paymentType") != null)
			nvpstr += "&PAYMENTREQUEST_0_PAYMENTACTION="
					+ session.getAttribute("paymentType");
		if (returnURL != null)
			nvpstr += "&RETURNURL=" + returnURL;
		if (cancelURL != null)
			nvpstr += "&CANCELURL=" + cancelURL;
		if (identityAccessToken != null)
			nvpstr += "&IDENTITYACCESSTOKEN=" + identityAccessToken;
		if (session.getAttribute("currencyCodeType") != null)
			nvpstr += "&PAYMENTREQUEST_0_CURRENCYCODE="
					+ session.getAttribute("currencyCodeType");
		if (session.getAttribute("L_PAYMENTREQUEST_0_NAME0") != null)
			nvpstr += "&L_PAYMENTREQUEST_0_NAME0="
					+ session.getAttribute("L_PAYMENTREQUEST_0_NAME0");
		if (session.getAttribute("L_PAYMENTREQUEST_0_NUMBER0") != null)
			nvpstr += "&L_PAYMENTREQUEST_0_NUMBER0="
					+ session.getAttribute("L_PAYMENTREQUEST_0_NUMBER0");
		if (session.getAttribute("L_PAYMENTREQUEST_0_DESC0") != null)
			nvpstr += "&L_PAYMENTREQUEST_0_DESC0="
					+ session.getAttribute("L_PAYMENTREQUEST_0_DESC0");
		if (session.getAttribute("L_PAYMENTREQUEST_0_AMT0") != null)
			nvpstr += "&L_PAYMENTREQUEST_0_AMT0="
					+ session.getAttribute("L_PAYMENTREQUEST_0_AMT0");
		if (session.getAttribute("L_PAYMENTREQUEST_0_QTY0") != null)
			nvpstr += "&L_PAYMENTREQUEST_0_QTY0="
					+ session.getAttribute("L_PAYMENTREQUEST_0_QTY0");
		if (session.getAttribute("PAYMENTREQUEST_0_ITEMAMT") != null)
			nvpstr += "&PAYMENTREQUEST_0_ITEMAMT="
					+ session.getAttribute("PAYMENTREQUEST_0_ITEMAMT");
		if (session.getAttribute("PAYMENTREQUEST_0_TAXAMT") != null)
			nvpstr += "&PAYMENTREQUEST_0_TAXAMT="
					+ session.getAttribute("PAYMENTREQUEST_0_TAXAMT");
		if (session.getAttribute("PAYMENTREQUEST_0_SHIPPINGAMT") != null)
			nvpstr += "&PAYMENTREQUEST_0_SHIPPINGAMT="
					+ session.getAttribute("PAYMENTREQUEST_0_SHIPPINGAMT");
		if (session.getAttribute("PAYMENTREQUEST_0_HANDLINGAMT") != null)
			nvpstr += "&PAYMENTREQUEST_0_HANDLINGAMT="
					+ session.getAttribute("PAYMENTREQUEST_0_HANDLINGAMT");
		if (session.getAttribute("PAYMENTREQUEST_0_SHIPDISCAMT") != null)
			nvpstr += "&PAYMENTREQUEST_0_SHIPDISCAMT="
					+ session.getAttribute("PAYMENTREQUEST_0_SHIPDISCAMT");
		if (session.getAttribute("PAYMENTREQUEST_0_INSURANCEAMT") != null)
			nvpstr += "&PAYMENTREQUEST_0_INSURANCEAMT="
					+ session.getAttribute("PAYMENTREQUEST_0_INSURANCEAMT");
		if (session.getAttribute("LOGOIMG") != null)
			nvpstr += "&LOGOIMG=" + session.getAttribute("LOGOIMG");

		/*
		 * Make the call to PayPal to get the Express Checkout token If the API
		 * call succeeded, then redirect the buyer to PayPal to begin to
		 * authorize payment. If an error occurred, show the resulting errors
		 */
		System.out.println("nvp string is " + nvpstr);
		HashMap<String, String> nvp = httpcall("SetExpressCheckout", nvpstr);
		String strAck = nvp.get("ACK").toString();
		if (strAck != null
				&& (strAck.equalsIgnoreCase("SUCCESS") || strAck
						.equalsIgnoreCase("SUCCESSWITHWARNING"))) {
			session.setAttribute("TOKEN", decode(nvp.get("TOKEN")));
		}
		System.out.println(nvp);
		return nvp;
	}

	private Boolean isSet(Object value) {
		return (value != null && value.toString().length() != 0);
	}

	private String encode(Object object) {
		try {
			return URLEncoder.encode((String) object, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (String) object;
	}

	private String decode(Object object) {
		try {
			return URLDecoder.decode((String) object, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (String) object;
	}

	/*********************************************************************************
	 * CallMarkExpressCheckout: Function to perform the SetExpressCheckout API
	 * call
	 * 
	 * Inputs: paymentAmount: Total value of the shopping cart currencyCodeType:
	 * Currency code value the PayPal API paymentType: paymentType has to be one
	 * of the following values: Sale or Order or Authorization returnURL: the
	 * page where buyers return to after they are done with the payment review
	 * on PayPal cancelURL: the page where buyers return to when they cancel the
	 * payment review on PayPal shipToName: the Ship to name entered on the
	 * merchant's site shipToStreet: the Ship to Street entered on the
	 * merchant's site shipToCity: the Ship to City entered on the merchant's
	 * site shipToState: the Ship to State entered on the merchant's site
	 * shipToCountryCode: the Code for Ship to Country entered on the merchant's
	 * site shipToZip: the Ship to ZipCode entered on the merchant's site
	 * shipToStreet2: the Ship to Street2 entered on the merchant's site
	 * phoneNum: the phoneNum entered on the merchant's site
	 * 
	 * Output: Returns a HashMap object containing the response from the server.
	 *********************************************************************************/
	public HashMap callMarkExpressCheckout(HttpServletRequest request,
			HashMap shippingDetail) {
		HttpSession session = request.getSession(true);
		// ------------------------------------------------------------------------------------------------------------------------------------
		// Construct the parameter string that describes the SetExpressCheckout
		// API call in the shortcut implementation
		String nvpstr = "";
		// Mandatory parameters for SetExpressCheckout API call
		if (isSet(request.getParameter("PAYMENTREQUEST_0_AMT"))) {
			nvpstr = "&PAYMENTREQUEST_0_AMT="
					+ request.getParameter("PAYMENTREQUEST_0_AMT");
			session.setAttribute("Payment_Amount",
					request.getParameter("PAYMENTREQUEST_0_AMT"));
		}

		if (isSet(request.getParameter("paymentType"))) {
			nvpstr += "&PAYMENTREQUEST_0_PAYMENTACTION="
					+ request.getParameter("paymentType");
			session.setAttribute("PaymentType",
					request.getParameter("paymentType"));
		}

		if (isSet(request.getParameter("RETURN_URL")))
			nvpstr += "&RETURNURL=" + request.getParameter("RETURN_URL");

		if (isSet(request.getParameter("CANCEL_URL")))
			nvpstr += "&CANCELURL=" + request.getParameter("CANCEL_URL");

		// Optional parameters for SetExpressCheckout API call
		if (isSet(request.getParameter("currencyCodeType"))) {
			nvpstr += "&PAYMENTREQUEST_0_CURRENCYCODE="
					+ request.getParameter("currencyCodeType");
			session.setAttribute("currencyCodeType",
					request.getParameter("currencyCodeType"));
		}

		if (isSet(request.getParameter("PAYMENTREQUEST_0_ITEMAMT"))) {
			nvpstr += "&PAYMENTREQUEST_0_ITEMAMT="
					+ request.getParameter("PAYMENTREQUEST_0_ITEMAMT");
			session.setAttribute("itemAmt",
					request.getParameter("PAYMENTREQUEST_0_ITEMAMT"));
		}

		if (isSet(request.getParameter("PAYMENTREQUEST_0_TAXAMT"))) {
			nvpstr += "&PAYMENTREQUEST_0_TAXAMT="
					+ request.getParameter("PAYMENTREQUEST_0_TAXAMT");
			session.setAttribute("taxAmt",
					request.getParameter("PAYMENTREQUEST_0_TAXAMT"));
		}

		if (isSet(request.getParameter("PAYMENTREQUEST_0_SHIPPINGAMT"))) {
			nvpstr += "&PAYMENTREQUEST_0_SHIPPINGAMT="
					+ request.getParameter("PAYMENTREQUEST_0_SHIPPINGAMT");
			session.setAttribute("shippingAmt",
					request.getParameter("PAYMENTREQUEST_0_SHIPPINGAMT"));
		}

		if (isSet(request.getParameter("PAYMENTREQUEST_0_HANDLINGAMT"))) {
			nvpstr += "&PAYMENTREQUEST_0_HANDLINGAMT="
					+ request.getParameter("PAYMENTREQUEST_0_HANDLINGAMT");
			session.setAttribute("handlingAmt",
					request.getParameter("PAYMENTREQUEST_0_HANDLINGAMT"));
		}

		if (isSet(request.getParameter("PAYMENTREQUEST_0_SHIPDISCAMT"))) {
			nvpstr += "&PAYMENTREQUEST_0_SHIPDISCAMT="
					+ request.getParameter("PAYMENTREQUEST_0_SHIPDISCAMT");
			session.setAttribute("shippingDiscAmt",
					request.getParameter("PAYMENTREQUEST_0_SHIPDISCAMT"));
		}

		if (isSet(request.getParameter("PAYMENTREQUEST_0_INSURANCEAMT"))) {
			nvpstr += "&PAYMENTREQUEST_0_INSURANCEAMT="
					+ request.getParameter("PAYMENTREQUEST_0_INSURANCEAMT");
			session.setAttribute("insuranceAmt",
					request.getParameter("PAYMENTREQUEST_0_INSURANCEAMT"));
		}

		if (isSet(request.getParameter("L_PAYMENTREQUEST_0_NAME0")))
			nvpstr += "&L_PAYMENTREQUEST_0_NAME0="
					+ request.getParameter("L_PAYMENTREQUEST_0_NAME0");

		if (isSet(request.getParameter("L_PAYMENTREQUEST_0_NUMBER0")))
			nvpstr += "&L_PAYMENTREQUEST_0_NUMBER0="
					+ request.getParameter("L_PAYMENTREQUEST_0_NUMBER0");

		if (isSet(request.getParameter("L_PAYMENTREQUEST_0_DESC0")))
			nvpstr += "&L_PAYMENTREQUEST_0_DESC0="
					+ request.getParameter("L_PAYMENTREQUEST_0_DESC0");

		if (isSet(request.getParameter("L_PAYMENTREQUEST_0_AMT0")))
			nvpstr += "&L_PAYMENTREQUEST_0_AMT0="
					+ request.getParameter("L_PAYMENTREQUEST_0_AMT0");

		if (isSet(request.getParameter("L_PAYMENTREQUEST_0_QTY0")))
			nvpstr += "&L_PAYMENTREQUEST_0_QTY0="
					+ request.getParameter("L_PAYMENTREQUEST_0_QTY0");

		if (isSet(request.getParameter("LOGOIMG")))
			nvpstr += "&LOGOIMG=" + request.getParameter("LOGOIMG");

		nvpstr += "&ADDROVERRIDE=1";

		// Shipping parameters for API call

		if (isSet(shippingDetail.get("L_PAYMENTREQUEST_FIRSTNAME"))) {
			String fullname = (String) shippingDetail
					.get("L_PAYMENTREQUEST_FIRSTNAME");
			if (isSet(shippingDetail.get("L_PAYMENTREQUEST_LASTNAME")))
				fullname = fullname
						+ " "
						+ (String) shippingDetail
								.get("L_PAYMENTREQUEST_LASTNAME");

			nvpstr += "&PAYMENTREQUEST_n_SHIPTONAME=" + fullname;
			session.setAttribute("shipToName", fullname);
		}

		if (isSet(shippingDetail.get("PAYMENTREQUEST_0_SHIPTOSTREET"))) {
			nvpstr += "&PAYMENTREQUEST_0_SHIPTOSTREET="
					+ shippingDetail.get("PAYMENTREQUEST_0_SHIPTOSTREET");
			session.setAttribute("shipToAddress",
					shippingDetail.get("PAYMENTREQUEST_0_SHIPTOSTREET"));
		}

		if (isSet(shippingDetail.get("PAYMENTREQUEST_0_SHIPTOSTREET2"))) {
			nvpstr += "&PAYMENTREQUEST_0_SHIPTOSTREET2="
					+ shippingDetail.get("PAYMENTREQUEST_0_SHIPTOSTREET2");
			session.setAttribute("shipToAddress2",
					shippingDetail.get("PAYMENTREQUEST_0_SHIPTOSTREET2"));
		}

		if (isSet(shippingDetail.get("PAYMENTREQUEST_0_SHIPTOCITY"))) {
			nvpstr += "&PAYMENTREQUEST_0_SHIPTOCITY="
					+ shippingDetail.get("PAYMENTREQUEST_0_SHIPTOCITY");
			session.setAttribute("shipToCity",
					shippingDetail.get("PAYMENTREQUEST_0_SHIPTOCITY"));
		}

		if (isSet(shippingDetail.get("PAYMENTREQUEST_0_SHIPTOSTATE"))) {
			nvpstr += "&PAYMENTREQUEST_0_SHIPTOSTATE="
					+ shippingDetail.get("PAYMENTREQUEST_0_SHIPTOSTATE");
			session.setAttribute("shipToState",
					shippingDetail.get("PAYMENTREQUEST_0_SHIPTOSTATE"));
		}
		if (isSet(shippingDetail.get("PAYMENTREQUEST_0_SHIPTOZIP"))) {
			nvpstr += "&PAYMENTREQUEST_0_SHIPTOZIP="
					+ shippingDetail.get("PAYMENTREQUEST_0_SHIPTOZIP");
			session.setAttribute("shipToZip",
					shippingDetail.get("PAYMENTREQUEST_0_SHIPTOZIP"));
		}
		if (isSet(shippingDetail.get("PAYMENTREQUEST_0_SHIPTOCOUNTRY"))) {
			nvpstr += "&PAYMENTREQUEST_0_SHIPTOCOUNTRY="
					+ shippingDetail.get("PAYMENTREQUEST_0_SHIPTOCOUNTRY");
			session.setAttribute("shipToCountry",
					shippingDetail.get("PAYMENTREQUEST_0_SHIPTOCOUNTRY"));
		}
		if (isSet(shippingDetail.get("PAYMENTREQUEST_0_SHIPTOPHONENUM"))) {
			nvpstr += "&PAYMENTREQUEST_0_SHIPTOPHONENUM="
					+ shippingDetail.get("PAYMENTREQUEST_0_SHIPTOPHONENUM");
			session.setAttribute("shipToPhone",
					shippingDetail.get("PAYMENTREQUEST_0_SHIPTOPHONENUM"));
		}

		/*
		 * Make the call to PayPal to set the Express Checkout token If the API
		 * call succeded, then redirect the buyer to PayPal to begin to
		 * authorize payment. If an error occured, show the resulting errors
		 */
		HashMap<String, String> nvp = httpcall("SetExpressCheckout", nvpstr);
		String strAck = nvp.get("ACK").toString();
		if (strAck != null
				&& (strAck.equalsIgnoreCase("SUCCESS") || strAck
						.equalsIgnoreCase("SUCCESSWITHWARNING"))) {
			session.setAttribute("TOKEN", decode(nvp.get("TOKEN")));
		}
		return nvp;

	}

	/*********************************************************************************
	 * GetShippingDetails: Function to perform the GetExpressCheckoutDetails API
	 * call
	 * 
	 * Inputs: None
	 * 
	 * Output: Returns a HashMap object containing the response from the server.
	 *********************************************************************************/
	public HashMap getShippingDetails(HttpSession session, String token) {
		/*
		 * Build a second API request to PayPal, using the token as the ID to
		 * get the details on the payment authorization
		 */

		String nvpstr = "&TOKEN=" + token;

		/*
		 * Make the API call and store the results in an array. If the call was
		 * a success, show the authorization details, and provide an action to
		 * complete the payment. If failed, show the error
		 */

		HashMap nvp = httpcall("GetExpressCheckoutDetails", nvpstr);
		String strAck = nvp.get("ACK").toString();
		if (strAck != null
				&& (strAck.equalsIgnoreCase("Success") || strAck
						.equalsIgnoreCase("SuccessWithWarning"))) {
			session.setAttribute("payer_id", nvp.get("PAYERID"));
			return nvp;
		}
		return null;
	}

	/*********************************************************************************
	 * Purpose: Prepares the parameters for the DoExpressCheckoutPayment API
	 * Call. Inputs: FinalPaymentAmount: The total transaction amount. Returns:
	 * The NVP Collection object of the DoExpressCheckoutPayment Call Response.
	 *********************************************************************************/
	public HashMap confirmPayment(HttpSession session,
			String finalPaymentAmount, String serverName) {

		/*
		 * Gather the information to make the final call to finalize the PayPal
		 * payment. The variable nvpstr holds the name value pairs
		 */
		String nvpstr = "";
		// mandatory parameters in DoExpressCheckoutPayment call
		if (isSet(session.getAttribute("TOKEN")))
			nvpstr = "&TOKEN=" + encode(session.getAttribute("TOKEN"));

		if (isSet(session.getAttribute("payer_id")))
			nvpstr += "&PAYERID=" + encode(session.getAttribute("payer_id"));

		if (isSet(session.getAttribute("PaymentType")))
			nvpstr += "&PAYMENTREQUEST_0_PAYMENTACTION="
					+ encode(session.getAttribute("PaymentType"));

		if (isSet(serverName))
			nvpstr += "&IPADDRESS=" + encode(serverName);

		nvpstr += "&PAYMENTREQUEST_0_AMT=" + finalPaymentAmount;

		// Check for additional parameters that can be passed in
		// DoExpressCheckoutPayment API call
		if (isSet(session.getAttribute("currencyCodeType")))
			nvpstr += "&PAYMENTREQUEST_0_CURRENCYCODE="
					+ encode(session.getAttribute("currencyCodeType"));

		if (isSet(session.getAttribute("itemAmt")))
			nvpstr += "&PAYMENTREQUEST_0_ITEMAMT="
					+ encode(session.getAttribute("itemAmt"));

		if (isSet(session.getAttribute("taxAmt")))
			nvpstr += "&PAYMENTREQUEST_0_TAXAMT="
					+ encode(session.getAttribute("taxAmt"));

		if (isSet(session.getAttribute("shippingAmt")))
			nvpstr += "&PAYMENTREQUEST_0_SHIPPINGAMT="
					+ encode(session.getAttribute("shippingAmt"));

		if (isSet(session.getAttribute("handlingAmt")))
			nvpstr += "&PAYMENTREQUEST_0_HANDLINGAMT="
					+ encode(session.getAttribute("handlingAmt"));

		if (isSet(session.getAttribute("shippingDiscAmt")))
			nvpstr += "&PAYMENTREQUEST_0_SHIPDISCAMT="
					+ encode(session.getAttribute("shippingDiscAmt"));

		if (isSet(session.getAttribute("insuranceAmt")))
			nvpstr += "&PAYMENTREQUEST_0_INSURANCEAMT="
					+ encode(session.getAttribute("insuranceAmt"));

		/*
		 * Make the call to PayPal to finalize payment If an error occurred,
		 * show the resulting errors
		 */
		HashMap nvp = httpcall("DoExpressCheckoutPayment", nvpstr);
		/*
		 * Display the API response back to the browser. If the response from
		 * PayPal was a success, display the response parameters' If the
		 * response was an error, display the errors.
		 */
		String strAck = nvp.get("ACK").toString();
		if (strAck != null
				&& (strAck.equalsIgnoreCase("Success") || strAck
						.equalsIgnoreCase("SuccessWithWarning"))) {
			return nvp;
		}
		return null;

	}

	/*********************************************************************************
	 * httpcall: Function to perform the API call to PayPal using API signature @
	 * methodName is name of API method. @ nvpStr is nvp string. returns a NVP
	 * string containing the response from the server.
	 *********************************************************************************/
	public HashMap<String, String> httpcall(String methodName, String nvpStr) {

		String version = "2.3";
		String agent = "Mozilla/4.0";
		String respText = "";
		HashMap nvp = null;

		// deformatNVP( nvpStr );
		String encodedData = "METHOD=" + methodName + "&VERSION=" + gv_Version
				+ "&PWD=" + gv_APIPassword + "&USER=" + gv_APIUserName
				+ "&SIGNATURE=" + gv_APISignature + nvpStr + "&BUTTONSOURCE="
				+ gv_BNCode;

		try {
			URL postURL = new URL(gv_APIEndpoint);
			HttpURLConnection conn = (HttpURLConnection) postURL
					.openConnection();

			// Set connection parameters. We need to perform input and output,
			// so set both as true.
			conn.setDoInput(true);
			conn.setDoOutput(true);

			// Set the content type we are POSTing. We impersonate it as
			// encoded form data
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			conn.setRequestProperty("User-Agent", agent);

			// conn.setRequestProperty( "Content-Type", type );
			conn.setRequestProperty("Content-Length",
					String.valueOf(encodedData.length()));
			conn.setRequestMethod("POST");

			// get the output stream to POST to.
			DataOutputStream output = new DataOutputStream(
					conn.getOutputStream());
			output.writeBytes(encodedData);
			output.flush();
			output.close();

			// Read input from the input stream.
			DataInputStream in = new DataInputStream(conn.getInputStream());
			int rc = conn.getResponseCode();
			if (rc != -1) {
				BufferedReader is = new BufferedReader(new InputStreamReader(
						conn.getInputStream()));
				String _line = null;
				while (((_line = is.readLine()) != null)) {
					respText = respText + _line;
				}
				nvp = deformatNVP(respText);
			}
			return nvp;
		} catch (IOException e) {
			// handle the error here
			return null;
		}
	}

	/*********************************************************************************
	 * deformatNVP: Function to break the NVP string into a HashMap pPayLoad is
	 * the NVP string. returns a HashMap object containing all the name value
	 * pairs of the string.
	 *********************************************************************************/
	public HashMap deformatNVP(String pPayload) {
		HashMap nvp = new HashMap();
		StringTokenizer stTok = new StringTokenizer(pPayload, "&");
		while (stTok.hasMoreTokens()) {
			StringTokenizer stInternalTokenizer = new StringTokenizer(
					stTok.nextToken(), "=");
			if (stInternalTokenizer.countTokens() == 2) {
				String key;
				try {
					key = URLDecoder.decode(stInternalTokenizer.nextToken(),
							"UTF-8");
					String value;
					value = URLDecoder.decode(stInternalTokenizer.nextToken(),
							"UTF-8");
					nvp.put(key.toUpperCase(), value);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
		return nvp;
	}

	/*********************************************************************************
	 * RedirectURL: Function to redirect the user to the PayPal site token is
	 * the parameter that was returned by PayPal returns a HashMap object
	 * containing all the name value pairs of the string.
	 *********************************************************************************/
	public void redirectURL(HttpServletResponse response, String token) {
		String payPalURL = PAYPAL_URL + token;

		// response.sendRedirect( payPalURL );
		response.setStatus(302);
		response.setHeader("Location", payPalURL);
		response.setHeader("Connection", "close");
	}

	/*********************************************************************************
	 * acquire_access_token: Function to perform the API call to PayPal using
	 * APP ID and APP SECRET.It returns access token from authorization code
	 * which is to be used while redirecting to PayPal.
	 *********************************************************************************/
	public String acquire_access_token(HttpServletRequest request, String code) {
		HttpSession session = request.getSession();
		String accessToken = "";
		try {
			URL obj = new URL(gv_token_service_Endpoint);
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setRequestMethod("POST");
			String urlParameters = "client_id="
					+ session.getAttribute("sellerRestAppClientId")
					+ "&client_secret="
					+ session.getAttribute("sellerRestAppClientSecret")
					+ "&grant_type=authorization_code&code=" + code;

			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			JSONObject resObj = new JSONObject(response.toString());
			accessToken = resObj.getString("access_token");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return accessToken;
	}

	/*********************************************************************************
	 * acquire_paypal_user_profile: Function to perform the API call to PayPal
	 * using Access Token.It returns User Profile which can be used to display
	 * User Information.
	 *********************************************************************************/
	public String acquire_paypal_user_profile(String accessToken) {
		StringBuffer response = new StringBuffer();
		try {
			URL obj = new URL(gv_user_info_Endpoint);
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setRequestMethod("GET");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("Authorization", "Bearer " + accessToken);
			con.setDoOutput(true);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return response.toString();
	}

	// end class
}