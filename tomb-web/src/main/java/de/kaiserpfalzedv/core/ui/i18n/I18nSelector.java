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

import ch.carnet.kasparscherrer.LanguageSelect;
import com.sun.istack.NotNull;

import java.util.Locale;

/**
 * I18nSelector -- A small wrapper around {@link LanguageSelect} for setting the available languages.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 0.1.0  2021-03-27
 */
public class I18nSelector extends LanguageSelect {
    public I18nSelector(@NotNull final Locale selectedLocale) {
        this("input.locale", selectedLocale);
    }

    public I18nSelector(@NotNull final String i18nPrefix, @NotNull final Locale selectedLocale) {
        super(true, Translator.PROVIDED_LANGUAGES.toArray(new Locale[0]));

        setValue(selectedLocale);
        setLabel(getTranslation(i18nPrefix + ".caption", getValue()));
        setHelperText(getTranslation(i18nPrefix + ".help", getValue()));

        setRequiredIndicatorVisible(true);
        setEmptySelectionAllowed(false);
        setRequiredIndicatorVisible(true);

        addValueChangeListener(ev -> {
            setLabel(getTranslation(i18nPrefix + ".caption", ev.getValue()));
            setHelperText(getTranslation(i18nPrefix + ".help", ev.getValue()));
        });
    }
}
