/*
 * Copyright (c) 2020 Kaiserpfalz EDV-Service, Roland T. Lichti
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package de.kaiserpfalzedv.rpg.base.dice;

/**
 * @author rlichti {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 2020-08-12
 */
public interface Die {
    /**
     * a single die roll.
     * @return result of the single die roll.
     */
    int roll();

    /**
     * a single die roll with modifier.
     * @param modifier the modifier for the die roll. This number will be added
     *                to the roll.
     * @return result of the single die roll.
     */
    int modifiedRoll(final int modifier);

    /**
     * Returns number of successes of a multiple dice roll to a certain
     * threshold. Defaults to {@link #successLow(int, int)} but may of course
     * be overwritten for certain games.
     *
     * @param dice number of dice to roll.
     * @param threshold The threshold to match.
     * @return number of successes of this roll.
     */
    default int success(final int dice, final int threshold) {
        return successLow(dice, threshold);
    }

    /**
     * Returns number of successes when throwing multiple dice and need to roll
     * higher than a threshold.
     *
     * @param dice The number of dice to roll.
     * @param threshold The threshold for a successful roll.
     * @return number of successes of this roll.
     */
    int successHigh(final int dice, final int threshold);

    /**
     * Returns number of successes when throwing multiple dice and need to roll
     * lower than a threshold.
     *
     * @param dice The number of dice to roll.
     * @param threshold The threshold for a successful roll.
     * @return number of successes of this roll.
     */
    int successLow(final int dice, final int threshold);

    /**
     * the result of multiple dice rolls.
     * @param number the number of dice rolls.
     * @return ann array of Integer containing the results. The first element contains the sum, starting with index 1
     * the results of the single rolls are returned.
     */
    Integer[] roll(final int number);

    /**
     * The maximum number this die can generate.
     * @return the maximum number on this die.
     */
    int getSides();
}
