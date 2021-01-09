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

package de.kaiserpfalzedv.rpg.core.mongodb;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.output.Slf4jLogConsumer;

import java.util.HashMap;
import java.util.Map;

public class MongoDBResource implements QuarkusTestResourceLifecycleManager {
    private static final Logger LOG = LoggerFactory.getLogger(MongoDBResource.class);

    private static final String MONGODB_CONTAINER_NAME = "mongo:4.0.10";
    private static final int MONGO_PORT = 27017;

    private static final String USER = "admin";
    private static final String PASSWORD = "admin";
    private static final String DATABASE = "admin";

    private static final Network network = Network.newNetwork();
    private final GenericContainer container = createMongoDbReplicaContainer("M1", "rs0");

    @Override
    public Map<String, String> start() {
        container.start();
        LOG.info("Port bindings: {}", container.getMappedPort(MONGO_PORT));

        Map<String, String> result = new HashMap<>();
        result.put("quarkus.mongodb.connection-string","mongodb://localhost:" + container.getMappedPort(MONGO_PORT));
        result.put("quarkus.mongodb.database", DATABASE);
        result.put("quarkus.mongodb.credentials.username", USER);
        result.put("quarkus.mongodb.credentials.password", PASSWORD);

        LOG.info("Result: {}", result);
        return result;
    }

    private GenericContainer createMongoDbReplicaContainer(final String networkAlias, final String rs) {
        return new GenericContainer(MONGODB_CONTAINER_NAME)
                .withNetwork(network)
                .withNetworkAliases(networkAlias)
                .withExposedPorts(MONGO_PORT)
                .withEnv("MONGO_INITDB_DATABASE", DATABASE)
                .withEnv("MONGO_INITDB_ROOT_USERNAME", USER)
                .withEnv("MONGO_INITDB_ROOT_PASSWORD", PASSWORD)
                .withCommand("--bind_ip localhost," + networkAlias)
                .withLogConsumer(new Slf4jLogConsumer(LOG));
    }

    @Override
    public void stop() {
        container.close();
    }
}
