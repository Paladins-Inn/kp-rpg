package de.kaiserpfalzedv.vaadin;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.inject.spi.CDI;

/**
 * This {@link ViewProvider} scans all CDI beans of type {@link View} for the annotation {@link BeanNavigatorView} and uses
 *
 * @author rlichti {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 2020-08-09
 */
public class BeanNavigatorViewProvider implements ViewProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(BeanNavigatorViewProvider.class);

    private String name;
    private Class<? extends View> clasz;

    public BeanNavigatorViewProvider(
            final String name,
            final Class<? extends View> clasz
    ) {
        this.name = name;
        this.clasz = clasz;

        LOGGER.trace("Created BeanNavigatorViewProvider: name='{}', class={}", name, clasz.getName());
    }

    @Override
    public String getViewName(String navigationState) {
        if (name.equals(navigationState) || name.startsWith(navigationState + "/")) {
            LOGGER.trace("Found View: name='{}'", name);

            return name;
        }

        return null;
    }

    @Override
    public View getView(String viewName) {
        if (name.equals(viewName)) {
            LOGGER.trace("Returning view for: name='{}'", name);

            return CDI.current().select(clasz).get();
        }

        return null;
    }
}
