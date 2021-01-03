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

package de.kaiserpfalzedv.rpg.bot;


import de.kaiserpfalzedv.rpg.bot.dice.Roller;
import io.quarkus.runtime.StartupEvent;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.security.auth.login.LoginException;

@ApplicationScoped
public class Discord extends ListenerAdapter {
    private static final Logger LOG = LoggerFactory.getLogger(Discord.class);

    @ConfigProperty(name = "discord.token", defaultValue = "")
    String discordToken;

    JDA bot;

    BotPlugin[] plugins = {new Roller()};

    private long requests = 0;

    void startup(@Observes StartupEvent event) throws LoginException {
        JDABuilder builder = new JDABuilder(AccountType.BOT);
        builder.setToken(discordToken);
        builder.addEventListener(this);
        bot = builder.buildAsync();

        LOG.info("Created BOT: bot={}, plugins.count={}", bot.asBot().getInviteUrl(), plugins.length);

        for (BotPlugin p : plugins) {
            LOG.debug("- Plugin: {}", p);
        }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        LOG.debug("Received message: channel.id='{}', from='{}', message='{}'",
                event.getTextChannel().getId(),
                event.getAuthor().getName(),
                event.getMessage().getContentRaw()
        );

        requests++;

        for (BotPlugin p : plugins) {
            p.work(event);
        }
    }

    public long getRequests() {
        return requests;
    }
}
