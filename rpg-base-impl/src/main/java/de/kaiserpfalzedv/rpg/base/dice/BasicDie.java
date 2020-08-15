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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Implements the die rolling. If no special die is created by using the Constructor
 *
 * @author rlichti {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 2020-08-12
 */
public class BasicDie implements Die {
    private static final Logger LOGGER = LoggerFactory.getLogger(BasicDie.class);

    /**
     * The number of sides of the die.
     */
    private final int sides;

    public BasicDie() {
        this.sides = 6;
    }

    public BasicDie(final int sides) {
        this.sides = sides;
    }

    @Override
    public final int getSides() {
        return sides;
    }

    @Override
    public final int roll() {
        return (int) (Math.random() * sides + 1);
    }

    @Override
    public int modifiedRoll(int modifier) {
        return roll() + modifier;
    }

    @Override
    public int successHigh(int dice, int threshold) {
        int result = 0;

        for (int roll : roll(dice)) {
            result += (roll > threshold) ? 1 : 0;
        }

        return result;
    }

    @Override
    public int successLow(int dice, int threshold) {
        int result = 0;

        for (int roll : roll(dice)) {
            result += (roll <= threshold) ? 1 : 0;
        }

        return result;
    }

    /**
     * This method rolls multiple dice. The result array contains the sum in element 0 and the single results in the
     * following elements.
     *
     * @param number how many dice should be rolled?
     * @return array containing the sum in [0] and the different results starting on index [1]
     */
    @Override
    public final Integer[] roll(final int number) {
        ArrayList<Integer> result = new ArrayList<>(number + 1);

        int sum = 0;
        result.add(0);
        for(int i = 1; i <= number; i++) {
            result.add(roll());
            result.set(0, result.get(0) + result.get(i));
        }

        LOGGER.trace("roll {}. result={}", getClass().getSimpleName(), result);
        return result.toArray(new Integer[1]);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BasicDie)) return false;
        BasicDie basicDie = (BasicDie) o;
        return sides == basicDie.sides;
    }

    @Override
    public final int hashCode() {
        return Objects.hash(sides);
    }


    @Override
    public final String toString() {
        return new StringJoiner(
                ", ",
                BasicDie.class.getSimpleName() + "@" + System.identityHashCode(this) + "[",
                "]"
        )
                .add("max=" + sides)
                .toString();
    }
}
