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

package de.kaiserpfalzedv.rpg.core.user;

import de.kaiserpfalzedv.rpg.core.api.BaseException;

/**
 * InvalidUserException -- The user has no data set.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0  2021-02-04
 */
public class InvalidUserException extends BaseException {
    /**
     * The user data is invalid. No spec is defined.
     *
     * @param user The invalid user
     */
    @SuppressWarnings("CdiInjectionPointsInspection")
    public InvalidUserException(final User user) {
        super(String.format("The user '%s/%s' has no data set!", user.getNamespace(), user.getName()));
    }
}
