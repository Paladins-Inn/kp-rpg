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

package de.kaiserpfalzedv.rpg.bot.discord.dice;

import de.kaiserpfalzedv.rpg.bot.discord.DiscordDispatcher;
import io.quarkus.runtime.StartupEvent;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.security.auth.login.LoginException;
import java.util.StringJoiner;

@Singleton
public class DiscordBotConnector {
    private static final Logger LOG = LoggerFactory.getLogger(DiscordBotConnector.class);

    private static JDA bot;

    @ConfigProperty(name = "discord.token", defaultValue = "")
    String discordToken;

    @Inject
    DiscordDispatcher dispatcher;

    void startup(@Observes StartupEvent event) {
        JDABuilder builder = new JDABuilder(AccountType.BOT);
        builder.setToken(discordToken);
        builder.addEventListener(dispatcher);

        try {
            bot = builder.buildAsync();
        } catch (LoginException e) {
            LOG.error("Could not login to Discord: " + e.getMessage(), e);

            throw new IllegalStateException("Login to Discord failed: " + e.getMessage());
        }

        LOG.info("Created Discord connect: {}", bot.asBot().getInviteUrl());
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", getClass().getSimpleName() + "@" + System.identityHashCode(this) + "[", "]")
                .add("dispatcher=" + dispatcher)
                .toString();
    }
}
