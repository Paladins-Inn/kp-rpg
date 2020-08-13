package de.kaiserpfalzedv.rpg.admin.ui.view;

import de.kaiserpfalzedv.vaadin.BeanNavigatorViewMenu;
import de.kaiserpfalzedv.vaadin.BeanNavigator;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;

@SuppressWarnings("serial")
public class MainView extends HorizontalLayout {

    public MainView() {
        setSizeFull();
        addStyleName("mainview");
        setSpacing(false);

        addComponent(new BeanNavigatorViewMenu());

        ComponentContainer content = new CssLayout();
        content.addStyleName("view-content");
        content.setSizeFull();
        addComponent(content);
        setExpandRatio(content, 1.0f);

       new BeanNavigator(content);
    }
}
