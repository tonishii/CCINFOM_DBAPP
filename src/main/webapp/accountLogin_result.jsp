<%-- 
    Document   : accountLogin_result.jsp
    Created on : Nov 14, 2024, 9:48:21 PM
    Author     : 36m36
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*, schemaobjects.*"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Account Login Result</title>
    </head>
    <body>
        <form action="index.html" method="POST">
            <%
                int id = (int)session.getAttribute("lAccount"); 
                String strAccType = (String)session.getAttribute("strAccount"); 
                if (id!=0){
            %>
                    <h1>Login <%=strAccType%> Successful (ID: <%=id%>)</h1>
            <%  } else {
            %>
                    <h1>Login Failed</h1>
            <%  }
            %>
            <input type="submit" value="return to home menu">
        </form>
    </body>
</html>
