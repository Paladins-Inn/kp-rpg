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

import de.kaiserpfalzedv.commons.core.discord.DiscordMessageHandler;
import de.kaiserpfalzedv.commons.discord.JDA.NullDiscordUser;
import de.kaiserpfalzedv.commons.discord.guilds.Guild;
import de.kaiserpfalzedv.commons.discord.text.DiscordMessageChannelPlugin;
import io.quarkus.runtime.StartupEvent;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.internal.entities.DataMessage;
import org.jetbrains.annotations.NotNull;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * de.kaiserpfalzedv.rpg.bot.dice.DiscordDiceRoller -- The discord bridge for rolling dice.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.0.0 2021-01-11
 */
@ApplicationScoped
@ToString
@EqualsAndHashCode
@Slf4j
public class DiscordDiceRoller implements DiscordMessageChannelPlugin {
    /**
     * Emoji for re-rolling the die roll.
     */
    private static final String RE_ROLL_EMOJI = "🔁";
    /**
     * Emoji for adding to the current die roll.
     */
    private static final String ADD_ROLL = "⬆️";
    /**
     * Emoji for
     */
    private static final String ADD_ROLL_A_BIT = "↗️";

    private static final List<String> REACTIONS = new ArrayList<>();

    static {
        REACTIONS.add(RE_ROLL_EMOJI);
        REACTIONS.add(ADD_ROLL);
        REACTIONS.add(ADD_ROLL_A_BIT);
    }

    DiceRoller roller;
    DiscordMessageHandler sender;

    @Inject
    public DiscordDiceRoller(
            final DiceRoller roller,
            final DiscordMessageHandler sender
    ) {
        this.roller = roller;
        this.sender = sender;
    }

    public void startup(@Observes final StartupEvent event) {
        log.info("Created discord roller plugin {}: roller={}", this, roller);
    }

    @Override
    public void workOnMessage(
            final Guild guild,
            final MessageReceivedEvent event
    ) {
        String command = event.getMessage().getContentRaw();

        if (command.startsWith("/r ")) {
            String roll;
            try {
                roll = roll(command.substring(3), event.getAuthor());
            } catch (IllegalArgumentException e) {
                log.error("Rolling failed: " + e.getMessage(), e);

                sendHelp(event.getAuthor(), String.format("Sorry, I don't understand the command: '%s'", command.substring(3)));
                return;
            }

            // ad the re-roll reaction to the original message.
            REACTIONS.forEach(r -> sender.addReactionToEvent(event.getMessage(), r));

            sender.sendTextMessage(event.getTextChannel(), roll);
        }
    }

    private String roll(final String command, final User user) throws IllegalArgumentException {
        log.debug("{} working on: command={}, roller={}", this, command, roller);

        String roll = roller.work(command);

        //noinspection StringBufferReplaceableByString
        String text = new StringBuilder()
                .append(user.getAsMention())
                .append(" rolled: ")
                .append(roll)
                .toString();

        log.trace("Respond: plugin={}, text='{}'", getClass().getSimpleName(), text);
        return text;
    }

    @Override
    public void workOnReaction(
            final Guild guild,
            final GenericMessageReactionEvent event
    ) {
        String reactionCode = event.getReaction().getReactionEmote().toString();

        switch (reactionCode) {
            case "RE:U+1f501": // 🔁
            case "RE:U+2b06U+fe0f": // ⬆
            case "RE:U+2197U+fe0f": // ↗
                reRoll(event);
                break;

            default:
                log.debug("No reaction for '{}' defined.", reactionCode);

                sendHelp(event.getUser(), String.format("Sorry, currently I don't understand the reaction '%s'.", reactionCode));
        }

    }

    @NotNull
    private RestAction<Message> getMessageRestAction(final GenericMessageReactionEvent event) {
        return event.getChannel().retrieveMessageById(event.getReaction().getMessageId());
    }

    private void reRoll(final GenericMessageReactionEvent event) {
        RestAction<Message> action = getMessageRestAction(event);
        User user = event.getUser() != null ? event.getUser() : new NullDiscordUser();

        event.getReaction().removeReaction(user).queue();

        action.queue(
                (message) -> {
                    String command = message.getContentRaw();

                    if (command.startsWith("/r ")) {
                        String roll;
                        try {
                            roll = roll(command.substring(3), user);
                        } catch (IllegalArgumentException e) {
                            log.error("Rolling failed: " + e.getMessage(), e);

                            sendHelp(user, command.substring(3));
                            return;
                        }

                        Message response = new DataMessage(false, roll, UUID.randomUUID().toString(), null);
                        event.getChannel().sendMessage(response).queue();
                    }
                },
                (failure) -> {
                    log.error("Can't load message: " + failure.getMessage(), failure);

                    sendHelp(user, "Can't load message to re-roll the dice roll. Sorry");
                }
        );
    }


    /**
     * Sends a default failure message to the user creating the event.
     *
     * @param user    The user to be informed.
     * @param message The failure message.
     */
    private void sendHelp(final User user, final String message) {
        sender.sendDM(user, message);
    }
}
