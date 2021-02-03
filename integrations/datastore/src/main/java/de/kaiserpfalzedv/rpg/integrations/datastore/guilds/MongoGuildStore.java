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

package de.kaiserpfalzedv.rpg.integrations.datastore.guilds;

import de.kaiserpfalzedv.rpg.integrations.datastore.resources.MongoResourceStore;
import de.kaiserpfalzedv.rpg.integrations.discord.guilds.Guild;
import de.kaiserpfalzedv.rpg.integrations.discord.guilds.GuildStoreService;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import io.quarkus.mongodb.panache.PanacheQuery;
import io.quarkus.panache.common.Parameters;
import io.quarkus.runtime.StartupEvent;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

/**
 * MongoGuildRepository -- The persistent datastore for guild configurations.
 */
@ApplicationScoped
public class MongoGuildStore extends MongoResourceStore<Guild, MongoGuild> implements GuildStoreService, PanacheMongoRepository<MongoGuild> {


    public void startUp(@Observes StartupEvent event) {
        LOG.info("started: {}", this);
    }

    @PostConstruct
    @Override
    public MongoGuildStore setUp() {
        super.setUp();

        return this;
    }

    @PreDestroy
    @Override
    public void tearDown() {
        super.tearDown();
    }

    public MongoGuild empty() {
        return new MongoGuild();
    }

    @Override
    public PanacheQuery<MongoGuild> query(final String query, final Parameters parameters) {
        PanacheQuery<MongoGuild> result = find(query, parameters);

        LOG.trace("query: query='{}', count={}", query, result.count());
        return result;
    }
}
