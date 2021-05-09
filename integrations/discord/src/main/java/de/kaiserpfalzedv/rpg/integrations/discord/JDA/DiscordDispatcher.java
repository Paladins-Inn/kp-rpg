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


import de.kaiserpfalzedv.rpg.core.discord.DiscordMessageHandler;
import de.kaiserpfalzedv.rpg.core.resources.ResourceMetadata;
import de.kaiserpfalzedv.rpg.integrations.discord.DiscordPluginNotAllowedException;
import de.kaiserpfalzedv.rpg.integrations.discord.DontWorkOnDiscordEventException;
import de.kaiserpfalzedv.rpg.integrations.discord.IgnoreBotsException;
import de.kaiserpfalzedv.rpg.integrations.discord.guilds.Guild;
import de.kaiserpfalzedv.rpg.integrations.discord.guilds.GuildStoreService;
import de.kaiserpfalzedv.rpg.integrations.discord.text.DiscordMessageChannelPlugin;
import io.quarkus.runtime.StartupEvent;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.MDC;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.UUID;

/**
 * DiscordDispatcher -- The plugin based dispatcher for Discord bots.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.0.0
 */
@Singleton // since ListenerAdapter contains a final method, we need a proxy-less implementation.
@Slf4j
@ToString
public class DiscordDispatcher extends ListenerAdapter {
    /**
     * The plugins to work on.
     */
    private final ArrayList<DiscordMessageChannelPlugin> plugins = new ArrayList<>();

    /**
     * The guild provider to load guilds from.
     */
    private final GuildStoreService store;
    /**
     * The message sender for discord messages.
     */
    private final DiscordMessageHandler sender;

    @Inject
    public DiscordDispatcher(
            final GuildStoreService store,
            final DiscordMessageHandler sender,
            final Instance<DiscordMessageChannelPlugin> plugins
    ) {
        this.store = store;
        this.sender = sender;
        plugins.forEach(this.plugins::add);
    }


    /**
     * Loads the injected instances into the array for later easier access.
     *
     * @param event The startup event.
     */
    void startup(@Observes StartupEvent event) {
        log.info("Discord Dispatcher: plugins: {}", plugins);
    }

    @Override
    public void onMessageReceived(@NotNull final MessageReceivedEvent event) {
        addMDCInfo(event, event.getAuthor());

        Guild guild = retrieveGuild(event);

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
    public void onMessageReactionAdd(@NotNull final MessageReactionAddEvent event) {
        addMDCInfo(event, event.getUser());

        Guild guild = retrieveGuild(event);

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

    private Guild retrieveGuild(@NotNull GenericMessageEvent event) {
        if (event.isFromGuild()) {
            return store
                    .findByNameSpaceAndName(Guild.DISCORD_NAMESPACE, event.getGuild().getName())
                    .orElse(generateNewGuildEntry(event.getGuild().getName()));
        }

        return store
                .findByNameSpaceAndName(Guild.DISCORD_NAMESPACE, "no-guild")
                .orElse(generateNewGuildEntry("no-guild"));
    }

    @NotNull
    private Guild generateNewGuildEntry(final String name) {
        Guild result = Guild.builder()
                .metadata(
                        ResourceMetadata.builder()
                                .kind(Guild.KIND)
                                .apiVersion(Guild.API_VERSION)

                                .namespace(Guild.DISCORD_NAMESPACE)
                                .name(name)
                                .uid(UUID.randomUUID())
                                .generation(0L)
                                .created(OffsetDateTime.now(Clock.systemUTC()))

                                .build()
                )
                .build();

        result = store.save(result);

        log.info("Created: guild={}", result);
        return result;
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
            log.debug("This is no text channel. Can't check permission.");
            return;
        }

        try {
            plugin.checkUserPermission(guild, (TextChannel) channel, user);
        } catch (DiscordPluginNotAllowedException e) {
            if (e.isBlame()) {
                sender.sendDM(user, "Sorry, you are not allowed to use this command: " + e.getMessage());
            }

            log.warn("{}: plugin={}, user={}, blame={}",
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
            final GenericMessageEvent event,
            final User user
    ) {
        MDC.put("message.id", event.getMessageId());

        if (event.isFromGuild()) {
            MDC.put("guild.name", event.getGuild().getName());
            MDC.put("guild.id", event.getGuild().getId());
        }

        MDC.put("channel.name", event.getChannel().getName());
        MDC.put("channel.id", event.getChannel().getId());

        MDC.put("user.name", user.getName());
        MDC.put("user.id", user.getId());

        log.trace("Received event: guild='{}', channel='{}', author='{}', message.id='{}'",
                event.isFromGuild() ? event.getGuild().getName() : "no-guild",
                event.getChannel().getName(),
                user.getName(),
                event.getMessageId()
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
}
