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


import de.kaiserpfalzedv.rpg.integrations.discord.DiscordPluginNotAllowedException;
import de.kaiserpfalzedv.rpg.integrations.discord.DontWorkOnDiscordEventException;
import de.kaiserpfalzedv.rpg.integrations.discord.guilds.Guild;
import de.kaiserpfalzedv.rpg.integrations.discord.guilds.GuildProvider;
import de.kaiserpfalzedv.rpg.integrations.discord.text.DiscordMessageHandler;
import de.kaiserpfalzedv.rpg.integrations.discord.text.DiscordTextChannelPlugin;
import io.quarkus.runtime.StartupEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GenericGuildMessageReactionEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.StringJoiner;

/**
 * DiscordDispatcher -- The plugin based dispatcher for Discord bots.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.0.0
 */
@Singleton // since ListenerAdapter contains a final method, we need a proxy-less implementation.
public class DiscordDispatcher extends ListenerAdapter {
    private static final Logger LOG = LoggerFactory.getLogger(DiscordDispatcher.class);

    @Inject
    Instance<DiscordTextChannelPlugin> pluginInstances;
    private final ArrayList<DiscordTextChannelPlugin> plugins = new ArrayList<>();

    @Inject
    GuildProvider guildProvider;

    @Inject
    DiscordMessageHandler sender;


    /**
     * Loads the injected instances into the array for later easier access.
     *
     * @param event The startup event.
     */
    void startup(@Observes StartupEvent event) {
        pluginInstances.forEach(plugins::add);

        LOG.info("Discord Dispatcher: plugins: {}", plugins);
    }

    @Override
    public void onMessageReceived(@NotNull final MessageReceivedEvent event) {
        addMDCInfo(event);

        if (event.getAuthor().isBot()) {
            LOG.info("Ignoring bot: bot={}", event.getAuthor());
            return;
        }

        Guild guild = guildProvider.retrieve(event.getGuild().getName());

        for (DiscordTextChannelPlugin p : plugins) {
            try {
                checkForWork(p, guild, event);

                p.workOn(guild, event);
            } catch (DontWorkOnDiscordEventException | DiscordPluginNotAllowedException e) {
                cleanMDC();
                return;
            }
        }

        cleanMDC();
    }

    @Override
    public void onGuildMessageReactionAdd(@NotNull final GuildMessageReactionAddEvent event) {
        addMDCInfo(event);

        if (event.getUser().isBot()) {
            LOG.info("Ignoring bot: bot={}", event.getUser());
            return;
        }

        Guild guild = guildProvider.retrieve(event.getGuild().getName());

        for (DiscordTextChannelPlugin p : plugins) {
            try {
                checkForWork(p, guild, event);

                p.workOn(guild, event);
            } catch (DontWorkOnDiscordEventException | DiscordPluginNotAllowedException e) {
                cleanMDC();
                return;
            }
        }

        cleanMDC();
    }


    /**
     * Checks if the plugin should be called on this event.
     *
     * @param plugin The plugin to check this event on.
     * @param event  The event to be checked against the plugin.,
     */
    private void checkForWork(final DiscordTextChannelPlugin plugin, final Guild guild, final MessageReceivedEvent event)
            throws DontWorkOnDiscordEventException, DiscordPluginNotAllowedException {
        try {
            plugin.checkUserPermission(guild, event.getTextChannel(), event.getAuthor());
        } catch (DiscordPluginNotAllowedException e) {
            if (e.isBlame()) {
                sender.sendDM(
                        event.getAuthor(),
                        "Sorry, you are not allowed to use this command: " + e.getMessage()
                );
            }

            LOG.warn("{}: plugin={}, user={}, blame={}",
                    e.getMessage(), plugin.getName(), event.getAuthor().getName(), e.isBlame());

            throw e;
        }
    }


    /**
     * Checks if the plugin should be called on this event.
     *
     * @param plugin The plugin to check this event on.
     * @param event  The event to be checked against the plugin.,
     */
    private void checkForWork(final DiscordTextChannelPlugin plugin, final Guild guild, final GenericGuildMessageReactionEvent event)
            throws DontWorkOnDiscordEventException, DiscordPluginNotAllowedException {
        try {
            plugin.checkUserPermission(guild, event.getChannel(), event.getUser());
        } catch (DiscordPluginNotAllowedException e) {
            if (e.isBlame() && event.getUser() != null) {
                sender.sendDM(
                        event.getUser(),
                        "Sorry, you are not allowed to use this command: " + e.getMessage()
                );
            }

            if (event.getUser() != null) {
                LOG.warn("{}: plugin={}, user={}, blame={}",
                        e.getMessage(), plugin.getName(), event.getUser().getName(), e.isBlame());
            }

            throw e;
        }
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

        MDC.put("user.name", event.getUser() != null ? event.getUser().getName() : "-no-name-");
        MDC.put("user.id", event.getUser().getId());

        LOG.trace("Received event: guild='{}', channel='{}', author='{}', message.id='{}', emote='{}'",
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


        LOG.trace("Received event: guild='{}', channel='{}', author='{}', message.id='{}'",
                event.getGuild().getName(),
                event.getChannel().getName(),
                event.getAuthor().getName(),
                event.getMessageId()
        );
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
