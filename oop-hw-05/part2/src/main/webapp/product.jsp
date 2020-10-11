<%@ page import="model.DataManager" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<html>
    <%
        DataManager.Product product =  ((DataManager) application.getAttribute("Data Manager"))
                .getProductInfo(request.getParameter("id"));
    %>
    <head>
        <title><%=product.name%></title>
    </head>

    <body>
        <h1><%= product.name %></h1>
        <img src="<%="/images/"+product.img%>" alt="<%=product.name%>">

        <form action="CartController" method="post">
            $<%=product.price%> <input name="productId" type="hidden" value="<%= product.id %>"/>
            <input type="submit" value="Add to cart"/>
        </form>
    </body>
</html>
