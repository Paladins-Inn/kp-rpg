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

package de.kaiserpfalzedv.rpg.core.files;

import java.io.InputStream;
import java.util.UUID;

/**
 * This is the interface of the file resource handling service. This interface encapsulates the technical details of the
 * datastore.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.0.0 2021-01-08
 */
public interface FileResourceService {
    /**
     * Creates a file resource.
     *
     * @param uid The uid of the file resource.
     * @param fileName The filename of the file data.
     * @param data The inputstream contains the file data.
     * @throws DuplicateFileException The file with this UID or namespace/name already exists.
     * @throws FileCouldNotBeSavedException Another problem occured while storing the file resource.
     */
    void create(final UUID uid, final String fileName, InputStream data) throws FileCouldNotBeSavedException, DuplicateFileException;

    /**
     * Retrieves a file resource by its UID.
     *
     * @param id The UID to retrieve
     * @throws FileNotFoundException The file resource could not be found.
     * @return
     */
    InputStream retrieve(final UUID id) throws FileNotFoundException;

    /**
     * Deletes the file resource by UID. If the file resource does not exists, nothing will be reported.
     *
     * @param id The UID of the file resource do be deleted.
     * @throws FileCouldNotBeDeletedException A problem occurred while deleting the file resource.
     */
    void delete(final UUID id) throws FileCouldNotBeDeletedException;
}
