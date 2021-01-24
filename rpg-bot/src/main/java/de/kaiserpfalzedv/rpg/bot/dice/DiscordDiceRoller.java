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

import de.kaiserpfalzedv.rpg.bot.discord.DiscordPlugin;
import io.quarkus.runtime.StartupEvent;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.impl.DataMessage;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionAddEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.StringJoiner;
import java.util.UUID;

/**
 * de.kaiserpfalzedv.rpg.bot.dice.DiscordDiceRoller -- The discord bridge for rolling dice.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.0.0 2021-01-11
 */
@ApplicationScoped
public class DiscordDiceRoller implements DiscordPlugin {
    static private final Logger LOG = LoggerFactory.getLogger(DiscordDiceRoller.class);

    @Inject
    DiceRoller roller;

    public void startup(@Observes final StartupEvent event) {
        LOG.info("Created discord roller plugin {}: roller={}", this, roller);
    }

    @Override
    public void work(MessageReceivedEvent event) {
        String command = event.getMessage().getContentRaw();

        if (command.startsWith("/r ")) {
            LOG.debug("{} working on: command={}, roller={}", this, command, roller);

            String roll;
            try {
                roll = roller.work(command.substring(3));
            } catch (IllegalArgumentException e) {
                Message msg = new DataMessage(true, event.getAuthor().getAsMention() + " kann nicht richtig würfeln!", UUID.randomUUID().toString(), null);
                event.getChannel().sendMessage(msg).queue();
                return;
            }

            //noinspection StringBufferReplaceableByString
            String text = new StringBuilder()
                    .append(event.getAuthor().getAsMention())
                    .append(" rolled: ")
                    .append(roll)
                    .toString();

            LOG.trace("Respond: plugin={}, text='{}'", getClass().getSimpleName(), text);
            Message msg = new DataMessage(false, text, UUID.randomUUID().toString(), null);
            event.getChannel().sendMessage(msg).queue(message -> message.addReaction("➕").queue());
        }
    }

    @Override
    public void work(final GuildMessageReactionAddEvent event) {
        LOG.info("Working on event: {}", event);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", getClass().getSimpleName() + "@" + System.identityHashCode(this) + "[", "]")
                .add("roller=" + roller)
                .toString();
    }
}
