package com.paypal;

/*==================================================================
 PayPal Express Checkout Call
 ===================================================================
 */

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Lipp extends HttpServlet {

	// PayPal properties;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PayPal properties = new PayPal();
		HttpSession session = request.getSession(true);
		session.removeAttribute("Payment_Amount");
		session.removeAttribute("sellerUsername");
		session.removeAttribute("sellerPassword");
		session.removeAttribute("sellerSignature");
		session.removeAttribute("sellerRestAppClientId");
		session.removeAttribute("sellerRestAppClientSecret");
		session.removeAttribute("currencyCodeType");
		session.removeAttribute("paymentType");
		session.removeAttribute("L_PAYMENTREQUEST_0_NAME0");
		session.removeAttribute("L_PAYMENTREQUEST_0_NUMBER0");
		session.removeAttribute("L_PAYMENTREQUEST_0_DESC0");
		session.removeAttribute("L_PAYMENTREQUEST_0_QTY0");
		session.removeAttribute("L_PAYMENTREQUEST_0_AMT0");
		session.removeAttribute("PAYMENTREQUEST_0_ITEMAMT");
		session.removeAttribute("PAYMENTREQUEST_0_TAXAMT");
		session.removeAttribute("PAYMENTREQUEST_0_SHIPPINGAMT");
		session.removeAttribute("PAYMENTREQUEST_0_HANDLINGAMT");
		session.removeAttribute("PAYMENTREQUEST_0_SHIPDISCAMT");
		session.removeAttribute("PAYMENTREQUEST_0_INSURANCEAMT");
		session.removeAttribute("PAYMENTREQUEST_0_AMT");
		session.removeAttribute("LOGOIMG");
		if (request.getParameter("PAYMENTREQUEST_0_ITEMAMT") != null)
			session.setAttribute("Payment_Amount",
					request.getParameter("PAYMENTREQUEST_0_AMT"));

		if ((properties.getGv_APIUserName() != null))
			session.setAttribute("sellerUsername",
					properties.getGv_APIUserName());

		if ((properties.getGv_APIPassword() != null))
			session.setAttribute("sellerPassword",
					properties.getGv_APIPassword());

		if ((properties.getGv_APISignature() != null))
			session.setAttribute("sellerSignature",
					properties.getGv_APISignature());

		if ((properties.getGv_ClientId() != null))
			session.setAttribute("sellerRestAppClientId",
					properties.getGv_ClientId());

		if ((properties.getGv_ClientSecret() != null))
			session.setAttribute("sellerRestAppClientSecret",
					properties.getGv_ClientSecret());

		if ((request.getParameter("currencyCodeType") != null))
			session.setAttribute("currencyCodeType",
					request.getParameter("currencyCodeType"));

		if ((request.getParameter("paymentType") != null))
			session.setAttribute("paymentType",
					request.getParameter("paymentType"));

		if ((request.getParameter("L_PAYMENTREQUEST_0_NAME0") != null))
			session.setAttribute("L_PAYMENTREQUEST_0_NAME0",
					request.getParameter("L_PAYMENTREQUEST_0_NAME0"));

		if ((request.getParameter("L_PAYMENTREQUEST_0_NUMBER0") != null))
			session.setAttribute("L_PAYMENTREQUEST_0_NUMBER0",
					request.getParameter("L_PAYMENTREQUEST_0_NUMBER0"));

		if ((request.getParameter("L_PAYMENTREQUEST_0_DESC0") != null))
			session.setAttribute("L_PAYMENTREQUEST_0_DESC0",
					request.getParameter("L_PAYMENTREQUEST_0_DESC0"));

		if ((request.getParameter("L_PAYMENTREQUEST_0_QTY0") != null))
			session.setAttribute("L_PAYMENTREQUEST_0_QTY0",
					request.getParameter("L_PAYMENTREQUEST_0_QTY0"));

		if ((request.getParameter("PAYMENTREQUEST_0_ITEMAMT") != null))
			session.setAttribute("PAYMENTREQUEST_0_ITEMAMT",
					request.getParameter("PAYMENTREQUEST_0_ITEMAMT"));

		if ((request.getParameter("PAYMENTREQUEST_0_TAXAMT") != null))
			session.setAttribute("PAYMENTREQUEST_0_TAXAMT",
					request.getParameter("PAYMENTREQUEST_0_TAXAMT"));

		if ((request.getParameter("PAYMENTREQUEST_0_SHIPPINGAMT") != null))
			session.setAttribute("PAYMENTREQUEST_0_SHIPPINGAMT",
					request.getParameter("PAYMENTREQUEST_0_SHIPPINGAMT"));

		if ((request.getParameter("PAYMENTREQUEST_0_HANDLINGAMT") != null))
			session.setAttribute("PAYMENTREQUEST_0_HANDLINGAMT",
					request.getParameter("PAYMENTREQUEST_0_HANDLINGAMT"));

		if ((request.getParameter("PAYMENTREQUEST_0_SHIPDISCAMT") != null))
			session.setAttribute("PAYMENTREQUEST_0_SHIPDISCAMT",
					request.getParameter("PAYMENTREQUEST_0_SHIPDISCAMT"));

		if ((request.getParameter("PAYMENTREQUEST_0_INSURANCEAMT") != null))
			session.setAttribute("PAYMENTREQUEST_0_INSURANCEAMT",
					request.getParameter("PAYMENTREQUEST_0_INSURANCEAMT"));

		if ((request.getParameter("PAYMENTREQUEST_0_AMT") != null))
			session.setAttribute("PAYMENTREQUEST_0_AMT",
					request.getParameter("PAYMENTREQUEST_0_AMT"));

		if ((request.getParameter("LOGOIMG") != null))
			session.setAttribute("LOGOIMG", request.getParameter("LOGOIMG"));

		if ((request.getParameter("PAYMENTREQUEST_0_ITEMAMT") != null))
			session.setAttribute("L_PAYMENTREQUEST_0_AMT0",
					request.getParameter("PAYMENTREQUEST_0_ITEMAMT"));

		request.setAttribute("client_id",
				session.getAttribute("sellerRestAppClientId"));
		RequestDispatcher dispatcher = request.getRequestDispatcher("lipp.jsp");
		if (dispatcher != null) {
			dispatcher.forward(request, response);
		}

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}