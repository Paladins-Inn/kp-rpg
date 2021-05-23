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

package de.kaiserpfalzedv.core.ui;

import jakarta.validation.constraints.NotNull;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import de.codecamp.vaadin.serviceref.ServiceRef;
import de.kaiserpfalzedv.core.security.LoggedInUser;
import de.kaiserpfalzedv.core.ui.i18n.TranslatableComponent;
import de.kaiserpfalzedv.core.ui.i18n.Translator;
import de.kaiserpfalzedv.core.ui.views.about.AboutView;
import de.kaiserpfalzedv.core.ui.views.missions.MissionListView;
import de.kaiserpfalzedv.core.ui.views.operative.OperativesListView;
import de.kaiserpfalzedv.core.ui.views.person.PersonEditView;
import de.kaiserpfalzedv.core.ui.views.person.PersonListView;
import de.kaiserpfalzedv.core.ui.views.specialmissions.SpecialMissionEditorView;
import de.kaiserpfalzedv.core.ui.views.specialmissions.SpecialMissionListView;
import de.kaiserpfalzedv.core.ui.views.tools.ThreatCardView;
import de.paladinsinn.tp.dcis.data.person.Person;
import de.paladinsinn.tp.dcis.data.person.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.PostConstruct;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;

import static com.vaadin.flow.component.Unit.PIXELS;

/**
 * MainScreen -- The main view is a top-level placeholder for other views.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 0.1.0  2021-03-27
 */
@PWA(name = "Delphi Council Information System", shortName = "DC IS", enableInstallPrompt = false)
@JsModule("./styles/shared-styles.js")
@Theme(value = Lumo.class, variant = Lumo.DARK)
@CssImport("./views/delphi-council-is.css")
public class MainView extends AppLayout implements LocaleChangeObserver, TranslatableComponent {
    private static final Logger LOG = LoggerFactory.getLogger(MainView.class);

    @Autowired
    private ServiceRef<Translator> translator;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private LoggedInUser user;

    private Tabs menu;
    private H1 titleBar;

    private Locale locale;

    private static Tab createTab(String text, Class<? extends Component> navigationTarget) {
        final Tab tab = new Tab();
        tab.add(new RouterLink(text, navigationTarget));
        ComponentUtil.setData(tab, Class.class, navigationTarget);
        return tab;
    }

    @PostConstruct
    public void init() {
        setLastLoggedIn();

        menu = createMenu();

        setPrimarySection(Section.DRAWER);
        addToNavbar(true, createHeaderContent());
        addToDrawer(createDrawerContent(menu));
    }

    private void setLastLoggedIn() {
        if (user != null) {
            LOG.debug("Setting last login date to user. user='{}'", user.getPerson().getUsername());

            user.getPerson().getStatus().setLastLogin(OffsetDateTime.now(ZoneOffset.UTC));
            user.setPerson(personRepository.save(user.getPerson()));

            locale = user.getPerson().getLocale();
            LOG.debug("Setting locale to user locale. locale={}", locale);
        } else {
            LOG.info("No user logged in.");
            locale = VaadinSession.getCurrent().getLocale();
        }
    }

    private Component createHeaderContent() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setId("header");
        layout.getThemeList().set("dark", true);
        layout.addClassName("torg-header");
        layout.setWidthFull();
        layout.setSpacing(false);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.add(new DrawerToggle());
        titleBar = new H1();
        layout.add(titleBar);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Avatar avatar = createAvatar(authentication);
        layout.add(avatar);

        return layout;
    }

    private Avatar createAvatar(Authentication authentication) {
        Avatar result = new Avatar(authentication.getName());

        Person data = personRepository.findByUsername(authentication.getName());
        result.setImage(data.getGravatar());

        ContextMenu menu = new ContextMenu();
        menu.setTarget(result);

        menu.addItem(getTranslation("avatar.menu.account-settings"),
                e -> {
                    LOG.info("User opens settings menu. event={}", e);

                    e.getSource().getUI().ifPresent(ui -> ui.navigate(
                            PersonEditView.class,
                            new RouteParameters("id", user.getPerson().getId().toString())
                    ));
                }
        );

        menu.addItem(getTranslation("buttons.logout.caption"),
                e -> {
                    LOG.info("User wanted to log out. event={}", e);
                    e.getSource().getUI().ifPresent(ui -> ui.getPage().open("/logout", "_self"));
                }
        );


        return result;
    }

    private Component createDrawerContent(@NotNull final Tabs menu) {
        VerticalLayout layout = new VerticalLayout();
        layout.addClassName("torg-thunder");
        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(false);
        layout.getThemeList().set("spacing-s", true);
        layout.setAlignItems(FlexComponent.Alignment.STRETCH);
        HorizontalLayout logoLayout = new HorizontalLayout();
        logoLayout.setId("logo");
        logoLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        Image logoImage = new Image("/images/logo.png", "Delphi Council");
        logoImage.setMaxWidth(50, PIXELS);
        logoImage.setMaxHeight(50, PIXELS);
        logoLayout.add(logoImage);

        VerticalLayout logo = new VerticalLayout();
        logoLayout.add(logo);

        H1 systemName = new H1(translator.get().getTranslation("application.title", getLocale()));
        Div claim = new Div();
        claim.setText(translator.get().getTranslation("application.description", getLocale()));
        logo.add(systemName, claim);

        layout.add(logoLayout, menu);
        return layout;
    }

    private Tabs createMenu() {
        final Tabs tabs = new Tabs();
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        tabs.addThemeVariants(TabsVariant.LUMO_MINIMAL);
        tabs.setId("tabs");
        tabs.add(createMenuItems());
        return tabs;
    }

    private Component[] createMenuItems() {
        ArrayList<Component> result = new ArrayList<>();

        ArrayList<Class<? extends Component>> allTabs = new ArrayList<>();
        allTabs.add(MissionListView.class);
        allTabs.add(SpecialMissionListView.class);
        allTabs.add(SpecialMissionEditorView.class);
        allTabs.add(OperativesListView.class);
        allTabs.add(PersonListView.class);
        allTabs.add(ThreatCardView.class);
        allTabs.add(AboutView.class);

        Set<String> roles = readRoleFromAuthentication();

        for (Class<? extends Component> t : allTabs) {
            Secured secured = t.getAnnotation(Secured.class);

            if (secured == null || Arrays.stream(secured.value()).anyMatch(roles::contains)) {
                LOG.trace(
                        "View is either free or role matches. view={}, roles={}, rolesAllowed={}",
                        t.getSimpleName(),
                        roles,
                        secured != null ? Arrays.asList(secured.value()) : "-not-set-"
                );


                Tab tab = createTab(getTranslation(t.getSimpleName()), t);
                result.add(tab);
            } else {
                LOG.debug("View is not allowed for user. view={}, roles={}, rolesAllowed={}",
                        t.getSimpleName(),
                        roles,
                        Arrays.asList(secured.value())
                );
            }
        }

        return result.toArray(new Component[]{});
    }

    private Set<String> readRoleFromAuthentication() {
        HashSet<String> result = new HashSet<>(user.getPerson().getAuthorities().size());

        user.getPerson().getAuthorities().forEach(r -> result.add(r.getAuthority()));

        return result;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        getTabForComponent(getContent()).ifPresent(menu::setSelectedTab);
        titleBar.setText(menu.getSelectedTab().getLabel());
//        titleBar.setText(getCurrentPageTitle());
    }

    private Optional<Tab> getTabForComponent(Component component) {
        return menu.getChildren().filter(tab -> ComponentUtil.getData(tab, Class.class).equals(component.getClass()))
                .findFirst().map(Tab.class::cast);
    }

    @Override
    public void localeChange(@NotNull final LocaleChangeEvent event) {
        LOG.trace("Language change event. locale={}", event.getLocale());

        Locale locale = event.getLocale();

        setLocale(locale);
    }

    public void setLocale(@NotNull final Locale locale) {
        if (this.locale != null && this.locale.equals(locale)) {
            LOG.trace("Language did not change - ignoring event. locale={}", this.locale);
            return;
        }

        this.locale = locale;

        translate();
    }

    public void translate() {

    }
}
