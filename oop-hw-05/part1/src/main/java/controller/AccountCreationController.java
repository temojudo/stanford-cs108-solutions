package controller;

import model.AccountManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AccountCreationController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("username", req.getParameter("username"));
        AccountManager accountManager = (AccountManager) getServletContext().getAttribute(AccountManager.ATTRIBUTE_NAME);

        if (!accountManager.hasAccount(req.getParameter("username"))) {
            accountManager.createNewAccount(req.getParameter("username"), req.getParameter("password"));
            req.getRequestDispatcher("/WEB-INF/homepage.jsp").forward(req, resp);
        } else {
            req.setAttribute("error", "username and/or password is already used");
            req.getRequestDispatcher("/WEB-INF/register.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/register.jsp").forward(req, resp);
    }

}
