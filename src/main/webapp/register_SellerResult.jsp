<%-- 
    Document   : register_SellerResult.jsp
    Created on : Nov 14, 2024, 7:29:17 PM
    Author     : 36m36
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*, schemaobjects.*"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Seller Register Result</title>
    </head>
    <body>
        <form action="index.html" method="POST">
            <%
                int id = (int)session.getAttribute("sId"); 
                if (id!=0){
            %>
                    <h1>Registering Seller Successful (ID: <%=id%>)</h1>
            <%  } else {
            %>
                    <h1>Registering Seller Failed</h1>
            <%  }
            %>
            <input type="submit" value="return to home menu">
        </form>
    </body>
</html>
