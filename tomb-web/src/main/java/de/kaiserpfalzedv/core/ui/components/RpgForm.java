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

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.server.VaadinSession;
import de.kaiserpfalzedv.core.security.LoggedInUser;
import de.kaiserpfalzedv.core.ui.i18n.TranslatableComponent;
import de.kaiserpfalzedv.rpg.core.resources.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Locale;
import java.util.Optional;

/**
 * RpgForm --
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 0.1.0  2021-04-11
 */
@Slf4j
public abstract class RpgForm<T extends Resource> extends Composite<Div> implements LocaleChangeObserver, TranslatableComponent, Serializable, AutoCloseable {
    protected final LoggedInUser user;
    protected final FormLayout form = new FormLayout();
    protected T data;
    protected T oldData;
    protected Locale locale;

    protected boolean initialized = false;

    protected TorgActionBar actions;

    public RpgForm(final LoggedInUser user) {
        this.user = user;
    }

    protected void init() {
        form.setResponsiveSteps(
                new FormLayout.ResponsiveStep("1px", 1),
                new FormLayout.ResponsiveStep("400px", 2),
                new FormLayout.ResponsiveStep("800px", 3)
        );

        if (locale == null) {
            locale = VaadinSession.getCurrent().getLocale();
        }
    }

    protected abstract void populate();

    protected abstract void scrape();

    public void resetData() {
        setData(oldData);
    }

    public Optional<T> getData() {
        return Optional.ofNullable(data);
    }

    public void setData(T data) {
        log.debug("Set data. old={}, new={}", this.data, data);

        this.oldData = this.data;
        this.data = data;

        init();
        populate();
        translate();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    protected void bindData(final Object data, HasValue component) {
        if (data != null) {
            component.setValue(data);
        }
    }

    @Override
    public void fireEvent(ComponentEvent<?> event) {
        log.trace("Fire event. event={}", event);

        getEventBus().fireEvent(event);
    }

    @Override
    public void localeChange(LocaleChangeEvent event) {
        log.trace("Change locale event. locale={}", event.getLocale());

        setLocale(event.getLocale());
    }

    @Override
    public void setLocale(Locale locale) {
        if (this.locale != null && this.locale.equals(locale)) {
            log.debug("Locale has not changed. Ignoring event. locale={}", this.locale);
            return;
        }

        this.locale = locale;

        translate();
    }

    protected String getTranslation(final String key) {
        try {
            return super.getTranslation(key);
        } catch (NullPointerException e) {
            log.warn("Can't call translator from vaadin: {}", e.getMessage());
            return "!" + key;
        }
    }

    @Override
    public void close() throws Exception {
        log.debug("Closing form.");

        getContent().removeAll();
        form.removeAll();
    }
}
