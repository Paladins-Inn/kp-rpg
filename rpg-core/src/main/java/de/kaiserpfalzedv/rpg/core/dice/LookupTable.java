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

package de.kaiserpfalzedv.rpg.core.dice;

/**
 * a lookup table to calculate a numeric result from a dice throw.
 *
 * @author klenkes74 {@literal <rlichit@kaiserpfalz-edv.de>}
 * @since 1.0.0 2021-01-09
 */
public interface LookupTable {
    /**
     * This is a lookup to calculate a result from a die roll.
     *
     * @param total The original die roll.
     * @return The looked up value.
     */
    int lookup(int total);
}
