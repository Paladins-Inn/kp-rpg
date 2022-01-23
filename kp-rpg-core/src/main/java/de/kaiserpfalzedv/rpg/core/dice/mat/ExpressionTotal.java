/*
 * Copyright (c) 2022 Kaiserpfalz EDV-Service, Roland T. Lichti
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

package de.kaiserpfalzedv.rpg.core.dice.mat;

import com.fasterxml.jackson.annotation.JsonInclude;
import de.kaiserpfalzedv.rpg.core.dice.Die;
import de.kaiserpfalzedv.rpg.core.dice.LookupTable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.io.Serializable;
import java.util.Optional;
import java.util.StringJoiner;


/**
 * RollTotal -- The result of a numeric die result.
 *
 * This is the result of a numeric die roll.
 */
@Jacksonized
@Builder(toBuilder = true)
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@Schema(name = "ExpressionTotal", description = "A generic result of an expression.")
public class ExpressionTotal implements Serializable {
    private DieResult[] rolls;
    private String expression;


    /**
     * Displays the roll.
     *
     * @return the expression description.
     */
    public String getDescription() {
        StringJoiner rolls = new StringJoiner(", ", "{", "}");

        for (DieResult r : getRolls()) {
            rolls.add(r.getDisplay());
        }

        return new StringBuilder(getExpression().replace("x", getDieIdentifier()))
                .append(": ")
                .append(calculateExpression())
                .append(rolls)
                .toString();
    }

    public String getDieIdentifier() {
        return getRolls()[0].getDie().getDieType();
    }

    public int getAmountOfDice() {
        return getRolls().length;
    }

    /**
     * This calculates the expression. If the die can't be evaluated
     *
     * @return the calculated expression as result of the roll.
     */
    public String calculateExpression() {
        String result = "";
        if (getRolls().length > 0 && getRolls()[0].getDie().isNumericDie()) {
            Die die = getRolls()[0].getDie();

            int roll = calcuateTotal(getRolls());

            Expression math = new ExpressionBuilder(getExpression()).variable("x").build();

            int total = roll;
            Optional<LookupTable> table = die.getLookupTable();
            if (table.isPresent()) {
                total = table.get().lookup(roll);
            }

            total = (int) math.setVariable("x", total).evaluate();
            result = Integer.toString(total, 10) + " | " + Integer.toString(roll, 10);
        }

        return result;
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
