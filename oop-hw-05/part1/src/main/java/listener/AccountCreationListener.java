package listener;

import model.AccountManager;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class AccountCreationListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        sce.getServletContext().setAttribute(AccountManager.ATTRIBUTE_NAME, new AccountManager());
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }
}
