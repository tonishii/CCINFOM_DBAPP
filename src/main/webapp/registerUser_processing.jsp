<%-- 
    Document   : registerUser_processing.jsp
    Created on : Nov 14, 2024, 8:25:47 PM
    Author     : 36m36
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*, schemaobjects.*"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Register User Processing</title>
    </head>
    <body>
        <jsp:useBean id="uAccount" class="schemaobjects.User" scope="session"/>
        <%
            String v_uName = request.getParameter("uName");
            String v_ufName = request.getParameter("fName");
            String v_ulName = request.getParameter("lName");
            String v_uPhone = request.getParameter("uPhone");
            String v_uAddress = request.getParameter("uAddress");
            uAccount.user_name=v_uName;
            uAccount.user_firstname=v_ufName;
            uAccount.user_lastname=v_ulName;
            uAccount.user_phone_number=v_uPhone;
            uAccount.user_address=v_uAddress;
            int id = uAccount.signUp();
            session.setAttribute("uId", id);
            response.sendRedirect("register_UserResult.jsp");
        %>
    </body>
</html>
