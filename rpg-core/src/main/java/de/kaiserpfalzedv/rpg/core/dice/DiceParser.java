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

import de.kaiserpfalzedv.rpg.core.dice.bag.GenericNumericDie;
import de.kaiserpfalzedv.rpg.core.dice.mat.ExpressionTotal;
import de.kaiserpfalzedv.rpg.core.dice.mat.RollTotal;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ParseDiceString -- Parses a string to the DiceRollCommand.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 2020-01-03
 */
@Dependent
@RequiredArgsConstructor(onConstructor_= {@Inject})
@ToString
@Slf4j
public class DiceParser {
    static private final String DICE_PATTERN =
            "(?<pre>(([A-Za-z]+)?[(])?)?"
                    + "(?<amount>\\d+)?"
                    + "(?<type>([dD])?[A-Za-z][0-9A-Za-z]+)"
                    + "(?<post>.*)?";

    static private final Pattern PATTERN = Pattern.compile(DICE_PATTERN);

    private final Collection<Die> dice;

    @PostConstruct
    public void startUp() {
        log.debug("Loaded dice: {}", dice);
    }

    public RollTotal parse(final String diceString) {
        String[] dieString = diceString.split("\\s+");

        log.debug("working on di(c)e roll: {}", (Object[]) dieString);

        RollTotal.RollTotalBuilder result = RollTotal.builder();


        List<ExpressionTotal> expressions = new ArrayList<>();
        for (String d : dieString) {
            parseSingleDie(d).ifPresent(expressions::add);
        }

        return result.withExpressions(expressions).build();
    }

    /**
     * Parses the string of a single die roll.
     *
     * @param dieString The die string to parse.
     * @return The roll preparsed for the services.
     */
    public Optional<ExpressionTotal> parseSingleDie(final String dieString) {
        Matcher m = PATTERN.matcher(dieString);

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
            dieIdentifier = dieIdentifier.toUpperCase();

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
            log.trace("Die roll expression: input='{}', amount={}, expression='{}'", dieString, amount, expression);

            Die die;
            try {
                die = selectDieType(dieIdentifier);
            } catch (NumberFormatException e) {
                log.warn("Can't find a valid die for this expression!");

                return Optional.empty();
            }

            try {
                new ExpressionBuilder(expression).variable("x").build();
            } catch (IllegalArgumentException e) {
                log.warn("Expression '" + dieString + "' is not valid: " + e.getMessage());

                return Optional.empty();
            }

            ExpressionTotal result = ExpressionTotal.builder()
                    .withRolls(die.roll(amount))
                    .withExpression(expression)
                    .build();

            log.debug("Parsed die: {}", result);
            return Optional.of(result);
        }

        return Optional.empty();
    }


    private Die selectDieType(final String qualifier) {
        for (Die die : dice) {
            log.trace("Checking die type: qualifier={}, die={}", qualifier, die.getDieType());
            if (die.getDieType().equalsIgnoreCase(qualifier))
                return die;
        }

        return new GenericNumericDie(Integer.parseInt(qualifier.substring(1)));
    }
}
