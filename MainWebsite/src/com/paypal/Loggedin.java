package com.paypal;

/*==================================================================
 PayPal Express Checkout Call
 ===================================================================
 */

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

public class Loggedin extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PayPal helper = new PayPal();
		HashMap<String, String> userInfo = new HashMap<String, String>();
		HttpSession session = request.getSession(true);
		if (request.getParameter("code") != null) {

			// Get access token from the authorization code
			String access_token = helper.acquire_access_token(request,
					request.getParameter("code"));

			try {
				if (access_token == null) {
					throw new Exception("Failed to get access token");
				}

				session.setAttribute("access_token", access_token);
				String profile = "";
				// Get user profile details using the access token
				profile = helper.acquire_paypal_user_profile(access_token);
				if (profile == "") {
					throw new Exception("Failed to get user profile");
				}
				JSONObject profileObj = new JSONObject(profile);
				userInfo.put("email", profileObj.getString("email"));
				userInfo.put("given_name", profileObj.getString("given_name"));
				userInfo.put("family_name", profileObj.getString("family_name"));
				userInfo.put("language", profileObj.getString("language"));
				userInfo.put("phone_number",
						profileObj.getString("phone_number"));
				userInfo.put(
						"street_address",
						profileObj.getJSONObject("address").getString(
								"street_address"));
				userInfo.put("locality", profileObj.getJSONObject("address")
						.getString("locality"));
				userInfo.put("region", profileObj.getJSONObject("address")
						.getString("region"));
				userInfo.put("postal_code", profileObj.getJSONObject("address")
						.getString("postal_code"));
				userInfo.put("country", profileObj.getJSONObject("address")
						.getString("country"));
				userInfo.put("access_token", access_token);
				session.setAttribute("username",
						profileObj.getString("given_name"));
				session.setAttribute("user", userInfo);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		response.sendRedirect("ecreturn.jsp");
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}