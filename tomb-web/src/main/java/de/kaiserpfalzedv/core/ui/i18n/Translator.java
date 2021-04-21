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

package de.kaiserpfalzedv.core.ui.i18n;

import com.sun.istack.NotNull;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.i18n.I18NProvider;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterListener;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.UIInitEvent;
import com.vaadin.flow.server.UIInitListener;
import com.vaadin.flow.server.VaadinServiceInitListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.*;

/**
 * Translator -- Provides a nice way to read translations from Resource bundles.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 0.1.0  2021-03-27
 */
@Scope("singleton")
public class Translator implements
        I18NProvider, VaadinServiceInitListener, UIInitListener, BeforeEnterListener,
        Serializable, AutoCloseable {
    /**
     * The languages this {@link I18NProvider} may provide.
     */
    public static final List<Locale> PROVIDED_LANGUAGES = Arrays.asList(Locale.GERMAN, Locale.ENGLISH);
    private static final Logger LOG = LoggerFactory.getLogger(Translator.class);
    /**
     * Default bundle to use when no other bundle is selected.
     */
    private static final String DEFAULT_BUNDLE = "messages";
    private final HashMap<String, HashMap<Locale, ResourceBundle>> bundles = new HashMap<>();
    @Value("${spring.web.locale:de}")
    private String defaultLocale;

    /**
     * Reads the translation from the default bundle {@value #DEFAULT_BUNDLE}.
     *
     * @param key       The key of the bundle entry.
     * @param locale    the locale to use.
     * @param arguments Arguments for the translation.
     * @return The translated text or {@literal !<key>}
     */
    @Override
    public String getTranslation(final String key, final Locale locale, Object... arguments) {
        return getTranslation(DEFAULT_BUNDLE, key, locale, arguments);
    }

    /**
     * Returns the translation from a resource accompanying the class given as bundleObject.
     *
     * @param bundleObject The class to be translated.
     * @param key          The key for the translation.
     * @param locale       The locale to use.
     * @param arguments    Arguments to the translation.
     * @return The translated string or {@literal !<key>}.
     */
    public String getTranslation(final Object bundleObject, final String key, final Locale locale, final Object... arguments) {
        String bundleName = bundleObject.getClass().getCanonicalName().replace(".", "/");

        return getTranslation(bundleName, key, locale, arguments);
    }

    /**
     * Reads the translation from the bundle with the given name.
     *
     * @param bundleName The name of the bundle. The locale and the postfix {@literal .properties} will be appended.
     * @param key        The key of the bundle entry.
     * @param locale     the locale to use.
     * @param arguments  Arguments for the translation.
     * @return The translated text or {@literal !<key>}
     */
    public String getTranslation(final String bundleName, final String key, final Locale locale, Object... arguments) {
        loadBundle(bundleName, locale);

        try {
            final String pattern = bundles.get(bundleName).get(locale).getString(key);
            final MessageFormat format = new MessageFormat(pattern, locale);
            return format.format(arguments);
        } catch (NullPointerException | MissingResourceException ex) {
            LOG.warn(
                    "Translation failed. bundle={}, locale={}, key={}",
                    bundleName, locale, key
            );
            return "!" + key;
        }
    }


    /**
     * Loads the bundle into the cache.
     *
     * @param bundleName The base filename for the translation bundle.
     * @param locale     The locale to load the bundle for.
     */
    private void loadBundle(@NotNull String bundleName, @NotNull Locale locale) {
        if (!bundles.containsKey(bundleName)) {
            LOG.debug("Adding bundle. baseName='{}'", bundleName);

            bundles.put(bundleName, new HashMap<>());
        }

        if (locale == null) {
            locale = Locale.forLanguageTag(defaultLocale);
        }

        if (!bundles.get(bundleName).containsKey(locale)) {
            LOG.info("Loading bundle. baseName='{}', locale='{}'", bundleName, locale.getDisplayName());

            ResourceBundle bundle;
            try {
                bundle = ResourceBundle.getBundle(bundleName, locale, new UnicodeResourceBundleControl());
            } catch (NullPointerException | MissingResourceException e) {
                Locale l = Locale.forLanguageTag(locale.getLanguage());

                LOG.warn("Translator did not find the wanted locale for the bundle. bundle={}, locale={}, orig.locale={}",
                        bundleName, l, locale);
                try {
                    bundle = ResourceBundle.getBundle(bundleName, l, new UnicodeResourceBundleControl());
                } catch (NullPointerException | MissingResourceException e1) {
                    LOG.warn("Translator did not find the wanted bundle. Using default bundle. bundle={}", bundleName);

                    try {
                        bundle = ResourceBundle.getBundle(DEFAULT_BUNDLE, Locale.forLanguageTag(defaultLocale),
                                new UnicodeResourceBundleControl());
                    } catch (NullPointerException e2) {
                        LOG.error("Resource bundle can't be read.", e2);

                        return;
                    }
                }
            }
            bundles.get(bundleName).put(locale, bundle);
        }
    }

    @Override
    public void close() throws Exception {
        LOG.info("Closing all bundles.");
        bundles.clear();
    }

    @Override
    public List<Locale> getProvidedLocales() {
        return PROVIDED_LANGUAGES;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Class<?> navigationTarget = event.getNavigationTarget();
        I18nPageTitle annotation = navigationTarget.getAnnotation(I18nPageTitle.class);
        if (annotation == null) {
            LOG.warn("No page title set for view. view={}", navigationTarget.getName());
        } else {
            final String messageKey = (annotation.value().isEmpty())
                    ? annotation.defaultValue()
                    : annotation.value();

            Locale locale = event.getUI().getLocale() != null
                    ? event.getUI().getLocale()
                    : Locale.forLanguageTag(defaultLocale);

            String pageTitle = getTranslation(messageKey, locale);

            UI.getCurrent().getPage().setTitle(pageTitle);
        }
    }

    @Override
    public void uiInit(UIInitEvent event) {
        event.getUI().addBeforeEnterListener(this);
    }

    @Override
    public void serviceInit(ServiceInitEvent event) {
        event.getSource().addUIInitListener(this);
    }

    /**
     * @author peholmst
     * @since 0.1.0
     */
    private static class UnicodeResourceBundleControl extends ResourceBundle.Control {
        @Override
        public ResourceBundle newBundle(
                final String baseName,
                final Locale locale,
                final String format,
                final ClassLoader loader,
                final boolean reload
        ) throws IllegalAccessException, InstantiationException, IOException {

            String bundleName = toBundleName(baseName, locale);
            String resourceName = toResourceName(bundleName, "properties");
            final URL resourceURL = loader.getResource(resourceName);
            if (resourceURL == null)
                return null;

            try (BufferedReader in = new BufferedReader(new InputStreamReader(resourceURL.openStream(), StandardCharsets.UTF_8))) {
                return new PropertyResourceBundle(in);
            }
        }
    }

}
