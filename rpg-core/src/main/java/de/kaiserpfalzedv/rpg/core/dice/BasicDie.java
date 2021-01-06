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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Implements the die rolling.
 *
 * @author rlichti {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 2020-08-12
 */
public class BasicDie implements Die {
    private static final Logger LOGGER = LoggerFactory.getLogger(BasicDie.class);

    /**
     * The number of sides of the die.
     */
    private final int max;

    public BasicDie(final int max) {
        this.max = max;
    }

    @Override
    public final int getMax() {
        return max;
    }

    @Override
    public int roll() {
        return (int) (Math.random( ) * max + 1);
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
        return max == basicDie.max;
    }

    @Override
    public final int hashCode() {
        return Objects.hash(max);
    }


    @Override
    public final String toString() {
        return new StringJoiner(", ",
                getClass().getSimpleName() + "@" + System.identityHashCode(this) + "[",
                "]")
                .add("max=" + max)
                .toString();
    }
}
