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

package de.kaiserpfalzedv.rpg.bot.dice;

import de.kaiserpfalzedv.rpg.core.dice.DiceParser;
import de.kaiserpfalzedv.rpg.core.dice.mat.RollTotal;
import io.quarkus.vertx.ConsumeEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.StringJoiner;

@ApplicationScoped
public class DiceRoller {
    @Inject
    DiceParser parser;


    @ConsumeEvent("throw-dice")
    public String work(final String dieRollString) {
        String[] rollAndComment = dieRollString.split(" # ", 2);

        RollTotal roll = results(dieRollString);

        return (rollAndComment.length > 1 && rollAndComment[1] != null ? rollAndComment[1] + ": " : "") + roll.getDescription();
    }

    /**
     * return all results.
     *
     * @param dieRollString The roll to parse.
     * @return All results instead of a parsed string like in {@link #work(String)}
     */
    public RollTotal results(final String dieRollString) {
        String[] rollAndComment = dieRollString.split(" # ", 2);

        RollTotal roll = parser.parse(rollAndComment[0]);

        if (roll.isEmpty()) {
            throw new IllegalArgumentException("The command '" + dieRollString + "' is no valid die roll!");
        }

        return roll;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", DiceRoller.class.getSimpleName() + "[", "]")
                .add("identity=" + System.identityHashCode(this))
                .toString();
    }
}
