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

package de.kaiserpfalzedv.core.security;

import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Value;

/**
 * LoggedInUser --
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 0.1.0  2021-04-07
 */
public class LoggedInUser {
    private final boolean allow = false;
    private final boolean calculated = false;

    private final String defaultLocale;

    public LoggedInUser(
            @NotNull @Value("${spring.web.locale:de}") final String locale
    ) {
        this.defaultLocale = locale;
    }

}
