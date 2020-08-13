package de.kaiserpfalzedv.rpg;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.*;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;
import de.kaiserpfalzedv.rpg.view.LoginView;
import de.kaiserpfalzedv.rpg.view.MainView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

/**
 * @author rlichti {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 2020-08-03
 */
@Theme("dashboard")
public class WebUI extends UI {
    private static final Logger LOG = LoggerFactory.getLogger(WebUI.class);

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        Responsive.makeResponsive(this);
        addStyleName(ValoTheme.UI_WITH_MENU);
        Page.getCurrent().addBrowserWindowResizeListener((Page.BrowserWindowResizeListener) event -> System.out.println("BrowserWindowResized"));
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

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true, initParams = {
            @WebInitParam(name = "org.atmosphere.websocket.suppressJSR356", value = "true")}
    )
    @VaadinServletConfiguration(ui = WebUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
