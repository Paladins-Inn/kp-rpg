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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.StringJoiner;

@ApplicationScoped
public class DiceRoller {
    static private final Logger LOG = LoggerFactory.getLogger(DiceRoller.class);

    @Inject
    DiceParser parser;


    @ConsumeEvent("throw-dice")
    public String work(final String dieRollString) {
        RollTotal roll = parser.parse(dieRollString);

        if (roll.isEmpty()) {
            throw new IllegalArgumentException("The command '" + dieRollString + "' is no valid die roll!");
        }

        return roll.getDescription();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", DiceRoller.class.getSimpleName() + "@" + System.identityHashCode(this) + "[", "]")
                .toString();
    }
}
