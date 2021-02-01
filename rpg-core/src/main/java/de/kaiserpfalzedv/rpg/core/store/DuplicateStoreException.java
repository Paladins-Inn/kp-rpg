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

package de.kaiserpfalzedv.rpg.core.store;

import de.kaiserpfalzedv.rpg.core.api.BaseSystemException;
import de.kaiserpfalzedv.rpg.core.resources.ResourceMetadata;

/**
 * DuplicateStoreException -- There is already an object with this metadata.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0  2021-01-31
 */
public class DuplicateStoreException extends BaseSystemException {
    private final ResourceMetadata stored;
    private final ResourceMetadata duplicate;

    /**
     * @param stored    the already stored resource metadata.
     * @param duplicate the new resource metadata.
     * @since 1.2.0
     */
    @SuppressWarnings("CdiInjectionPointsInspection")
    public DuplicateStoreException(final ResourceMetadata stored, final ResourceMetadata duplicate) {
        super(String.format("Duplicate element found. resource='%s', nameSpace='%s', name='%s'",
                stored.getKind(), stored.getNamespace(), stored.getName()
        ));

        this.stored = stored;
        this.duplicate = duplicate;
    }

    /**
     * @return the generation stored in the data store.
     */
    @SuppressWarnings("unused")
    public ResourceMetadata getStoredGeneration() {
        return stored;
    }

    /**
     * @return the generation that should be saved.
     */
    @SuppressWarnings("unused")
    public ResourceMetadata getSaveGeneration() {
        return duplicate;
    }
}
