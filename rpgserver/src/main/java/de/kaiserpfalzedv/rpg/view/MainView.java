package de.kaiserpfalzedv.rpg.view;

import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import de.kaiserpfalzedv.rpg.MyMenu;
import de.kaiserpfalzedv.rpg.MyNavigator;

/**
 * @author rlichti {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 2020-08-03
 */
public class MainView extends HorizontalLayout {
    public MainView() {
        setSizeFull();
        addStyleName("mainview");
        setSpacing(false);

        addComponent(new MyMenu());

        ComponentContainer content = new CssLayout();
        content.addStyleName("view-content");
        content.setSizeFull();
        addComponent(content);
        setExpandRatio(content, 1.0f);

        new MyNavigator(content);
    }
}
