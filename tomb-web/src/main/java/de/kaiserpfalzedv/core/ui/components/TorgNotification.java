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
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.html.NativeButton;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * TorgNotification --
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 0.1.0  2021-04-04
 */
@Getter
public class TorgNotification extends Notification {
    private final static Logger LOG = LoggerFactory.getLogger(TorgNotification.class);

    /**
     * The duration of the message in millis.
     */
    private final static int DEFAULT_DURATION = 3000;

    private final String i18nKey;
    private final ComponentEventListener<ClickEvent<NativeButton>> event;
    private final List<String> i18nParameters = new ArrayList<>();
    private final List<String> parameters = new ArrayList<>();

    /**
     * Creates a new event using the given source and indicator whether the
     * event originated from the client side or the server side.
     *
     * @param i18nKey        the i18n key for the notification message
     * @param event          the button event when needed. Ff null, there won't be a button
     * @param i18nParameters the parameters that get translated
     * @param parameters     the parameters that does not need to be translated
     */
    public TorgNotification(
            @NotNull final String i18nKey,
            final ComponentEventListener<ClickEvent<NativeButton>> event,
            @NotNull final List<String> i18nParameters,
            @NotNull final List<String> parameters
    ) {
        this.i18nKey = i18nKey;
        this.event = event;

        if (i18nParameters != null) {
            this.i18nParameters.addAll(i18nParameters);
        }

        if (parameters != null) {
            this.parameters.addAll(parameters);
        }

        setDuration(DEFAULT_DURATION);
        setPosition(Position.BOTTOM_END);

        LOG.debug("Created notification. data={}", this);
    }

    private NativeButton createButton(ComponentEventListener<ClickEvent<NativeButton>> event) {
        NativeButton button = new NativeButton(getTranslation("buttons.ok.caption"));
        button.addClickListener(e -> this.close());
        button.addClickListener(event);
        button.addClickShortcut(Key.ACCEPT);

        return button;
    }

    @Override
    public void open() {
        LOG.trace("Opening notification. data={}", this);

        List<String> params = this.i18nParameters.stream().map(this::getTranslation).collect(Collectors.toList());
        this.parameters.forEach(params::add);

        add(new Span(getTranslation(i18nKey, params.toArray(new Object[0]))));

        if (event != null) {
            add(createButton(event));
        }

        super.open();
    }


    @Override
    public String toString() {
        return new StringJoiner(", ", TorgNotification.class.getSimpleName() + "[", "]")
                .add("i18nKey='" + i18nKey + "'")
                .add("button=" + (event != null ? "true" : "false"))
                .add("i18nParameters=" + i18nParameters)
                .add("parameters=" + parameters)
                .toString();
    }
}
