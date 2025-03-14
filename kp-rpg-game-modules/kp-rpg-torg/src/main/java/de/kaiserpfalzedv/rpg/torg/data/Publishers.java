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

import de.kaiserpfalzedv.commons.core.resources.MetadataImpl;
import de.kaiserpfalzedv.commons.core.resources.PointerImpl;
import de.kaiserpfalzedv.rpg.core.Books.Publisher;
import de.kaiserpfalzedv.rpg.core.Books.PublisherData;
import de.kaiserpfalzedv.rpg.torg.About;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Publishers -- A list of known publishers.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0  2021-05-23
 */
public interface Publishers {
    Publisher ULISSES_SPIELE = Publisher.builder()
            .metadata(MetadataImpl.builder()
                    .identity(PointerImpl.builder()
                            .kind(Publisher.KIND)
                            .apiVersion(Publisher.VERSION)
                            .nameSpace(About.TORG_NAMESPACE)
                            .name("Ulisses Spiele")
                            .build()
                    )
                    .created(About.DEFAULT_CREATION)
                    .uid(UUID.fromString("c6d3efb7-2c99-4ac3-8d9a-fa48b1a597c2"))
                    .generation(1)
                    .build()
            )
            .spec(
                    PublisherData.builder()
                            .driveThroughId(3444)
                            .build()
            )
            .build();

    Publisher PALADINS_INN = Publisher.builder()
            .metadata(MetadataImpl.builder()
                    .identity(PointerImpl.builder()
                            .kind(Publisher.KIND)
                            .apiVersion(Publisher.VERSION)
                            .nameSpace(About.TORG_NAMESPACE)
                            .name("Paladin's Inn")
                            .build()
                    )
                    .created(About.DEFAULT_CREATION)
                    .uid(UUID.fromString("a26b0214-2aca-4f78-bb34-e827319ec8c9"))
                    .generation(1)
                    .build()
            )
            .build();

    Map<UUID, Publisher> PUBLISHERS = Stream.of(ULISSES_SPIELE, PALADINS_INN).collect(Collectors.toMap(Publisher::getUid, Function.identity()));

    /**
     * @param id The UUID of the publisher.
     * @return The publisher with the given ID (if present).
     */
    default Optional<Publisher> findPublisherByUuid(final UUID id) {
        return Optional.ofNullable(PUBLISHERS.get(id));
    }
}
