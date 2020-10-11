package listener;

import model.DataManager;
import model.ShoppingCart;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener
public class DataManagerListener implements ServletContextListener, HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent event) {
        event.getSession().setAttribute("Shopping Cart", new ShoppingCart());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        context.setAttribute("Data Manager", new DataManager());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }

}