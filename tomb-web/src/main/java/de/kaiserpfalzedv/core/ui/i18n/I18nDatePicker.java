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
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.server.VaadinSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Locale;

/**
 * I18nDatePicker -- An auto localized DatePicker for Vaadin.
 * <p>
 * Oh. and just for the record: week starts at monday.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 0.1.0  2021-04-04
 */
@Service
@Scope("prototype")
@Slf4j
public class I18nDatePicker extends DatePicker implements LocaleChangeObserver {

    private Locale locale;

    public I18nDatePicker() {
        locale = VaadinSession.getCurrent().getLocale();

        translate();
    }

    private void translate() {
        log.debug("Translating. component={}, locale={}", this, locale);

        setI18n(new DatePicker.DatePickerI18n()
                .setWeek(getTranslation("input.datepicker.week"))
                .setCalendar(getTranslation("input.datepicker.calendar"))
                .setToday(getTranslation("input.datepicker.today"))
                .setCancel(getTranslation("buttons.cancel.caption"))
                .setFirstDayOfWeek(1)
                .setMonthNames(Arrays.asList(
                        getTranslation("input.datepicker.months.january"),
                        getTranslation("input.datepicker.months.february"),
                        getTranslation("input.datepicker.months.march"),
                        getTranslation("input.datepicker.months.april"),
                        getTranslation("input.datepicker.months.may"),
                        getTranslation("input.datepicker.months.june"),
                        getTranslation("input.datepicker.months.july"),
                        getTranslation("input.datepicker.months.august"),
                        getTranslation("input.datepicker.months.septembre"),
                        getTranslation("input.datepicker.months.octobre"),
                        getTranslation("input.datepicker.months.novembre"),
                        getTranslation("input.datepicker.months.decembre")
                ))
                .setWeekdays(Arrays.asList(
                        getTranslation("input.datepicker.days.long.sunday"),
                        getTranslation("input.datepicker.days.long.monday"),
                        getTranslation("input.datepicker.days.long.tuesday"),
                        getTranslation("input.datepicker.days.long.wednesday"),
                        getTranslation("input.datepicker.days.long.thursday"),
                        getTranslation("input.datepicker.days.long.friday"),
                        getTranslation("input.datepicker.days.long.saturday")
                ))
                .setWeekdaysShort(Arrays.asList(
                        getTranslation("input.datepicker.days.short.sunday"),
                        getTranslation("input.datepicker.days.short.monday"),
                        getTranslation("input.datepicker.days.short.tuesday"),
                        getTranslation("input.datepicker.days.short.wednesday"),
                        getTranslation("input.datepicker.days.short.thursday"),
                        getTranslation("input.datepicker.days.short.friday"),
                        getTranslation("input.datepicker.days.short.saturday")
                ))
        );
    }

    @Override
    public void localeChange(@NotNull final LocaleChangeEvent event) {
        locale = event.getLocale();

        translate();
    }
}
