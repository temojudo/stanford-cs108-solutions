package controller;

import model.AccountManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("message", "Welcome to Homework 5");
        req.getRequestDispatcher("/WEB-INF/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        AccountManager accountManager = (AccountManager) getServletContext().getAttribute(AccountManager.ATTRIBUTE_NAME);

        if (accountManager.isCorrectPassword(req.getParameter("username"), req.getParameter("password"))) {
            req.setAttribute("username", req.getParameter("username"));
            req.getRequestDispatcher("/WEB-INF/homepage.jsp").forward(req, resp);
        } else {
            req.setAttribute("message", "Incorrect username and/or password, try again");
            req.getRequestDispatcher("/WEB-INF/login.jsp").forward(req, resp);
        }
    }

}
