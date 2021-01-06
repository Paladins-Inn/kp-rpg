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

import de.kaiserpfalzedv.rpg.core.dice.BasicDie;
import de.kaiserpfalzedv.rpg.core.dice.DiceParser;
import de.kaiserpfalzedv.rpg.core.dice.Die;
import de.kaiserpfalzedv.rpg.core.dice.DieRoll;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.vertx.ConsumeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Optional;
import java.util.StringJoiner;

@ApplicationScoped
public class DiceRoller {
    static private final Logger LOG = LoggerFactory.getLogger(DiceRoller.class);

    @Inject
    DiceParser parser;

    @Inject
    Instance<Die> diceInstances;
    ArrayList<Die> dice = new ArrayList<>();

    public void startup(@Observes final StartupEvent event) {
        this.diceInstances.forEach(dice::add);

        LOG.info("Created die roller plugin {}: parser={}, dice={}", this, parser, dice);
    }

    @ConsumeEvent("throw-dice")
    public String work(final String dieRollString) {
        StringBuilder textBuilder = new StringBuilder();

        Optional<DieRoll> roll = parser.parse(dieRollString);

        if (roll.isEmpty()) {
            throw new IllegalArgumentException("The command '" + dieRollString + "' is no valid die roll!");
        }

        Integer[] result = roll(roll.get());

        textBuilder.append(result[0]);

        if (result.length >= 2) {
            textBuilder.append(" [");

            StringBuilder singleResults = new StringBuilder();

            for (int i = 1; i < result.length; i++) {
                singleResults.append(" | ").append(result[i]);
            }

            textBuilder.append(singleResults.substring(3)).append("]");
        }

        return textBuilder.toString();
    }

    private Integer[] roll(final DieRoll roll) {
        Die die = selectDieType(roll.getDieIdentifier());

        LOG.trace("Roll: die={}, roll={}", die, roll);
        return roll.eval(die);
    }

    private Die selectDieType(final String qualifier) {
        for (Die die : dice) {
            if (die.getDieType().equals(qualifier))
                return die;
        }

        return new BasicDie(Integer.parseInt(qualifier.substring(1)));
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", DiceRoller.class.getSimpleName() + "@" + System.identityHashCode(this) + "[", "]")
                .toString();
    }
}
