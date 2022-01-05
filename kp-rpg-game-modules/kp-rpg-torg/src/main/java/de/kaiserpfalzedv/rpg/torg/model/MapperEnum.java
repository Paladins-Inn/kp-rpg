/*
 * Copyright (c) &today.year Kaiserpfalz EDV-Service, Roland T. Lichti
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.kaiserpfalzedv.rpg.torg.model;

import javax.validation.constraints.NotNull;
import java.util.Optional;

/**
 * MapperEnum -- Mapper Enums contain the strings used by foundry VTT and Roll20 used for the resource.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0  2021-06-06
 */
public interface MapperEnum<T> {
    String getRoll20();

    String getFoundry();

    Optional<T> mapFromFoundry(@NotNull final String name);

    Optional<T> mapFromRoll20(@NotNull final String name);
}
