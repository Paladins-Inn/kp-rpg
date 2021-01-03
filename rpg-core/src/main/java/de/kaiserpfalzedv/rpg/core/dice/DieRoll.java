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

import java.util.Objects;
import java.util.StringJoiner;

/**
 * DiceRollCommand -- A generic die roll command object.
 *
 * This is a generic die roll. It specifies how many dice should be thrown and what amount should be added (or
 * subtracted). The result then will be multiplied (or divided) by the multiply value.
 */
public class DieRoll {
    private final int numberOfDice;
    private final String dieIdentifier;
    private final int add;
    private final double multiply;

    public DieRoll(int numberOfDice, String dieIdentifier, int add, double multiply) {
        this.numberOfDice = numberOfDice;
        this.dieIdentifier = dieIdentifier;
        this.add = add;
        this.multiply = multiply;
    }

    public int getNumberOfDice() {
        return numberOfDice;
    }

    public String getDieIdentifier() {
        return dieIdentifier;
    }

    public int getAdd() {
        return add;
    }

    public double getMultiply() {
        return multiply;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DieRoll)) return false;
        DieRoll dieRoll = (DieRoll) o;
        return getNumberOfDice() == dieRoll.getNumberOfDice() &&
                getAdd() == dieRoll.getAdd() &&
                getMultiply() == dieRoll.getMultiply() &&
                getDieIdentifier().equals(dieRoll.getDieIdentifier());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNumberOfDice(), getDieIdentifier(), getAdd(), getMultiply());
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", DieRoll.class.getSimpleName() + "[", "]")
                .add("numberOfDice=" + numberOfDice)
                .add("dieIdentifier='" + dieIdentifier + "'")
                .add("add=" + add)
                .add("multiply=" + multiply)
                .toString();
    }
}
