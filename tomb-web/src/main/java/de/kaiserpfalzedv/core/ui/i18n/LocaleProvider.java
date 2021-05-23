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

import jakarta.validation.constraints.NotNull;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.util.Locale;

/**
 * LocaleProvider --
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 0.1.0  2021-04-04
 */
@Scope("prototype")
public class LocaleProvider implements LocaleChangeObserver {
    private static final Logger LOG = LoggerFactory.getLogger(LocaleProvider.class);

    private Locale locale;

    @Value("spring.web.locale")
    private String defaultLocale;

    @PostConstruct
    public void init() {
        LOG.debug("Setting default locale. service={}, defaultLocale='{}'", this, defaultLocale);

        try {
            locale = Locale.forLanguageTag(defaultLocale);
        } catch (Exception e) {
            LOG.error("Could not load locale, using java default locale. service={}, defaultLocale='{}', javaLocale={}",
                    this, defaultLocale, Locale.getDefault());

            locale = Locale.getDefault();
        }
    }

    @Override
    public void localeChange(@NotNull final LocaleChangeEvent event) {
        LOG.debug("Switching locale. service={}, oldLocale={}, newLocale={}", this, locale, event.getLocale());

        locale = event.getLocale();
    }

    @Bean
    public Locale getCurrentLocale() {
        LOG.trace("Returning locale. service={}, locale={}", this, locale);
        return locale;
    }
}
