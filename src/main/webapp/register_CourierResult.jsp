<%-- 
    Document   : register_CourierResult.jsp
    Created on : Nov 14, 2024, 6:32:54 PM
    Author     : 36m36
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*, schemaobjects.*"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Courier Register Result</title>
    </head>
    <body>
        <form action="index.html" method="POST">
            <%
                int id = (int)session.getAttribute("cId"); 
                if (id!=0){
            %>
                    <h1>Registering Courier Successful (ID: <%=id%>)</h1>
            <%  } else {
            %>
                    <h1>Registering Courier Failed</h1>
            <%  }
            %>
            <input type="submit" value="return to home menu">
        </form>
    </body>
</html>
