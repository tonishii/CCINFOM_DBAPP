<%-- 
    Document   : accountLogin_processing.jsp
    Created on : Nov 14, 2024, 9:12:33 PM
    Author     : 36m36
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*, schemaobjects.*"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Account Login Processing</title>
    </head>
    <body>
        <%
            int v_accType = Integer.parseInt(request.getParameter("accType"));
            int v_accountId = Integer.parseInt(request.getParameter("accountId")); 
            switch (v_accType){
                case 1:
        %>
                    <jsp:useBean id="lUserAccount" class="schemaobjects.User" scope="session"/>    
        <%          session.setAttribute("lAccount", lUserAccount.login(v_accountId));
                    session.setAttribute("strAccount", "User");
                    break;
                case 2:
        %>
                    <jsp:useBean id="lSellerAccount" class="schemaobjects.Seller" scope="session"/>    
        <%          session.setAttribute("lAccount",lSellerAccount.login(v_accountId));
                    session.setAttribute("strAccount", "Seller");
                    break;
                case 3:
        %>
                    <jsp:useBean id="lCourierAccount" class="schemaobjects.Courier" scope="session"/>
        <%          session.setAttribute("lAccount",lCourierAccount.login(v_accountId));
                    session.setAttribute("strAccount", "Courier");
                    break;
            }
            response.sendRedirect("accountLogin_result.jsp");
        %>
        
    </body>
</html>
