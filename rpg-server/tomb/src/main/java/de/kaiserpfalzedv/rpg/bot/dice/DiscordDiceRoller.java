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
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.internal.entities.DataMessage;
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

    /** Emoji for re-rolling the die roll. */
    private static final String REROLL_EMOJI = "🔁";

    /** Emoji for adding to the current die roll. */
    private static final String ADD_ROLL = "⬆️";
    private static final String ADD_ROLL_A_BIT = "↗️";

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
                LOG.error("Rolling failed: " + e.getMessage(), e);

                Message msg = new DataMessage(true, event.getAuthor().getAsMention() + " kann nicht richtig würfeln!", UUID.randomUUID().toString(), null);
                event.getChannel().sendMessage(msg).queue();
                return;
            }

            // ad the re-roll reaction to the original message.
            event.getMessage().addReaction(REROLL_EMOJI).queue();

            Message msg = new DataMessage(false, roll, UUID.randomUUID().toString(), null);
            event.getChannel().sendMessage(msg).queue();
        }
    }

    private String roll(final String command, final User user) throws IllegalArgumentException {
        LOG.debug("{} working on: command={}, roller={}", this, command, roller);

        String roll = roller.work(command);

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
        String reactionCode = event.getReaction().getReactionEmote().getAsReactionCode();
        LOG.info("Working on event: channel.id={}, message.id={}, user.name={}, reactionCode={}",
                event.getChannel(), event.getMessageId(), event.getUser().getName(), reactionCode);

        if ("RE:U+1f501".contentEquals(reactionCode)) {
            String msgId = event.getReaction().getMessageId();
            event.getChannel().retrieveMessageById(msgId).queue(
                    (message) -> {
                        String command = message.getContentRaw();

                        if (command.startsWith("/r ")) {
                            String roll;
                            try {
                                roll = roll(command.substring(3), event.getUser());
                            } catch (IllegalArgumentException e) {
                                LOG.error("Rolling failed: " + e.getMessage(), e);

                                Message response = new DataMessage(true, event.getUser().getAsMention() + " kann nicht richtig würfeln!", UUID.randomUUID().toString(), null);
                                event.getChannel().sendMessage(response).queue();
                                return;
                            }

                            Message response = new DataMessage(false, roll, UUID.randomUUID().toString(), null);
                            event.getChannel().sendMessage(response).queue();
                        }
                    },
                    (failure) -> {
                        LOG.error("Can't load message: " + failure.getMessage(), failure);

                        Message response = new DataMessage(false, event.getUser().getAsMention() + ": Sorry, can't load this message.", UUID.randomUUID().toString(), null);
                        event.getChannel().sendMessage(response).queue();
                        return;
                    }
            );
        }

        // remove the count on the original re-roll reaction to keep it nice and tidy at 1 ...
        event.getChannel().removeReactionById(event.getReaction().getMessageId(), REROLL_EMOJI, event.getUser()).queue();
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
