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

import net.objecthunter.exp4j.Expression;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * DiceRollCommand -- A generic die roll command object.
 *
 * This is a generic die roll. It specifies a die type and an expression to be evaluated after the die roll is
 * put as "x" into the equation:
 *
 * <code>
 *      DieRoll roll = ...
 *      int result = roll.eval(&lt;die roll total&gt;);
 * </code>
 */
public class DieRoll {
    private final String dieIdentifier;
    private final int amountOfDice;
    private final Expression expression;


    public DieRoll(final String dieType, final int amountOfDice, final Expression expression) {
        this.dieIdentifier = dieType.toUpperCase();
        this.amountOfDice = amountOfDice;
        this.expression = expression;
    }

    /**
     * This function evaluates the whole die expression and rounds according to mathematical rules.
     *
     * @param die Die to roll.
     * @return The total of the die roll.
     */
    public Integer[] eval(final Die die) {
        Integer[] result = die.roll(amountOfDice);

        result[0] = (int) Math.round(expression.setVariable("x", result[0]).evaluate());

        return result;
    }

    public String getDieIdentifier() {
        return dieIdentifier;
    }

    public int getAmountOfDice() {
        return amountOfDice;
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DieRoll)) return false;
        DieRoll dieRoll = (DieRoll) o;
        return getExpression() == dieRoll.getExpression() &&
                getDieIdentifier().equals(dieRoll.getDieIdentifier());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getExpression(), getDieIdentifier());
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", getClass().getSimpleName() + "@" + System.identityHashCode(this) + "[", "]")
                .add("dieIdentifier='" + dieIdentifier + "'")
                .add("expression=" + expression)
                .toString();
    }
}
