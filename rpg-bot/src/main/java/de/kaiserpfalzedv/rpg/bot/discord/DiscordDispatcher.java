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


import io.quarkus.runtime.StartupEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GenericGuildMessageReactionEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.StringJoiner;

@ApplicationScoped
public class DiscordDispatcher extends DiscordListenerAdapter {
    private static final Logger LOG = LoggerFactory.getLogger(DiscordDispatcher.class);

    @Inject
    Instance<DiscordPlugin> pluginInstances;
    private final ArrayList<DiscordPlugin> plugins = new ArrayList<>();

    void startup(@Observes StartupEvent event) {
        pluginInstances.forEach(plugins::add);

        LOG.info("Discord Dispatcher: plugins: {}", plugins);
    }

    @Override
    public void onMessageReceived(final MessageReceivedEvent event) {
        addMDCInfo(event);

        if (! event.getMessage().getContentRaw().startsWith("/")) {
            return; // do nothing.
        }

        LOG.info("Received message: guild='{}', channel='{}', author='{}', id='{}', message='{}'",
                event.getGuild().getName(),
                event.getChannel().getName(),
                event.getAuthor().getName(),
                event.getMessageId(),
                event.getMessage().getContentRaw()
        );

        for (DiscordPlugin p : plugins) {
            try {
                p.work(event);
            } catch (DiscordPluginException e) {
                LOG.error("Plugin '" + p.getClass().getSimpleName() + "' complains: " + e.getMessage(), e);
            }
        }

        cleanMDC();
    }


    @Override
    public void onGuildMessageReactionAdd(final GuildMessageReactionAddEvent event) {
        addMDCInfo(event);

        if (event.getUser().isBot())
            return;

        for (DiscordPlugin p : plugins) {
            try {
                p.work(event);
            } catch (DiscordPluginException e) {
                LOG.error("Plugin '" + p.getClass().getSimpleName() + "' complains: " + e.getMessage(), e);
            }
        }

        cleanMDC();
    }


    @Override
    public void onGuildMessageReactionRemove(final GuildMessageReactionRemoveEvent event) {
        addMDCInfo(event);

        if (event.getUser().isBot())
            return;

        for (DiscordPlugin p : plugins) {
            try {
                p.work(event);
            } catch (DiscordPluginException e) {
                LOG.error("Plugin '" + p.getClass().getSimpleName() + "' complains: " + e.getMessage(), e);
            }
        }

        cleanMDC();
    }


    /**
     * Adds the information to MDC for logging.
     *
     * @param event event to read information from.
     */
    private void addMDCInfo(final GenericGuildMessageReactionEvent event) {
        MDC.put("message.id", event.getMessageId());

        MDC.put("guild.name", event.getGuild().getName());
        MDC.put("guild.id", event.getGuild().getIconId());

        MDC.put("channel.name", event.getChannel().getName());
        MDC.put("channel.id", event.getChannel().getId());

        MDC.put("user.name", event.getUser().getName());
        MDC.put("user.id", event.getUser().getId());

        LOG.info("Received event: guild='{}', channel='{}', author='{}', message.id='{}', emote='{}'",
                event.getGuild().getName(),
                event.getChannel().getName(),
                event.getUser().getName(),
                event.getMessageId(),
                event.getReaction().getReactionEmote().toString()
        );
    }

    /**
     * Adds the information to MDC for logging.
     *
     * @param event event to read information from.
     */
    private void addMDCInfo(final MessageReceivedEvent event) {
        MDC.put("message.id", event.getMessageId());

        MDC.put("guild.name", event.getGuild().getName());
        MDC.put("guild.id", event.getGuild().getIconId());

        MDC.put("channel.name", event.getChannel().getName());
        MDC.put("channel.id", event.getChannel().getId());

        MDC.put("user.name", event.getAuthor().getName());
        MDC.put("user.id", event.getAuthor().getId());
    }

    /**
     * Removes the MDC for this event.
     */
    private void cleanMDC() {
        MDC.remove("guild.name");
        MDC.remove("guild.id");

        MDC.remove("channel.name");
        MDC.remove("channel.id");

        MDC.remove("user.name");
        MDC.remove("user.id");
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", getClass().getSimpleName() + "@" + System.identityHashCode(this) + "[", "]")
                .toString();
    }
}
