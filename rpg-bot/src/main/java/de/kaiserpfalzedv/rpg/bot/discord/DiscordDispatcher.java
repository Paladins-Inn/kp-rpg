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
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.StringJoiner;

@ApplicationScoped
public class DiscordDispatcher extends DiscordListenerAdapter {
    private static final Logger LOG = LoggerFactory.getLogger(DiscordDispatcher.class);

    @Inject
    Instance<DiscordPlugin> pluginInstances;
    final ArrayList<DiscordPlugin> plugins = new ArrayList<>();

    private long requests = 0;
    private long errors = 0;

    void startup(@Observes StartupEvent event) throws LoginException {
        this.pluginInstances.forEach(plugins::add);

        LOG.info("Created Discord Dispatcher: plugins={}", plugins);
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        LOG.debug("Received message: channel.id='{}', from='{}', message='{}'",
                event.getTextChannel().getId(),
                event.getAuthor().getName(),
                event.getMessage().getContentRaw()
        );

        requests++;

        for (DiscordPlugin p : plugins) {
            try {
                p.work(event);
            } catch (Exception e) {
                errors++;

                LOG.error("plugin " + p + " threw exception: " + e.getMessage(), e);
            }
        }
    }

    public long getRequests() {
        return requests;
    }

    public long getErrors() {
        return errors;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", getClass().getSimpleName() + "@" + System.identityHashCode(this) + "[", "]")
                .add("plugins=" + plugins)
                .add("requests=" + requests)
                .add("errors=" + errors)
                .toString();
    }
}
