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

/**
 * de.kaiserpfalzedv.rpg.bot.discord -- The technical parts of the discord bot.
 *
 * <p>This package contains the technical gear of the discord connector. It connects to discord
 * and dispatches all discord bot messages via {@link de.kaiserpfalzedv.rpg.integrations.discord.JDA.DiscordDispatcher} to all
 * services implementing the {@link de.kaiserpfalzedv.rpg.integrations.discord.text.DiscordMessageChannelPlugin}.</p>
 *
 * <p>It's a fire-and-forget usage of the plugin. Every plugin gets the message and decides how to react on it. Handling
 * the outgoing message is the responsibility of the plugin.</p>
 *
 * <p>The {@link de.kaiserpfalzedv.rpg.integrations.discord.JDA.DiscordLivenessCheck} will end in the liveness check of the quarkus
 * app.</p>
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.0.0 2021-01-08
 */
package de.kaiserpfalzedv.rpg.bot.discord;