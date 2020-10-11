package controller;

import model.ShoppingCart;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;

@WebServlet(name = "CartController", urlPatterns = {"/CartController"})
public class CartController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        ShoppingCart cart = (ShoppingCart) session.getAttribute("Shopping Cart");

        String id = req.getParameter("productId");
        if (id == null) {
            Enumeration<String> params = req.getParameterNames();
            ShoppingCart newCart = new ShoppingCart();
            while (params.hasMoreElements()) {
                id = params.nextElement();
                newCart.addProduct(id, req.getParameter(id), cart.getQuantityOf(id));
            }
            cart = newCart;
        } else {
            cart.addProduct(id, 1);
        }

        session.setAttribute("Shopping Cart", cart);
        req.getRequestDispatcher("cart.jsp").forward(req, resp);
    }

}