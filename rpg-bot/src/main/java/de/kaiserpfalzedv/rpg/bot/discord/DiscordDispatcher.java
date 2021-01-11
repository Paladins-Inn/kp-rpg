/*
 * Copyright 2021 Kaiserpfalz EDV-Service, Roland T. Lichti
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.kaiserpfalzedv.rpg.bot.discord;


import io.quarkus.runtime.StartupEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
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
    public void onMessageReceived(MessageReceivedEvent event) {
        addMDCInfo(event);

        if (! event.getMessage().getContentRaw().startsWith("/")) {
            return; // do nothing.
        }

        LOG.debug("Received message: guild='{}', channel='{}', author='{}', id='{}', message='{}'",
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
