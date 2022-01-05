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
import de.kaiserpfalzedv.rpg.torg.model.Book.Publication;
import de.kaiserpfalzedv.rpg.torg.model.Book.PublicationData;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

/**
 * Publications -- The known publications
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0  2021-05-23
 */
public interface Publications {
    Publication CORE_RULES = Publication.builder()
            .withKind(Publication.KIND)
            .withApiVersion(Publication.VERSION)
            .withNameSpace(About.TORG_NAMESPACE)
            .withName("Torg Core Rules")
            .withUid(UUID.fromString("dc1de20e-c19b-49a7-873e-45fd0ce91e73"))
            .withGeneration(1L)

            .withMetadata(
                    Metadata.builder()
                            .withCreated(About.DEFAULT_CREATION)
                            .withOwner(Publishers.ULISSES_SPIELE)

                            .build()
            )
            .withSpec(
                    PublicationData.builder()
                            .withDisplayTitle("Torg Core Rules")
                            .withTitle("Torg Eternity - Core Rules")
                            .withOrderId("UNA10000")
                            .withDriveThroughId(216248)
                            .build()
            )
            .build();


    HashMap<UUID, Publication> PUBLICATIONS = new HashMap<>();

    /**
     * @param id The UUID of the publication.
     * @return The publication with the given ID (if present).
     */
    default Optional<Publication> findPublicationByUuid(final UUID id) {
        if (PUBLICATIONS.isEmpty()) {
            PUBLICATIONS.put(CORE_RULES.getUid(), CORE_RULES);
        }
        return Optional.ofNullable(PUBLICATIONS.get(id));
    }
}
