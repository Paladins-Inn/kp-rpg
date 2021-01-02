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


import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import javax.security.auth.login.LoginException;

@Singleton
public class Discord extends ListenerAdapter {
    private static final Logger LOG = LoggerFactory.getLogger(Discord.class);

    @ConfigProperty(name = "discord.token", defaultValue = "")
    String discordToken;

    JDA bot;

    @PostConstruct
    void initializeBot() throws LoginException {
        JDABuilder builder = new JDABuilder(AccountType.BOT);
        builder.setToken(discordToken);
        builder.addEventListener(this);
        bot = builder.buildAsync();

        LOG.trace("Created BOT: token={}", discordToken);
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        LOG.debug("Received message: topic={}, from={}, message={}",
                event.getTextChannel().getTopic(),
                event.getAuthor().getName(),
                event.getMessage().getContentDisplay()
        );

        if (event.getMessage().getContentRaw().equals("!ping")) {
            event.getChannel().sendMessage("/tts pong!");
        }
    }
}
