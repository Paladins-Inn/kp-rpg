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
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.impl.DataMessage;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionRemoveEvent;
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
            String roll;
            try {
                roll = roll(command.substring(3), event.getAuthor());
            } catch (IllegalArgumentException e) {
                Message msg = new DataMessage(true, event.getAuthor().getAsMention() + " kann nicht richtig würfeln!", UUID.randomUUID().toString(), null);
                event.getChannel().sendMessage(msg).queue();
                return;
            }

            Message msg = new DataMessage(false, roll, UUID.randomUUID().toString(), null);
            event.getChannel().sendMessage(msg).queue(
                    message -> message.addReaction("➕").queue()
            );
        }
    }

    private String roll(final String command, final User user) throws IllegalArgumentException {
        LOG.debug("{} working on: command={}, roller={}", this, command, roller);

        String roll = roller.work(command.substring(3));

        //noinspection StringBufferReplaceableByString
        String text = new StringBuilder()
                .append(user.getAsMention())
                .append(" rolled: ")
                .append(roll)
                .toString();

        LOG.trace("Respond: plugin={}, text='{}'", getClass().getSimpleName(), text);
        return text;
    }

    @Override
    public void work(final GuildMessageReactionAddEvent event) {
        LOG.info("Working on event: {}", event);

        event.getChannel().getMessageById(event.getMessageId()).queue(
                (message) -> {
                    String command = message.getContentRaw();

                    if (command.startsWith("/r ")) {
                        String roll;
                        try {
                            roll = roll(command.substring(3), event.getUser());
                        } catch (IllegalArgumentException e) {
                            Message msg = new DataMessage(true, event.getUser().getAsMention() + " kann nicht richtig würfeln!", UUID.randomUUID().toString(), null);
                            event.getChannel().sendMessage(msg).queue();
                            return;
                        }

                        Message msg = new DataMessage(false, roll, UUID.randomUUID().toString(), null);
                        event.getChannel().sendMessage(msg).queue(
                                m -> m.addReaction("➕").queue()
                        );
                    }
                },
                (failure) -> {
                    LOG.error("Can't read message: {}", failure.getMessage());
                }
        );

        event.getChannel().removeReactionById(event.getReaction().getMessageId(), "➕", event.getUser()).queue();
    }

    @Override
    public void work(final GuildMessageReactionRemoveEvent event) {
        LOG.info("Working on event: {}", event);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", getClass().getSimpleName() + "@" + System.identityHashCode(this) + "[", "]")
                .add("roller=" + roller)
                .toString();
    }
}
