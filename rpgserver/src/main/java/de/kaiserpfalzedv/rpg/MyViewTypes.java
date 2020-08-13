package de.kaiserpfalzedv.rpg;

import com.vaadin.navigator.View;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import de.kaiserpfalzedv.rpg.view.DashBoardView;
import de.kaiserpfalzedv.rpg.view.Report1View;
import de.kaiserpfalzedv.rpg.view.Report2View;

/**
 * @author rlichti {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 2020-08-03
 */
public enum MyViewTypes {

    DASHBOARD("dashboard", DashBoardView.class, FontAwesome.HOME, false),
    REPORT1("report-1", Report1View.class, FontAwesome.TABLE, true),
    REPORT2("report-2", Report2View.class, FontAwesome.TABLE, true);

    private final String viewName;
    private final Class<? extends View> viewClass;
    private final Resource icon;
    private final boolean stateful;

    private MyViewTypes(
            final String viewName,
            final Class<? extends View> viewClass,
            final Resource icon,
            final boolean stateful) {

        this.viewName = viewName;
        this.viewClass = viewClass;
        this.icon = icon;
        this.stateful = stateful;
    }

    public boolean isStateful() {
        return stateful;
    }

    public String getViewName() {
        return viewName;
    }

    public Class<? extends View> getViewClass() {
        return viewClass;
    }

    public Resource getIcon() {
        return icon;
    }

    public static MyViewTypes getByViewName(final String viewName) {
        MyViewTypes result = null;
        for (MyViewTypes viewType : values()) {
            if (viewType.getViewName().equals(viewName)) {
                result = viewType;
                break;
            }
        }
        return result;
    }
}
