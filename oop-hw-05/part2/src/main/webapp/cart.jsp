<%@ page import="java.math.BigDecimal" %>
<%@ page import="model.ShoppingCart" %>
<%@ page import="model.DataManager" %>
<%@ page import="java.util.ArrayList" %>

<%@ page contentType="text/html;charset=UTF-8" %>
<%
    ShoppingCart cart = (ShoppingCart) session.getAttribute("Shopping Cart");
    DataManager dm = (DataManager) application.getAttribute("Data Manager");
    ArrayList<DataManager.Product> products = dm.getProductsList(cart.getproductIds());
    BigDecimal total = new BigDecimal(0);
%>
<html>
    <head>
        <title>Shopping Cart</title>
    </head>

    <body>
        <h1>Shopping Cart</h1>

        <form action="CartController" method="post">
            <ul><%
                for (DataManager.Product product : products) {
                    int quantity = cart.getQuantityOf(product.id);
                    total = total.add(product.price.multiply(new BigDecimal(quantity)));
                    out.print("<li> <input type ='number' value='" + quantity + "' name='" + product.id
                            + "'>" + product.name + ", " + product.price + "</li>");
                }
            %></ul>
            Total: $ <%= total %> <input type="submit" value="Update Cart"/>
        </form>
        <a href="/">Continue shopping</a>
    </body>
</html>
