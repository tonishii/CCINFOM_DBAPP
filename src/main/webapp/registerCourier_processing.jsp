<%-- 
    Document   : registerCourier_processing.jsp
    Created on : Nov 14, 2024, 5:08:13 PM
    Author     : 36m36
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*, schemaobjects.*"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Register Courier Processing</title>
    </head>
    <body>
        <jsp:useBean id="cAccount" class="schemaobjects.Courier" scope="session"/>
        <%
            String v_cName = request.getParameter("cName");
            String v_cAddress = request.getParameter("cAddress");
            String v_cEmail = request.getParameter("cEmail");
            cAccount.courier_name=v_cName;
            cAccount.courier_address=v_cAddress;
            cAccount.courier_email_address=v_cEmail;
            int id = cAccount.signUp();
            session.setAttribute("cId", id);
            response.sendRedirect("register_CourierResult.jsp");
        %>
    </body>
</html>
