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

package de.kaiserpfalzedv.rpg.bot.drivethru;

import de.kaiserpfalzedv.commons.core.user.UserStoreService;
import de.kaiserpfalzedv.commons.discord.DiscordPluginCommand;
import de.kaiserpfalzedv.commons.discord.DiscordPluginContext;
import de.kaiserpfalzedv.commons.discord.DiscordPluginException;
import de.kaiserpfalzedv.commons.discord.DontWorkOnDiscordEventException;
import de.kaiserpfalzedv.commons.discord.guilds.Guild;
import de.kaiserpfalzedv.commons.discord.text.DiscordMessageChannelPlugin;
import de.kaiserpfalzedv.rpg.integrations.drivethru.DriveThruRPGService;
import io.quarkus.runtime.StartupEvent;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * The plugin for handling direct commands to DriveThruRPG.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0  2021-02-04
 */
@ApplicationScoped
@ToString
@Slf4j
public class DriveThruRPGPlugin implements DiscordMessageChannelPlugin {
    private static final String PLUGIN_PREFIX = "dtr";

    /**
     * The DriveThruRPG service.
     */
    private final DriveThruRPGService service;
    /**
     * The user store.
     */
    private final UserStoreService userStore;
    /**
     * The commands of this plugin.
     */
    private final List<DriveThruRPGPluginCommand> commands = new ArrayList<>();

    @Inject
    public DriveThruRPGPlugin(
            final UserStoreService userStore,
            final DriveThruRPGService service,
            final Instance<DriveThruRPGPluginCommand> rawCommands
    ) {
        rawCommands.forEach(commands::add);
        this.service = service;
        this.userStore = userStore;
    }

    public void startup(@Observes final StartupEvent event) {
        log.info("Created discord DriveThruRPG plugin {}: service={}, userStore={}, commands={}",
                this, service, userStore, commands);
    }

    @Override
    public void workOnMessage(
            final Guild guild,
            final MessageReceivedEvent event
    ) {
        String message = event.getMessage().getContentRaw();
        String pluginPrefix = loadGuildPrefix(guild) + PLUGIN_PREFIX;

        if (message.startsWith(pluginPrefix)) {
            log.trace("command: message={}", message);
            String arg = message.substring(pluginPrefix.length() + 1);
            String[] args = arg.split(" ", 2);

            for (DiscordPluginCommand c : commands) {
                DiscordPluginContext ctx = DiscordPluginContext.builder()
                        .withPlugin(this)
                        .withGuild(guild)
                        .withChannel(event.getChannel())
                        .withUser(event.getAuthor())
                        .withArgument(c.getArgument(args[1]))
                        .build();

                try {
                    c.execute(ctx);
                } catch (DontWorkOnDiscordEventException e) {
                    // log nothing. Every but one plugin will throw this exception ...
                } catch (DiscordPluginException e) {
                    log.error("PluginCommand '{} {}' threw an exception: {}", pluginPrefix, c.getCommand(), e.getMessage());
                }
            }
        }
    }

    private String loadGuildPrefix(Guild guild) {
        String prefix = DEFAULT_GUILD_PREFIX;
        if (guild.getData().isPresent()) {
            prefix = guild.getData().get().getPrefix();
            if (prefix == null) {
                prefix = "tb!";
            }
        }
        return prefix;
    }

    @Override
    public String getHelpText() {
        StringBuilder result = new StringBuilder("Help for plugin '" + getName() + ":\n\n");

        for (DiscordPluginCommand c : commands) {
            result.append(c.getHelp()).append("\n");
        }

        return result.toString();
    }
}
