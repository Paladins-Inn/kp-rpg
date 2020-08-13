package de.kaiserpfalzedv.vaadin;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.navigator.ViewProvider;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.spi.CDI;
import javax.inject.Singleton;

@SuppressWarnings("serial")
public class BeanNavigator extends Navigator {
    private static final Logger LOGGER = LoggerFactory.getLogger(BeanNavigator.class);

    public BeanNavigator(final ComponentContainer container) {
        super(UI.getCurrent(), container);
        initViewChangeListener();
        initViewProviders();
    }

    private void initViewChangeListener() {
        addViewChangeListener(new ViewChangeListener() {

            @Override
            public boolean beforeViewChange(final ViewChangeEvent event) {
                // Since there's no conditions in switching between the views
                // we can always return true.
                return true;
            }

            @Override
            public void afterViewChange(final ViewChangeEvent event) {
                LOGGER.debug("Switching view: from={}, to={}",
                        event.getOldView().getViewComponent().getId(),
                        event.getNewView().getViewComponent().getId());
            }
        });
    }


    private void initViewProviders() {
        LOGGER.info("Adding views to provider.");


        for (View view : CDI.current().select(View.class)) {
            BeanNavigatorView beanNavigatorView = view.getClass().getAnnotation(BeanNavigatorView.class);
            LOGGER.trace("Checking view: {}", beanNavigatorView.name());

            if (beanNavigatorView != null) {
                LOGGER.debug("Adding view provider: view='{}', class={}", beanNavigatorView.name(), view.getClass());

                addProvider(new BeanNavigatorViewProvider(beanNavigatorView.name(), view.getClass()));
            }

            BeanNavigatorErrorView errorView = view.getClass().getAnnotation(BeanNavigatorErrorView.class);
            if (beanNavigatorView != null) {
                LOGGER.debug("Adding error view provider: view='{}', class={}", errorView.annotationType().getName(), view.getClass());

                setErrorProvider(new ViewProvider() {
                    @Override
                    public String getViewName(final String navigationState) {
                        BeanNavigatorView a = errorView.getClass().getAnnotation(BeanNavigatorView.class);
                        if (a != null) {
                            if (a.name().equals(navigationState) || a.name().startsWith(navigationState + "/"))
                                return a.name();
                        }
                        return a.name();
                    }

                    @Override
                    public View getView(final String viewName) {
                        return view;
                    }
                });
            }
        }
    }
}
