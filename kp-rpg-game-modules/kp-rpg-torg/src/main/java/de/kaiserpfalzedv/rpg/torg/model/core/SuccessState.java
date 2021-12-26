/*
 * Copyright (c) 2021 Kaiserpfalz EDV-Service, Roland T. Lichti
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.  If not, see
 * <http://www.gnu.org/licenses/>.
 */

package de.kaiserpfalzedv.rpg.torg.model.core;


import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * SuccessState --
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 0.1.0  2021-03-28
 */
@Schema(
        description = "The possible states of a Torg success.",
        enumeration = {"NONE","FAILED","SUCCESS","GOOD","OUTSTANDING"},
        example = "GOOD",
        defaultValue = "NONE"
)
public enum SuccessState {
    NONE,
    FAILED,
    SUCCESS,
    GOOD,
    OUTSTANDING;
}
