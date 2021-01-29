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

package de.kaiserpfalzedv.rpg.integrations.datastore.file.store;

import java.util.UUID;

/**
 * FileCouldNotBeSavedException -- The file could not be saved.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.0.0 2021-01-08
 */
public class FileCouldNotBeSavedException extends FileHandlingException {
    /**
     * @param uid UID of the file resource.
     * @param message the failure message.
     */
    @SuppressWarnings("CdiInjectionPointsInspection")
    public FileCouldNotBeSavedException(final UUID uid, final String message) {
        super(uid, message);
    }

    /**
     * @param uid UID of the file resource.
     * @param cause The failure cause.
     */
    public FileCouldNotBeSavedException(final UUID uid, final Throwable cause) {
        super(uid, cause);
    }

    /**
     * @param uid UID of the file resource.
     * @param message The failure message.
     * @param cause The failure cause.
     */
    public FileCouldNotBeSavedException(final UUID uid, final String message, final Throwable cause) {
        super(uid, message, cause);
    }
}
