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

package de.kaiserpfalzedv.rpg.integrations.discord.text;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.internal.entities.DataMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.util.UUID;

/**
 * DiscordMessageSender -- Sends the message to discord.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.0.0 2021-01-11
 */
@ApplicationScoped
public class DiscordMessageHandlerJDAImpl implements DiscordMessageHandler {
    private static final Logger LOG = LoggerFactory.getLogger(DiscordMessageHandlerJDAImpl.class);

    @Override
    public void sendTextMessage(final MessageChannel channel, final String message) {
        LOG.info("Sending channel message: channel='{}', message='{}'", channel.getName(), message);

        Message msg = new DataMessage(false, message, UUID.randomUUID().toString(), null);
        channel.sendMessage(msg).queue();
    }

    @Override
    public void sendDM(final User user, final String message) {
        LOG.info("Sending DM. user='{}', message='{}'", user.getName(), message);

        user.openPrivateChannel().queue((channel) -> channel.sendMessage(message).queue());
    }

    @Override
    public void addReactionToEvent(final Message message, final String reaction) {
        LOG.info("Adding reaction: channel='{}', message.id='{}, reaction='{}'", message.getChannel().getName(), message.getId(), reaction);

        message.addReaction(reaction).queue();
    }
}
