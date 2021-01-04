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
            "(?<amount>\\d+)?"
            +"(?<dieType>([dD])?[A-Za-z][0-9A-Za-z]+)"
            +"(?<add>[+-](?<D>\\d+))?"
            +"((?<mult>[*x:\\/])(?<C>\\d+))?";

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
            String amountDiceString = m.group("amount");
            if (amountDiceString == null) {
                amountDiceString = "1";
            }

            String dieIdentifier = m.group("dieType");
            if (dieIdentifier == null) {
                dieIdentifier = "D6";
            }

            String addString = m.group("D");
            if (addString == null) {
                addString = "0";
            }
            String addOrDel = m.group("add");
            if ("-".equals(addOrDel)) {
                addString = addOrDel + addString;
            }

            String multiplierString = m.group("C");
            if (multiplierString == null) {
                multiplierString = "1";
            }

            int amount = Integer.parseInt(amountDiceString);
            int add = Integer.parseInt(addString);
            double multiplier = Double.parseDouble(multiplierString);

            String multiOrDivide = m.group("mult");
            if ("/".equals(multiOrDivide) || ":".equals(multiOrDivide)) {
                multiplier = 1 / multiplier;
            }

            result = new DieRoll(amount, dieIdentifier, add, multiplier);
        }

        return Optional.ofNullable(result);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", DiceParser.class.getSimpleName() + "@" + System.identityHashCode(this) + "[", "]")
                .toString();
    }
}
