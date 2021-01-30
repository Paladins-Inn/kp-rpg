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

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * DiscordMessageSender -- Sends the message to discord.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.0.0 2021-01-11
 */
@ApplicationScoped
public class DiscordMessageSender {
    public void sendTextMessage(final MessageChannel channel, final String message) {
        channel.sendMessage(message);
    }

    public void sendDM(final User user, final String message) {
        user.openPrivateChannel().queue((channel) -> {
           channel.sendMessage(message).queue();
        });
    }
}
