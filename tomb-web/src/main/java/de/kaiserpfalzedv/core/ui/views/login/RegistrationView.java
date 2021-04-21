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

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import de.kaiserpfalzedv.core.ui.components.TorgActionBar;
import de.kaiserpfalzedv.core.ui.components.TorgNotification;
import de.kaiserpfalzedv.core.ui.components.TorgScreen;
import de.kaiserpfalzedv.core.ui.i18n.I18nPageTitle;
import de.kaiserpfalzedv.core.ui.i18n.I18nSelector;
import de.kaiserpfalzedv.core.ui.i18n.TranslatableComponent;
import de.kaiserpfalzedv.core.ui.views.person.ConfirmationTokenEvent;
import de.kaiserpfalzedv.core.ui.views.person.ConfirmationTokenListener;
import de.kaiserpfalzedv.core.ui.views.person.PersonRegistrationEvent;
import de.kaiserpfalzedv.core.ui.views.person.PersonRegistrationListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

import static com.vaadin.flow.component.Unit.PIXELS;

/**
 * RegistrationView --
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 0.1.0  2021-04-10
 */
@Tag("sa-registration-view")
@Route(RegistrationView.ROUTE + "/:token?")
@I18nPageTitle("registration.caption")
@CssImport("./views/edit-view.css")
public class RegistrationView extends TorgScreen implements BeforeEnterObserver, LocaleChangeObserver, TranslatableComponent {
    public static final String ROUTE = "register";
    private static final Logger LOG = LoggerFactory.getLogger(RegistrationView.class);
    @Value("${application.registration.redirect-url}")
    public String redirectPage = "https://www.ritter-der-stuerme.de/user-registered";

    @Value("${application.registration.cancel-url}")
    public String cancelPage = "https://www.ritter-der-stuerme.de/user-registration-canceled";

    @Autowired
    private PersonRegistrationListener registrationListener;

    @Autowired
    private ConfirmationTokenListener confirmationTokenListener;

    private Locale locale;

    private H1 title;
    private Div description;
    private TextField name;
    private TextField firstname;
    private TextField lastname;

    private EmailField email;
    private I18nSelector languageSelect;

    private TextField username;
    private PasswordField password;

    private TorgActionBar actions;

    @PostConstruct
    public void init() {
        addListener(PersonRegistrationEvent.class, registrationListener);
        addListener(ConfirmationTokenEvent.class, confirmationTokenListener);

        locale = VaadinSession.getCurrent().getLocale();
        if (locale == null) {
            locale = Locale.GERMAN;
        }

        setSizeFull();
        HorizontalLayout layout = new HorizontalLayout();
        layout.setSizeFull();

        FormLayout form = new FormLayout();
        form.setHeightFull();
        form.setResponsiveSteps(
                new FormLayout.ResponsiveStep("100px", 1),
                new FormLayout.ResponsiveStep("300px", 2),
                new FormLayout.ResponsiveStep("600px", 4)
        );

        title = new H1(getTranslation("registration.caption"));
        description = new Div();
        description.setText(getTranslation("registration.help"));

        name = new TextField(getTranslation("person.name.caption"));
        name.setClearButtonVisible(true);
        name.setRequired(true);
        name.setRequiredIndicatorVisible(true);
        name.setHelperText(getTranslation("person.name.help"));
        name.setMinLength(1);
        name.setMaxLength(100);

        firstname = new TextField(getTranslation("person.first-name.caption"));
        firstname.setHelperText(getTranslation("person.first-name.help"));
        firstname.setClearButtonVisible(true);
        firstname.setRequired(true);
        firstname.setRequiredIndicatorVisible(true);
        firstname.setMinLength(1);
        firstname.setMaxLength(50);

        lastname = new TextField(getTranslation("person.last-name.caption"));
        lastname.setHelperText(getTranslation("person.last-name.help"));
        lastname.setClearButtonVisible(true);
        lastname.setRequired(true);
        lastname.setRequiredIndicatorVisible(true);
        lastname.setMinLength(1);
        lastname.setMaxLength(50);

        email = new EmailField(getTranslation("person.email.caption"));
        email.setHelperText(getTranslation("person.email.help"));
        email.setClearButtonVisible(true);
        email.setRequiredIndicatorVisible(true);

        username = new TextField(getTranslation("person.username.caption"));
        username.setHelperText(getTranslation("person.username.help"));
        username.setClearButtonVisible(true);
        username.setRequired(true);
        username.setRequiredIndicatorVisible(true);
        username.setMinLength(1);
        username.setMaxLength(50);

        password = new PasswordField(getTranslation("person.password.caption"));
        password.setHelperText(getTranslation("person.password.caption"));
        password.setClearButtonVisible(true);
        password.setRequired(true);
        password.setRequiredIndicatorVisible(true);
        password.setMinLength(8);
        password.setMaxLength(50);

        languageSelect = new I18nSelector("input.locale", locale);
        languageSelect.setRequiredIndicatorVisible(true);
        languageSelect.setValue(VaadinSession.getCurrent().getLocale());

        // save
        // reset
        // cancel
        actions = new TorgActionBar(
                "buttons",
                ev -> { // save
                    getEventBus().fireEvent(new PersonRegistrationEvent(
                            this,
                            name.getValue(),
                            lastname.getValue(), firstname.getValue(),
                            email.getValue(),
                            username.getValue(), password.getValue(),
                            languageSelect.getValue()
                    ));

                    new TorgNotification(
                            "registration.send-registration",
                            null,
                            null,
                            Arrays.asList(name.getValue(), username.getValue(), email.getValue())
                    ).open();

                    ev.getSource().getUI().ifPresent(ui -> ui.navigate(LoginView.ROUTE));
                },
                ev -> { // reset
                    name.setValue(null);
                    lastname.setValue(null);
                    firstname.setValue(null);
                    email.setValue(null);
                    username.setValue(null);
                    password.setValue(null);
                },
                ev -> { // cancel
                    ev.getSource().getUI().ifPresent(ui -> ui.navigate(LoginView.ROUTE));
                },
                null
        );

        form.add(title, description,
                name,
                lastname, firstname,
                email, languageSelect,
                username, password,
                actions);
        form.setColspan(title, 4);
        form.setColspan(description, 4);
        form.setColspan(name, 4);
        form.setColspan(firstname, 2);
        form.setColspan(lastname, 2);
        form.setColspan(email, 3);
        form.setColspan(actions, 4);
        form.setColspan(username, 2);
        form.setColspan(password, 2);

        form.setMinWidth(400, PIXELS);
        form.setMaxWidth(600, PIXELS);

        add(form);
    }


    @Override
    public void translate() {
        if (locale == null) {
            locale = VaadinSession.getCurrent().getLocale();
        }

        LOG.trace("Translating form. locale={}", locale.getDisplayName());

        title.setText(getTranslation("registration.caption"));
        description.setText(getTranslation("registration.help"));

        name.setLabel(getTranslation("person.name.caption"));
        name.setHelperText(getTranslation("person.name.help"));
        firstname.setLabel(getTranslation("person.first-name.caption"));
        firstname.setHelperText(getTranslation("person.first-name.help"));
        lastname.setLabel(getTranslation("person.last-name.caption"));
        lastname.setHelperText(getTranslation("person.last-name.help"));

        email.setLabel(getTranslation("person.email.caption"));
        email.setHelperText(getTranslation("person.email.help"));

        username.setLabel(getTranslation("person.username.caption"));
        username.setHelperText(getTranslation("person.username.help"));
        password.setLabel(getTranslation("person.password.caption"));
        password.setHelperText(getTranslation("person.password.caption"));

        languageSelect.setLabel(getTranslation("input.locale.caption"));

        actions.setLocale(locale);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<String> tokenString = event.getRouteParameters().get("token");
        LOG.trace("confirming new user. token={}", tokenString);

        tokenString.ifPresent(token -> {
            try {
                LOG.info("Confirming token. token={}", token);

                ConfirmationTokenEvent cte = new ConfirmationTokenEvent(this, UUID.fromString(token));
                fireEvent(cte);

                getUI().ifPresent(ui -> ui.navigate(LoginView.ROUTE));
            } catch (IllegalArgumentException e) {
                new TorgNotification(
                        "registration.invalid-token.wrong-format",
                        null,
                        null,
                        null
                ).open();

            }

            event.getUI().navigate(LoginView.ROUTE);
        });
    }


    @Override
    public void localeChange(LocaleChangeEvent event) {
        setLocale(event.getLocale());
    }


    @Override
    public void setLocale(Locale locale) {
        if (this.locale != null && this.locale.equals(locale)) {
            LOG.debug("Locale not changed. current={}, new={}", this.locale.getDisplayName(), locale.getDisplayName());
            return;
        }

        this.locale = locale;

        translate();
    }
}