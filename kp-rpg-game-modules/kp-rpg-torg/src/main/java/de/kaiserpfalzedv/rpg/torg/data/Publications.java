/*
 * Copyright (c) 2022 Kaiserpfalz EDV-Service, Roland T. Lichti
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
import de.kaiserpfalzedv.commons.core.resources.Pointer;
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
            .metadata(
                    Metadata.builder()
                            .identity(
                                    Pointer.builder()
                                            .kind(Publication.KIND)
                                            .apiVersion(Publication.VERSION)
                                            .nameSpace(About.TORG_NAMESPACE)
                                            .name("Torg Core Rules")
                                            .build()
                            )
                            .uid(UUID.fromString("dc1de20e-c19b-49a7-873e-45fd0ce91e73"))
                            .generation(1)
                            .created(About.DEFAULT_CREATION)
                            .owner(Publishers.ULISSES_SPIELE.toPointer())

                            .build()
            )
            .spec(
                    PublicationData.builder()
                            .displayTitle("Torg Core Rules")
                            .title("Torg Eternity - Core Rules")
                            .orderId("UNA10000")
                            .driveThroughId(216248)
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
