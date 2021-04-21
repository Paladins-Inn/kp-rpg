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
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.server.VaadinSession;
import de.kaiserpfalzedv.core.ui.i18n.TranslatableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;
import java.util.StringJoiner;

import static com.vaadin.flow.component.Unit.PERCENTAGE;
import static com.vaadin.flow.component.Unit.PIXELS;

/**
 * DataCard -- A generalized card layout.
 *
 * <PRE>
 * +--------+---------------------------------------------+----------+
 * |        | {@literal <Header>}                                    |          |
 * +        +---------------------------------------------+          |
 * | {@literal <Logo>} | {@literal <Description>}                               | {@literal <Margin>} |
 * +        +---------------------------------------------+          |
 * |        | {@literal Footer>}                                    |          |
 * +--------+---------------------------------------------+
 * </PRE>
 *
 *
 *
 * <p>You can set the components into the containers:</p>
 *
 * <ul>
 *     <li>{@literal <Logo>} - {@link Image}</li>
 *     <li>{@literal <Header>} - {@link HorizontalLayout}</li>
 *     <li>{@literal <Description>} - {@link HorizontalLayout}</li>
 *     <li>{@literal <Footer>} - {@link HorizontalLayout}</li>
 *     <li>{@literal <Margin>} - {@link VerticalLayout}</li>
 * </ul>
 *
 * <p>You can use the following methods to manage the containers:</p>
 *
 * <ul>
 *     <li>add{@literal <container name>}(Components ...)</li>
 *     <li>add{@literal <container name>}AsFirst(Component)</li>
 *     <li>add{@literal <container name>}AtIndex(int, Component)</li>
 *     <li>remove{@literal <container name>}(Components ...)</li>
 *     <li>remove{@literal <container name>}All()</li>
 * </ul>
 *
 * <p>For special treatment you can request the containers itself via get{@literal <container name>}() to get access to
 * the low level layout functions. Or you can repalce the container itself via
 * set{@literal <container name>}(HorizontalLayout)</p>
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 0.1.0  2021-03-28
 */
@SuppressWarnings("unused")
public class DataCard extends HorizontalLayout implements Serializable, AutoCloseable, LocaleChangeObserver, TranslatableComponent {
    private static final Logger LOG = LoggerFactory.getLogger(DataCard.class);
    private final HashSet<TranslatableComponent> translatables = new HashSet<>();
    private Locale locale;
    private Component logo;
    private VerticalLayout data;
    private HorizontalLayout dataHeader;
    private HorizontalLayout dataDescription;
    private HorizontalLayout dataFooter;
    private VerticalLayout margin;

    @PostConstruct
    public void init() {
        setLocale(VaadinSession.getCurrent().getLocale());

        addClassName("card");
        setSpacing(false);
        getThemeList().add("spacing-s");

        if (logo == null)
            logo = new Image();

        data = new VerticalLayout();
        data.addClassName("description");
        data.getThemeList().add("spacing-s");
        data.setSpacing(false);
        data.setPadding(false);

        margin = new VerticalLayout();
        margin.addClassName("description");
        margin.getThemeList().add("spacing-s");
        margin.setMinWidth(150, PIXELS);
        margin.setMaxWidth(20, PERCENTAGE);
        margin.setSpacing(false);
        margin.setPadding(false);

        add(logo, data, margin);
        setFlexGrow(100, data);
        setFlexGrow(10, margin);

        if (dataHeader == null)
            setHeader(new HorizontalLayout());
        if (dataDescription == null)
            setDescription(new HorizontalLayout());
        if (dataFooter == null)
            setFooter(new HorizontalLayout());

        translate();
    }

    public void setLogo(@NotNull final Image logo) {
        if (this.logo != null)
            remove(this.logo);
        this.logo = logo;

        logo.setMinHeight(120, PIXELS);
        logo.setMaxHeight(400, PIXELS);
        logo.setMinWidth(120, PIXELS);
        logo.setMaxWidth(300, PIXELS);

        addComponentAsFirst(logo);
    }


    public void addHeader(Component... components) {
        addComponents(components);
        dataHeader.add(components);
    }

    public void addHeaderAsFirst(Component component) {
        addComponents(component);
        dataHeader.addComponentAsFirst(component);
    }

    public void addHeaderAtIndex(int index, Component component) {
        addComponents(component);
        dataHeader.addComponentAtIndex(index, component);
    }

    public void removeHeader(Component... components) {
        removeComponents(components);
        dataHeader.remove(components);
    }

    public void removeAllHeader() {
        removeComponents(dataHeader.getChildren().toArray(Component[]::new));
        dataHeader.removeAll();
    }

    public HorizontalLayout getHeader() {
        return dataHeader;
    }

    public void setHeader(HorizontalLayout layout) {
        layout.addClassName("description");
        layout.getThemeList().add("spacing-s");
        layout.setSpacing(false);
        layout.setPadding(false);
        layout.setWidth(100, PERCENTAGE);

        if (dataHeader != null)
            data.remove(dataHeader);
        dataHeader = layout;
        data.addComponentAsFirst(dataHeader);
    }

    public void addDescription(Component... components) {
        addComponents(components);
        dataDescription.add(components);
    }

    public void addDescriptionAsFirst(Component component) {
        addComponents(component);
        dataDescription.addComponentAsFirst(component);
    }

    public void addDescriptionAtIndex(int index, Component component) {
        addComponents(component);
        dataDescription.addComponentAtIndex(index, component);
    }

    public void removeDescription(Component... components) {
        removeComponents(components);
        dataDescription.remove(components);
    }

    public void removeAllDescription() {
        removeComponents(dataDescription.getChildren().toArray(Component[]::new));
        dataDescription.removeAll();
    }

    public HorizontalLayout getDescription() {
        return dataDescription;
    }

    public void setDescription(HorizontalLayout layout) {
        layout.addClassName("description");
        layout.getThemeList().add("spacing-s");
        layout.setSpacing(false);
        layout.setPadding(false);
        layout.setWidth(100, PERCENTAGE);
        layout.setMinHeight(5, PERCENTAGE);
        layout.setMaxHeight(100, PERCENTAGE);

        if (dataDescription != null)
            data.remove(dataDescription);
        dataDescription = layout;
        data.addComponentAtIndex(1, dataDescription);
        data.setFlexGrow(100, dataDescription);
    }

    public void addFooter(Component... components) {
        addComponents(components);
        dataFooter.add(components);
    }

    public void addFooterAsFirst(Component component) {
        addComponents(component);
        dataFooter.addComponentAsFirst(component);
    }

    public void addFooterAtIndex(int index, Component component) {
        addComponents(component);
        dataFooter.addComponentAtIndex(index, component);
    }

    public void removeFooter(Component... components) {
        removeComponents(components);
        dataFooter.remove(components);
    }

    public void removeAllFooter() {
        removeComponents(dataFooter.getChildren().toArray(Component[]::new));
        dataFooter.removeAll();
    }

    public HorizontalLayout getFooter() {
        return dataFooter;
    }

    public void setFooter(HorizontalLayout layout) {
        layout.addClassName("actions");
        layout.getThemeList().add("spacing-s");
        layout.setSpacing(false);
        layout.setPadding(false);
        layout.setWidth(100, PERCENTAGE);

        if (dataFooter != null)
            data.remove(dataFooter);
        dataFooter = layout;
        data.add(dataFooter);
    }


    public void addMargin(Component... components) {
        addComponents(components);
        margin.add(components);
    }

    public void addMarginAsFirst(Component component) {
        addComponents(component);
        margin.addComponentAsFirst(component);
    }

    public void addMarginAtIndex(int index, Component component) {
        addComponents(component);
        margin.addComponentAtIndex(index, component);
    }

    public void removeMargin(Component... components) {
        removeComponents(components);
        margin.remove(components);
    }

    public void removeAllMargin() {
        removeComponents(margin.getChildren().toArray(Component[]::new));
        margin.removeAll();
    }

    public VerticalLayout getMargin() {
        return margin;
    }

    public void setMargin(VerticalLayout layout) {
        layout.addClassName("actions");
        layout.getThemeList().add("spacing-s");
        layout.setSpacing(false);
        layout.setPadding(false);

        if (margin != null)
            remove(margin);
        margin = layout;
        add(margin);
    }


    private void addComponents(Component... components) {
        for (Component c : components) {
            if (c instanceof TranslatableComponent) {
                translatables.add((TranslatableComponent) c);
            }
        }
    }

    /**
     * Removes and <em>closes</em> all components.
     *
     * @param components Components to be removed.
     */
    private void removeComponents(Component... components) {
        for (Component c : components) {
            if (c instanceof TranslatableComponent) {
                translatables.remove(c);
            }

            if (c instanceof AutoCloseable) {
                try {
                    ((AutoCloseable) c).close();
                } catch (Exception e) {
                    LOG.error("Problem while closing the component.", e);
                }
            }
        }
    }


    @Override
    public void localeChange(LocaleChangeEvent event) {
        LOG.trace("Locale change event. component={}, locale={}", this, event.getLocale());

        setLocale(event.getLocale());
        translate();
    }

    @Override
    public void translate() {
        LOG.trace("Translate DataCard. card={}, locale={}", this, locale);

        for (TranslatableComponent t : translatables) {
            t.translate();
        }

        data.removeAll();
        data.add(dataHeader, dataDescription, dataFooter);
    }

    @Override
    public void setLocale(Locale locale) {
        this.locale = locale;

        for (TranslatableComponent t : translatables) {
            t.setLocale(locale);
        }
    }


    @Override
    public void close() throws Exception {
        LOG.debug("Closing card. card={}", this);

        dataHeader.removeAll();
        dataDescription.removeAll();
        dataFooter.removeAll();
        data.remove();
        logo = null;

        removeAll();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DataCard)) return false;
        DataCard dataCard = (DataCard) o;
        return logo.equals(dataCard.logo) && dataHeader.equals(dataCard.dataHeader) && dataDescription.equals(dataCard.dataDescription) && dataFooter.equals(dataCard.dataFooter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(logo, dataHeader, dataDescription, dataFooter);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", DataCard.class.getSimpleName() + "[", "]")
                .toString();
    }
}
