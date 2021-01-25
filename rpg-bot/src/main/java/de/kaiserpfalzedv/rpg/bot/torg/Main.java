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

package de.kaiserpfalzedv.rpg.bot.torg;


import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import javax.security.auth.login.LoginException;

@Singleton
public class Main extends ListenerAdapter {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    @ConfigProperty(name = "discord.token", defaultValue = "")
    String discordToken;

    JDA bot;

    @PostConstruct
    void initializeBot() throws LoginException {
        JDABuilder builder = new JDABuilder(AccountType.BOT);
        builder.setToken(discordToken);
        builder.addEventListeners(this);
        bot = builder.build();

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
