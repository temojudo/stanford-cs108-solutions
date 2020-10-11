<%@ page import="model.DataManager" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<html>
    <head>
        <title>Students Store</title>
    </head>

    <body>
        <h1>Students Store</h1>
        <h4>Items available:</h4>

        <ul><%
            for (DataManager.Product product :
                    ((DataManager) application.getAttribute("Data Manager")).getProductsList()) {
                out.print("<li><a href=\"product.jsp?id=" + product.id + "\">" + product.name + "</a></li>");
            }
        %></ul>
    </body>
</html>
