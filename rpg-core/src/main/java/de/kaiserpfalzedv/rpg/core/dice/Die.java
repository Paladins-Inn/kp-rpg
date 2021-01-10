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

import de.kaiserpfalzedv.rpg.core.dice.mat.DieResult;

/**
 * @author rlichti {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 2020-08-12
 */
public interface Die {
    /**
     * a single die roll.
     * @return result of the single die roll.
     */
    DieResult roll();

    /**
     * the result of multiple dice rolls.
     * @param number the number of dice rolls.
     * @return ann array of Integer containing the results. The first element contains the sum, starting with index 1
     * the results of the single rolls are returned.
     */
    DieResult[] roll(final int number);

    /**
     * @return The name of the die.
     */
    default String getDieType() {
        return getClass().getSimpleName();
    }

    /**
     * @return TRUE, if the result can be parsed as integer (and therefore calculations can be done)
     */
    boolean isNumericDie();
}
