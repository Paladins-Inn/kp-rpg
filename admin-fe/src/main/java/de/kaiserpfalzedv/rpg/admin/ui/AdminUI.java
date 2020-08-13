package de.kaiserpfalzedv.rpg.admin.ui;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;
import javax.servlet.annotation.WebInitParam;

import de.kaiserpfalzedv.rpg.admin.ui.view.LoginView;
import de.kaiserpfalzedv.rpg.admin.ui.view.MainView;

@Theme("dashboard")
public class AdminUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        Responsive.makeResponsive(this);
        addStyleName(ValoTheme.UI_WITH_MENU);
        Page.getCurrent().addBrowserWindowResizeListener(
                (Page.BrowserWindowResizeListener) event -> System.out.println("BrowserWindowResized")
        );
        updateContent();
    }

    public void updateContent() {
        Boolean isAuthenticated = (Boolean) VaadinSession.getCurrent().getAttribute("isAuthenticated");
        if (isAuthenticated != null && isAuthenticated) {
            setContent(new MainView());
            removeStyleName("loginview");
            getNavigator().navigateTo(getNavigator().getState());
        } else {
            setContent(new LoginView());
            addStyleName("loginview");
        }
    }

    /**
     * VaadinServlet configuration
     */
    @WebServlet(urlPatterns = "/*", name = "AdminUIServlet", asyncSupported = true, initParams = {
        @WebInitParam(name = "org.atmosphere.websocket.suppressJSR356", value = "true")}
    )
    @VaadinServletConfiguration(ui = AdminUI.class, productionMode = false)
    public static class AdminUIServlet extends VaadinServlet {
    }
}
