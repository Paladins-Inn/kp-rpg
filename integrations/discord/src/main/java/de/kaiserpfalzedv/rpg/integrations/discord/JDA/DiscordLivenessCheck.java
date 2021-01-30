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

package de.kaiserpfalzedv.rpg.integrations.discord.JDA;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * The liveness check for the discord bot.
 *
 * If the liveness check using {@link DiscordBot#discordOK()} fails, the liveness is reported as down.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.0.0 2021-01-08
 */
@Liveness
@ApplicationScoped
public class DiscordLivenessCheck implements HealthCheck {
    @Inject
    DiscordBot bot;

    @Override
    public HealthCheckResponse call() {
        if (bot.discordOK()) {
            return HealthCheckResponse.up(DiscordBot.SERVICE_NAME);
        } else {
            return HealthCheckResponse.down(DiscordBot.SERVICE_NAME);
        }
    }
}
