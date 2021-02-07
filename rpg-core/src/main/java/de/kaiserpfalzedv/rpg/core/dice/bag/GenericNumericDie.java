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

package de.kaiserpfalzedv.rpg.core.dice.bag;

import de.kaiserpfalzedv.rpg.core.dice.Die;
import de.kaiserpfalzedv.rpg.core.dice.mat.DieResult;
import de.kaiserpfalzedv.rpg.core.dice.mat.ImmutableDieResult;

import java.util.ArrayList;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * GenericNumericDie -- Implements the die rolling for numeric die from 1 to {@link #max}.
 *
 * @author rlichti {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 2020-08-12
 */
public class GenericNumericDie implements Die {
    /**
     * The number of sides of the die.
     */
    public final int max;

    public GenericNumericDie(final int max) {
        this.max = max;
    }

    /**
     * The die roll itself.
     * @return the numeric result of the roll of this die.
     */
    protected int rollSingle() {
        return (int) (Math.random( ) * max + 1);
    }


    @Override
    public DieResult roll() {
        String roll = Integer.toString(rollSingle(), 10);
        return ImmutableDieResult.builder()
                .die(this)
                .total(roll)
                .rolls(roll)
                .build();
    }

    @Override
    public final DieResult[] roll(final int number) {
        ArrayList<DieResult> results = new ArrayList<>(number);

        for(int i = 1; i <= number; i++) {
            results.add(roll());
        }

        return results.toArray(new DieResult[0]);
    }

    @Override
    public boolean isNumericDie() {
        return true;
    }

    @Override
    public String getDieType() {
        return "D" + max;
    }


    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GenericNumericDie)) return false;
        GenericNumericDie basicDie = (GenericNumericDie) o;
        return max == basicDie.max;
    }

    @Override
    public final int hashCode() {
        return Objects.hash(max);
    }

    @Override
    public final String toString() {
        return new StringJoiner(", ",
                getDieType() + "[",
                "]")
                .add("identity=" + System.identityHashCode(this))
                .toString();
    }
}
