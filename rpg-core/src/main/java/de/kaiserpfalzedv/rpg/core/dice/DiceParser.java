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
import net.objecthunter.exp4j.ExpressionBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.Dependent;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ParseDiceString -- Parses a string to the DiceRollCommand.
 *
 * @author klenkes74
 * @since 2020-01-03
 */
@Dependent
public class DiceParser {
    static private final Logger LOG = LoggerFactory.getLogger(DiceParser.class);

    static private final String DICE_PATTERN =
            "(?<pre>([(])?)?"
            +"(?<amount>\\d+)?"
            +"(?<type>([dD])?[A-Za-z][0-9A-Za-z]+)"
            +"(?<post>.*)?";

    static private final Pattern PATTERN = Pattern.compile(DICE_PATTERN);

    /**
     * Parses the string.
     *
     * @param diceString The dice string to parse.
     * @return The roll preparsed for the services.
     */
    public Optional<DieRoll> parse(final String diceString) {
        Matcher m = PATTERN.matcher(diceString);
        DieRoll result = null;

        if (m.matches()) {
            String pre = m.group("pre");
            if (pre == null || pre.isBlank()) {
                pre = "";
            }

            String amountString = m.group("amount");
            int amount = 1;
            if (amountString != null && !amountString.isBlank()) {
                amount = Integer.parseInt(amountString);
            }

            String dieIdentifier = m.group("type");
            if (dieIdentifier == null) {
                dieIdentifier = "D6";
            }

            if (dieIdentifier.startsWith("w") || dieIdentifier.startsWith("W")) {
                dieIdentifier  = "D" + dieIdentifier.substring(1);
            }

            String post = m.group("post");
            if (post != null && post.isBlank()) {
                post = "";
            }

            StringBuilder expressionString = new StringBuilder();
            if (! pre.isBlank()) {
                expressionString.append(pre).append("x").append(post);
            } else {
                expressionString.append("x").append(post);
            }

            String expression = expressionString.toString();
            LOG.trace("Die roll expression: input='{}', amount={}, expression='{}'", diceString, amount, expression);

            try {
                Expression e = new ExpressionBuilder(expression).variable("x").build();
                result = new DieRoll(dieIdentifier, amount, e);
            } catch (IllegalArgumentException e) {
                LOG.warn("Expression '" + diceString + "' is not valid: " + e.getMessage());
            }
        }

        return Optional.ofNullable(result);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", DiceParser.class.getSimpleName() + "@" + System.identityHashCode(this) + "[", "]")
                .toString();
    }
}
