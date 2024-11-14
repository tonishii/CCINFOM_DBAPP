<%-- 
    Document   : registerSeller_processing.jsp
    Created on : Nov 13, 2024, 6:07:15 PM
    Author     : 36m36
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*, schemaobjects.*"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Register Seller Processing</title>
    </head>
    <body>
        <jsp:useBean id="sAccount" class="schemaobjects.Seller" scope="session"/>
        <%
            String v_sName = request.getParameter("sName");
            String v_sAddress = request.getParameter("sAddress");
            String v_sPhone = request.getParameter("sPhone");
            sAccount.seller_name=v_sName;
            sAccount.seller_address=v_sAddress;
            sAccount.seller_phone_number=v_sPhone;
            int id = sAccount.signUp();
            session.setAttribute("sId", id);
            response.sendRedirect("register_SellerResult.jsp");
        %>
    </body>
</html>
