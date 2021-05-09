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

package de.kaiserpfalzedv.rpg.test.discord;

import de.kaiserpfalzedv.rpg.core.discord.DiscordMessageHandler;
import io.quarkus.arc.AlternativePriority;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import javax.enterprise.context.Dependent;
import java.util.Optional;

/**
 * DiscordMessageHandlerMock -- a mocked message sender.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0  2021-02-06
 */
@Dependent
@AlternativePriority(100)
public class DiscordMessageHandlerMock implements DiscordMessageHandler {
    private User user;
    private MessageChannel channel;
    private Message message;
    private String text;
    private String reaction;

    public void clear() {
        user = null;
        channel = null;
        message = null;
        text = null;
        reaction = null;
    }

    @Override
    public void sendTextMessage(MessageChannel channel, String text) {
        this.channel = channel;
        this.text = text;
    }

    @Override
    public void sendDM(User user, String text) {
        this.user = user;
        this.text = text;
    }

    @Override
    public void addReactionToEvent(Message message, String reaction) {
        this.message = message;
        this.reaction = reaction;
    }

    public boolean isTextMessageSent() {
        return channel != null && text != null;
    }

    public boolean isDMSent() {
        return user != null && text != null;
    }

    public boolean isReactionToEventSent() {
        return message != null && reaction != null;
    }

    public Optional<User> getUser() {
        return Optional.ofNullable(user);
    }

    public Optional<MessageChannel> getChannel() {
        return Optional.ofNullable(channel);
    }

    public Optional<Message> getMessage() {
        return Optional.ofNullable(message);
    }

    public Optional<String> getText() {
        return Optional.ofNullable(text);
    }

    public Optional<String> getReaction() {
        return Optional.ofNullable(reaction);
    }
}
