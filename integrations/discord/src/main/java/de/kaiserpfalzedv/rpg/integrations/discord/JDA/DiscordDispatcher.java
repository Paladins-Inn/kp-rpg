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
import de.kaiserpfalzedv.rpg.integrations.discord.IgnoreBotsException;
import de.kaiserpfalzedv.rpg.integrations.discord.guilds.Guild;
import de.kaiserpfalzedv.rpg.integrations.discord.guilds.GuildProvider;
import de.kaiserpfalzedv.rpg.integrations.discord.text.DiscordMessageChannelPlugin;
import de.kaiserpfalzedv.rpg.integrations.discord.text.DiscordMessageHandler;
import io.quarkus.runtime.StartupEvent;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
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

    /**
     * The plugins to work on.
     */
    private final ArrayList<DiscordMessageChannelPlugin> plugins = new ArrayList<>();
    /**
     * The guild provider to load guilds from.
     */
    private final GuildProvider guildProvider;
    /**
     * The message sender for discord messages.
     */
    private final DiscordMessageHandler sender;

    @Inject
    public DiscordDispatcher(
            final GuildProvider guildProvider,
            final DiscordMessageHandler sender,
            final Instance<DiscordMessageChannelPlugin> plugins
    ) {
        this.guildProvider = guildProvider;
        this.sender = sender;
        plugins.forEach(this.plugins::add);
    }


    /**
     * Loads the injected instances into the array for later easier access.
     *
     * @param event The startup event.
     */
    void startup(@Observes StartupEvent event) {
        LOG.info("Discord Dispatcher: plugins: {}", plugins);
    }

    @Override
    public void onMessageReceived(@NotNull final MessageReceivedEvent event) {
        addMDCInfo(event.getMessageId(), event.getGuild(), event.getChannel(), event.getAuthor());

        Guild guild = guildProvider.retrieve(event.getGuild().getName());

        for (DiscordMessageChannelPlugin p : plugins) {
            try {
                checkForWork(p, guild, event.getChannel(), event.getAuthor());

                p.workOnMessage(guild, event);
            } catch (DontWorkOnDiscordEventException | DiscordPluginNotAllowedException | IgnoreBotsException e) {
                cleanMDC();
                return;
            }
        }

        cleanMDC();
    }

    @Override
    public void onGuildMessageReactionAdd(@NotNull final GuildMessageReactionAddEvent event) {
        addMDCInfo(event.getMessageId(), event.getGuild(), event.getChannel(), event.getUser());

        Guild guild = guildProvider.retrieve(event.getGuild().getName());

        for (DiscordMessageChannelPlugin p : plugins) {
            try {
                checkForWork(p, guild, event.getChannel(), event.getUser());

                p.workOnReaction(guild, event);
            } catch (DontWorkOnDiscordEventException | DiscordPluginNotAllowedException | IgnoreBotsException e) {
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
     */
    private void checkForWork(
            final DiscordMessageChannelPlugin plugin,
            final Guild guild,
            final MessageChannel channel,
            final User user
    ) throws DontWorkOnDiscordEventException, DiscordPluginNotAllowedException, IgnoreBotsException {
        if (!(channel instanceof TextChannel)) {
            LOG.debug("This is no text channel. Can't check permission.");
            return;
        }

        try {
            plugin.checkUserPermission(guild, (TextChannel) channel, user);
        } catch (DiscordPluginNotAllowedException e) {
            if (e.isBlame()) {
                sender.sendDM(user, "Sorry, you are not allowed to use this command: " + e.getMessage());
            }

            LOG.warn("{}: plugin={}, user={}, blame={}",
                    e.getMessage(), plugin.getName(), user.getName(), e.isBlame());

            throw e;
        }
    }


    /**
     * Adds the information to MDC for logging.
     *
     * @param user user since depending on the event type, the user is in different properties.
     */
    private void addMDCInfo(
            final String messageId,
            final net.dv8tion.jda.api.entities.Guild guild,
            final MessageChannel channel,
            final User user
    ) {
        MDC.put("message.id", messageId);

        MDC.put("guild.name", guild.getName());
        MDC.put("guild.id", guild.getId());

        MDC.put("channel.name", channel.getName());
        MDC.put("channel.id", channel.getId());

        MDC.put("user.name", user.getName());
        MDC.put("user.id", user.getId());

        LOG.trace("Received event: guild='{}', channel='{}', author='{}', message.id='{}'",
                guild.getName(),
                channel.getName(),
                user.getName(),
                messageId
        );
    }


    /**
     * Removes the MDC for this event.
     */
    private void cleanMDC() {
        MDC.remove("message.id");

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
                .add("identity=" + System.identityHashCode(this))
                .toString();
    }
}
