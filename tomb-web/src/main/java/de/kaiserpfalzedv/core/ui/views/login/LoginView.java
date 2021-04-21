/*
 * Copyright (c) 2021 Kaiserpfalz EDV-Service, Roland T. Lichti.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package de.kaiserpfalzedv.core.ui.views.login;

import ch.carnet.kasparscherrer.LanguageSelect;
import com.sun.istack.NotNull;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import de.kaiserpfalzedv.core.ui.components.TorgButton;
import de.kaiserpfalzedv.core.ui.components.TorgScreen;
import de.kaiserpfalzedv.core.ui.i18n.I18nPageTitle;
import de.kaiserpfalzedv.core.ui.i18n.I18nSelector;
import de.kaiserpfalzedv.core.ui.i18n.TranslatableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.Locale;

import static com.vaadin.flow.component.Unit.PIXELS;

/**
 * LoginView -- The view for log in a user to the system.
 * <p>
 * We use the component {@link LoginForm} for logging in users.
 *
 * @author paulroemer (github.com/vaadin-lerning-center/spring-secured-vaadin)
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 0.1.0  2021-03-26
 */
@Tag("sa-login-view")
@Route(LoginView.ROUTE)
@I18nPageTitle("login.caption")
public class LoginView extends TorgScreen implements BeforeEnterObserver, LocaleChangeObserver, TranslatableComponent {
    public static final String ROUTE = "login";
    private static final Logger LOG = LoggerFactory.getLogger(LoginView.class);
    private H1 title;
    private Div description;

    private LanguageSelect languageSelect;
    private LoginForm login;
    private TorgButton register;

    private Locale locale;

    @PostConstruct
    public void init() {
        LOG.debug("Creating login view.");

        setSizeFull();

        VerticalLayout center = generateLoginForm();

        Image logo = new Image("/images/logo.png", "Delphi Council");
        logo.setClassName("centered");
        logo.setMaxWidth(200, PIXELS);
        logo.setMaxHeight(200, PIXELS);

        addInLeftBorder(logo);

        add(center);
    }

    private VerticalLayout generateLoginForm() {
        VerticalLayout result = new VerticalLayout();
        result.setHeightFull();

        /**
         * The login component.
         */
        title = new H1(getTranslation("login.caption"));
        description = new Div();
        description.setText(getTranslation("login.help"));

        languageSelect = new I18nSelector("input.locale", VaadinSession.getCurrent().getLocale());
        languageSelect.setRequiredIndicatorVisible(true);
        languageSelect.setValue(VaadinSession.getCurrent().getLocale());

        login = new LoginForm();
        login.setAction("login");
        login.addForgotPasswordListener(event -> Notification.show("sorry, not implemented yet", 2000, Notification.Position.BOTTOM_STRETCH));

        register = new TorgButton(
                "login.register-link",
                RegistrationView.class
        );

        result.add(title, description, languageSelect, login, register);

        return result;
    }


    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        login.setError(event.getLocation().getQueryParameters().getParameters().containsKey("error"));

        LOG.trace("Entering login view. ui={}, location={}, error={}",
                event.getUI().getId(), event.getLocation(),
                event.getLocation().getQueryParameters().getParameters().containsKey("error"));
    }

    @Override
    public void localeChange(@NotNull final LocaleChangeEvent event) {
        setLocale(event.getLocale());
    }

    @Override
    public void setLocale(@NotNull final Locale locale) {
        if (this.locale != null && this.locale.equals(locale)) {
            LOG.debug("Language not changed - ignoring event. locale={}", locale);
            return;
        }

        LOG.trace("Changing locale. locale={}", locale);

        this.locale = locale;

        translate();
    }

    @Override
    public void translate() {
        LOG.trace("Translating. locale={}, vaadin={}", locale, VaadinSession.getCurrent().getLocale());

        title.setText(getTranslation("login.caption"));
        description.setText(getTranslation("login.help"));

        languageSelect.setLabel(getTranslation("input.locale.caption"));
        languageSelect.setHelperText(getTranslation("input.locale.help"));

        register.translate();
    }
}
