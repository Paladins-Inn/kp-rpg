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

package de.kaiserpfalzedv.rpg.core.dice.mat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.immutables.value.Value;

import java.io.Serializable;
import java.util.StringJoiner;


/**
 * RollTotal -- The result of a numeric die result.
 *
 * This is the result of a numeric die roll.
 */
@Value.Immutable
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonSerialize(as = ImmutableExpressionTotal.class)
@JsonDeserialize(builder = ImmutableExpressionTotal.Builder.class)
@Schema(name = "ExpressionTotal", description = "A generic result of an expression.")
public interface ExpressionTotal extends Serializable {
    /**
     * Displays the roll.
     *
     * @return the expression description.
     */
    default String getDescription() {
        StringJoiner rolls = new StringJoiner(", ", "{", "}");

        for (DieResult r : getRolls()) {
            rolls.add(r.getDisplay());
        }

        return new StringBuilder(getExpression().replace("x", getDieIdentifier()))
                .append(": ")
                .append(calcuateExpression())
                .append(rolls)
                .toString();
    }

    default String getDieIdentifier() {
        return getRolls()[0].getDie().getDieType();
    }

    /**
     * @return The results of the di(c)e roll(s).
     */
    DieResult[] getRolls();

    /**
     * @return The parsed expression
     */
    String getExpression();

    default int getAmountOfDice() {
        return getRolls().length;
    }

    /**
     * This calculates the expression. If the die can't be evaluated
     * @return the calculated expression as result of the roll.
     */
    default String calcuateExpression() {
        if (getRolls().length > 0 && getRolls()[0].getDie().isNumericDie()) {
            int total = calcuateTotal(getRolls());

            Expression math = new ExpressionBuilder(getExpression()).variable("x").build()
                    .setVariable("x", total);

            return Integer.toString((int)math.evaluate(), 10);
        }

        return "";
    }

    private int calcuateTotal(DieResult[] rolls) {
        int total = 0;

        if (rolls.length > 0 && rolls[0].getDie().isNumericDie()) {
            for (DieResult r : rolls) {
                total += Integer.parseInt(r.getTotal(), 10);
            }
        }

        return total;
    }
}
