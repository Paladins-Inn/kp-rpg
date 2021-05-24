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
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.html.NativeButton;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import de.kaiserpfalzedv.core.ui.i18n.TranslatableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * TorgActionBar --
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 0.1.0  2021-04-07
 */
@Slf4j
public class TorgActionBar extends HorizontalLayout implements LocaleChangeObserver, TranslatableComponent {
    private final String i18nKey;
    private final Object[] i18nParameters;
    private final ComponentEventListener<ClickEvent<NativeButton>> save;
    private final ComponentEventListener<ClickEvent<NativeButton>> reset;
    private final ComponentEventListener<ClickEvent<NativeButton>> cancel;
    private final ComponentEventListener<ClickEvent<NativeButton>> delete;
    private final Map<ComponentEventListener<ClickEvent<NativeButton>>, String> additionalActions;
    private Locale locale;

    public TorgActionBar(
            @NotNull final String i18nKey,
            final ComponentEventListener<ClickEvent<NativeButton>> save,
            final ComponentEventListener<ClickEvent<NativeButton>> reset,
            final ComponentEventListener<ClickEvent<NativeButton>> cancel,
            final ComponentEventListener<ClickEvent<NativeButton>> delete,
            final Object... i18nParameters
    ) {
        this.i18nKey = i18nKey;
        this.i18nParameters = i18nParameters;

        this.save = save;
        this.reset = reset;
        this.delete = delete;

        if (cancel != null) {
            this.cancel = cancel;
        } else {
            log.debug("Adding back button as cancel.");

            this.cancel = event -> event.getSource()
                    .getUI().ifPresent(ui -> ui.getPage().getHistory().back());
        }

        this.additionalActions = new HashMap<>();

        if (isEnabled() && isVisible()) {
            addButton(String.format("%s.%s", this.i18nKey, "save"), this.save);
            addButton(String.format("%s.%s", this.i18nKey, "reset"), this.reset);
            addButton(String.format("%s.%s", this.i18nKey, "cancel"), this.cancel);
            addButton(String.format("%s.%s", this.i18nKey, "delete"), this.delete);
        }
    }

    public void addAction(
            @NotNull final String i18nKey,
            @NotNull final ComponentEventListener<ClickEvent<NativeButton>> action
    ) {
        additionalActions.put(action, i18nKey);

        if (isEnabled() && isVisible()) {
            addButton(i18nKey, action);
        }
    }

    /**
     * Returns whether this {@code HasValue} is in read-only mode or not.
     *
     * @return {@code false} if the user can modify the value, {@code true} if
     * not.
     */
    public boolean isReadOnly() {
        return super.isEnabled();
    }

    /**
     * Sets the read-only mode of this {@code HasValue} to given mode. The user
     * can't change the value when in read-only mode.
     * <p>
     * A {@code HasValue} with a visual component in read-only mode typically
     * looks visually different to signal to the user that the value cannot be
     * edited.
     *
     * @param readOnly a boolean value specifying whether the component is put
     *                 read-only mode or not
     */
    public void setReadOnly(boolean readOnly) {
        super.setEnabled(!readOnly);
    }

    @Override
    public void translate() {
        removeAll();

        if (isEnabled() && isVisible()) {
            addButton(String.format("%s.%s", this.i18nKey, "save"), save);
            addButton(String.format("%s.%s", this.i18nKey, "reset"), reset);
            addButton(String.format("%s.%s", this.i18nKey, "cancel"), cancel);
            addButton(String.format("%s.%s", this.i18nKey, "delete"), delete);

            additionalActions.forEach((key, value) -> addButton(value, key));
        }
    }

    @Override
    public void removeAll() {
        getChildren().forEach(b -> {
            if (b instanceof AutoCloseable) {
                try {
                    ((AutoCloseable) b).close();
                } catch (Exception e) {
                    log.error("Problem while closing. button=" + b, e);
                }
            }
        });

        super.removeAll();
    }

    private void addButton(
            @NotNull final String i18nKey,
            @NotNull final ComponentEventListener<ClickEvent<NativeButton>> action
    ) {
        if (action != null) {
            log.trace("Add button. i18nKey={}, i18nParameters={}", i18nKey, i18nParameters);

            add(new TorgButton(
                    i18nKey,
                    action,
                    i18nParameters
            ));
        }
    }

    @Override
    public void localeChange(@NotNull final LocaleChangeEvent event) {
        setLocale(event.getLocale());
    }

    @Override
    public void setLocale(@NotNull final Locale locale) {
        if (locale != null && !locale.equals(this.locale)) {
            this.locale = locale;

            translate();
        } else {
            log.debug("Locale didn't change - ignoring change. old={}, new={}", this.locale, locale);
        }
    }
}
