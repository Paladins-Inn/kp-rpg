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

package de.kaiserpfalzedv.core.ui.components;

import jakarta.validation.constraints.NotNull;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.server.VaadinSession;
import de.kaiserpfalzedv.core.ui.i18n.TranslatableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.Locale;

/**
 * TabbedMenu --
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 0.1.0  2021-03-28
 */
@Sfl4j
public class TabbedMenu extends Component implements TranslatableComponent, LocaleChangeObserver {
    private Locale locale;

    private VerticalLayout content;
    private VerticalLayout header;
    private Tabs menu;
    private VerticalLayout footer;

    @PostConstruct
    public void init() {
        locale = VaadinSession.getCurrent().getLocale();

        content = new VerticalLayout();
        content.setSizeFull();
        content.setPadding(false);
        content.setSpacing(false);
        content.getThemeList().set("spacing-s", true);
        content.setAlignItems(FlexComponent.Alignment.STRETCH);

        header = new VerticalLayout();
        menu = new Tabs();
        footer = new VerticalLayout();

        content.add(header, menu, footer);

    }

    public void setHeader(@NotNull final Component component) {
        header.removeAll();
        header.add(component);
    }

    public void addHeader(@NotNull final Component component) {
        header.add(component);
    }

    public void removeHeader(@NotNull final Component component) {
        header.remove(component);
    }


    public void setFooter(@NotNull final Component component) {
        footer.removeAll();
        footer.add(component);
    }

    public void addFooter(@NotNull final Component component) {
        footer.add(component);
    }

    public void removeFooter(@NotNull final Component component) {
        footer.remove(component);
    }


    @Override
    public void localeChange(LocaleChangeEvent event) {
        log.debug(
                "Locale change event. component={}, event={}, locale={}",
                getClass().getSimpleName(),
                event,
                event.getLocale()
        );

        setLocale(event.getLocale());


    }

    @Override
    public void translate() {

    }

    @Override
    public void setLocale(@NotNull final Locale locale) {
        log.trace("Changing locale. old={}, new={}", this.locale, locale);

        this.locale = locale;
    }
}
