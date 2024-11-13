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
        <form action="index.html">
            <jsp:useBean id="account" class="schemaobjects.Seller" scope="session"/>
            <%
                String v_sName = request.getParameter("sName");
                String v_sAddress = request.getParameter("sAddress");
                String v_sPhone = request.getParameter("sPhone");
                account.seller_name=v_sName;
                account.seller_address=v_sAddress;
                account.seller_phone_number=v_sPhone;
                int status = account.signUp();
                if (status!=0){
            %>
                    <h1>Registering Seller Successful</h1>
            <%  } else {
            %>
                    <h1>Registering Seller Failed</h1>
            <%  }
            %>
            <input type="submit" value="return to home menu">
        </form>
    </body>
</html>
