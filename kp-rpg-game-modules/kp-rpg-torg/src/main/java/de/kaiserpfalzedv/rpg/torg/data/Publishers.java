/*
 * Copyright (c) &today.year Kaiserpfalz EDV-Service, Roland T. Lichti
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.kaiserpfalzedv.rpg.torg.data;

import de.kaiserpfalzedv.commons.core.resources.Metadata;
import de.kaiserpfalzedv.rpg.torg.About;
import de.kaiserpfalzedv.rpg.torg.model.Book.Publisher;
import de.kaiserpfalzedv.rpg.torg.model.Book.PublisherData;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Publishers -- A list of known publishers.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0  2021-05-23
 */
public interface Publishers {
    Publisher ULISSES_SPIELE = Publisher.builder()
            .withKind(Publisher.KIND)
            .withApiVersion(Publisher.VERSION)
            .withNameSpace(About.TORG_NAMESPACE)
            .withName("Ulisses Spiele")
            .withUid(UUID.fromString("c6d3efb7-2c99-4ac3-8d9a-fa48b1a597c2"))
            .withGeneration(1L)

            .withMetadata(
                    Metadata.builder()
                            .withCreated(About.DEFAULT_CREATION)
                            .build()
            )
            .withSpec(
                    PublisherData.builder()
                            .withDriveThroughId(3444)
                            .build()
            )
            .build();

    Publisher PALADINS_INN = Publisher.builder()
            .withKind(Publisher.KIND)
            .withApiVersion(Publisher.VERSION)
            .withNameSpace(About.TORG_NAMESPACE)
            .withName("Paladin's Inn")
            .withUid(UUID.fromString("a26b0214-2aca-4f78-bb34-e827319ec8c9"))
            .withGeneration(1L)
            .withMetadata(
                    Metadata.builder()
                            .withCreated(About.DEFAULT_CREATION)
                            .build()
            )
            .build();

    Map<UUID, Publisher> PUBLISHERS = Set.of(ULISSES_SPIELE, PALADINS_INN).stream().collect(Collectors.toMap(Publisher::getUid, Function.identity()));

    /**
     * @param id The UUID of the publisher.
     * @return The publisher with the given ID (if present).
     */
    default Optional<Publisher> findPublisherByUuid(final UUID id) {
        return Optional.ofNullable(PUBLISHERS.get(id));
    }
}
