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

package de.kaiserpfalzedv.rpg.bot.discord;

import de.kaiserpfalzedv.rpg.bot.text.MarkdownConverter;
import net.dv8tion.jda.core.entities.MessageChannel;

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

    @Inject
    MarkdownConverter converter;

    public void sendMessage(final MessageChannel channel, final String message) {
        String converted = converter.convert(message);

        channel.sendMessage(converted);
    }
}
