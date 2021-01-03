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

import de.kaiserpfalzedv.rpg.bot.BotPlugin;
import de.kaiserpfalzedv.rpg.core.dice.*;
import de.kaiserpfalzedv.rpg.torg.dice.BD;
import de.kaiserpfalzedv.rpg.torg.dice.T20;
import de.kaiserpfalzedv.rpg.torg.dice.T20M;
import io.quarkus.runtime.StartupEvent;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.impl.DataMessage;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Default;
import java.util.Optional;
import java.util.UUID;

@Default
@ApplicationScoped
public class Roller implements BotPlugin {
    static private final Logger LOG = LoggerFactory.getLogger(Roller.class);

    DiceParser parser = new DiceParser();

    public void startup(@Observes final StartupEvent event) {
        LOG.info("Created die roller plugin: event={}", event);
    }

    @Override
    public void work(MessageReceivedEvent event) {
        String command = event.getMessage().getContentRaw();

        if (command.startsWith("/r ")) {
            Optional<DieRoll> roll = parser.parse(command.substring(3));

            if (roll.isEmpty()) {
                Message msg = new DataMessage(true, event.getAuthor().getAsMention() + " kann nicht richtig würfeln!", UUID.randomUUID().toString(), null);
                event.getChannel().sendMessage(msg).queue();
                return;
            }

            Integer[] result = roll(roll.get());

            StringBuilder textBuilder = new StringBuilder()
                    .append(event.getAuthor().getAsMention())
                    .append(" rolled ")
                    .append(command.substring(3))
                    .append(": ");

            textBuilder.append(result[0]);

            if (result.length >= 2) {
                textBuilder.append(" [");

                StringBuilder singleResults = new StringBuilder();

                for (int i = 1; i < result.length; i++) {
                    singleResults.append(" | ").append(result[i]);
                }

                textBuilder.append(singleResults.substring(3)).append("]");
            }

            String text = textBuilder.toString();

            LOG.trace("Respond: plugin={}, text='{}'", getClass().getSimpleName(), text);
            Message msg = new DataMessage(false, text, UUID.randomUUID().toString(), null);
            event.getChannel().sendMessage(msg).queue();
        }
    }


    private Integer[] roll(final DieRoll roll) {
        Die die;

        switch (roll.getDieIdentifier().toUpperCase()) {
            case "D2": die = new D2();
            break;

            case "D4": die = new D4();
            break;

            case "D6": die = new D6();
            break;

            case "D8": die = new D8();
            break;

            case "D10": die = new D10();
            break;

            case "D12": die = new D12();
            break;

            case "D20": die = new D20();
            break;

            case "D100": die = new D100();
            break;

            case "DBD": die = new BD();
            break;

            case "DT20": die = new T20();
            break;

            case "DT20M": die = new T20M();
            break;

            default: die = new BasicDie(Integer.parseInt(roll.getDieIdentifier().substring(2)));
        }

        Integer[] result = die.roll(roll.getNumberOfDice());
        result[0] = (int) Math.round((result[0] + roll.getAdd()) * roll.getMultiply());

        return result;
    }
}
