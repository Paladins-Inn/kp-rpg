package de.kaiserpfalzedv.vaadin;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

import javax.enterprise.inject.spi.CDI;
import java.util.Iterator;

/**
 * A responsive menu component providing user information and the controls for
 * primary navigation between the views.
 */
@SuppressWarnings({"serial", "unchecked"})
public final class BeanNavigatorViewMenu extends CustomComponent {

    public static final String ID = "dashboard-menu";

    public BeanNavigatorViewMenu() {
        setPrimaryStyleName("valo-menu");
        setId(ID);
        setSizeUndefined();
        setCompositionRoot(buildContent());
    }

    private Component buildContent() {
        final CssLayout menuContent = new CssLayout();
        menuContent.addStyleName("sidebar");
        menuContent.addStyleName(ValoTheme.MENU_PART);
        menuContent.addStyleName("no-vertical-drag-hints");
        menuContent.addStyleName("no-horizontal-drag-hints");
        menuContent.setWidth(null);
        menuContent.setHeight("100%");
        menuContent.addComponent(buildTitle());
        menuContent.addComponent(buildMenuItems());
        return menuContent;
    }

    private Component buildTitle() {
        Label logo = new Label("The Quarkus-Vaadin Example", ContentMode.HTML);
        logo.setSizeUndefined();
        HorizontalLayout logoWrapper = new HorizontalLayout(logo);
        logoWrapper.setComponentAlignment(logo, Alignment.MIDDLE_CENTER);
        logoWrapper.addStyleName("valo-menu-title");
        logoWrapper.setSpacing(false);
        return logoWrapper;
    }

    private Component buildMenuItems() {
        CssLayout menuItemsLayout = new CssLayout();
        menuItemsLayout.addStyleName("valo-menuitems");

        Iterator<View> views  = CDI.current().select(View.class).iterator();
        while (views.hasNext()) {
            View view = views.next();

            BeanNavigatorView beanNavigatorView = view.getClass().getAnnotation(BeanNavigatorView.class);
            if (beanNavigatorView != null) {
                Component menuItemComponent = new ValoMenuItemButton(view, beanNavigatorView);
                menuItemsLayout.addComponent(menuItemComponent, beanNavigatorView.weight());
            }
        }

        return menuItemsLayout;
    }

    @Override
    public void attach() {
        super.attach();
    }

    public final class ValoMenuItemButton extends Button {

        public ValoMenuItemButton(final View view, final BeanNavigatorView annotation) {
            setPrimaryStyleName("valo-menu-item");

            setIcon(VaadinIcons.valueOf(annotation.icon()));
            setCaption(annotation.name().substring(0, 1).toUpperCase()
                    + annotation.name().substring(1));
            addClickListener((ClickListener) event -> UI.getCurrent().getNavigator().navigateTo(annotation.name()));
        }
    }
}
