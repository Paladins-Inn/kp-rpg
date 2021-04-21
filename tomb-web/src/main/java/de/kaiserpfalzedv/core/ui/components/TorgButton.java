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

import com.sun.istack.NotNull;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.NativeButton;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.router.RouteParameters;
import de.kaiserpfalzedv.core.ui.i18n.TranslatableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.UUID;

/**
 * TorgButton -- A l10n button with torg background.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 0.1.0  2021-04-06
 */
@CssImport("./views/delphi-council-is.css")
public class TorgButton extends NativeButton implements TranslatableComponent, LocaleChangeObserver {
    private static final Logger LOG = LoggerFactory.getLogger(TorgButton.class);

    private final String i18nBase;
    private final Object[] i18nParameters;

    public TorgButton(@NotNull final String i18nBase, @NotNull Class<? extends Component> target) {
        this(i18nBase, target, (RouteParameters) null);
    }

    public TorgButton(
            @NotNull final String i18nBase,
            @NotNull Class<? extends Component> target,
            @NotNull final UUID id,
            final Object... i18nParameters
    ) {
        this(i18nBase, target, new RouteParameters("id", id.toString()), i18nParameters);
    }

    public TorgButton(
            @NotNull final String i18nBase,
            @NotNull Class<? extends Component> target,
            @NotNull final RouteParameters routeParameters,
            final Object... i18nParameters
    ) {
        this(
                i18nBase,
                e -> e.getSource().getUI().ifPresent(ui -> {
                    if (routeParameters != null) {
                        LOG.debug("TorgButton pressed. target={}, parameters={}", target, routeParameters);
                        ui.navigate(target, routeParameters);
                    } else {
                        LOG.debug("TorgButton pressed. target={}", target);
                        ui.navigate(target);
                    }
                }),
                i18nParameters);
    }

    public TorgButton(
            @NotNull final String i18nBase,
            @NotNull final ComponentEventListener<ClickEvent<NativeButton>> listener,
            final Object... i18nParameters
    ) {
        this.i18nBase = i18nBase.endsWith(".caption") ? i18nBase.substring(0, i18nBase.length() - 8) : i18nBase;
        addClassName("v-nativebutton-torg");

        this.i18nParameters = i18nParameters;

        addClickListener(listener);

        translate();
    }

    @Override
    public void translate() {
        if (i18nParameters != null) {
            setText(getTranslation(i18nBase + ".caption", i18nParameters));
            setTitle(getTranslation(i18nBase + ".help", i18nParameters));
        } else {
            setText(getTranslation(i18nBase + ".caption"));
            setTitle(getTranslation(i18nBase + ".help"));
        }
    }

    @Override
    public void localeChange(@NotNull final LocaleChangeEvent event) {
        setLocale(event.getLocale());
    }

    @Override
    public void setLocale(@NotNull final Locale locale) {
        translate();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TorgButton)) return false;
        TorgButton that = (TorgButton) o;
        return i18nBase.equals(that.i18nBase);
    }

    @Override
    public int hashCode() {
        return Objects.hash(i18nBase);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", TorgButton.class.getSimpleName() + "[", "]")
                .add("i18n='" + i18nBase + "'")
                .toString();
    }
}
