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

package de.kaiserpfalzedv.rpg.integrations.datastore.store;

import de.kaiserpfalzedv.rpg.core.api.BaseException;

import java.util.StringJoiner;
import java.util.UUID;

/**
 * FileHandlingException -- Some problems occurred while handling file resources.
 *
 * Please use one of the derived classes.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.0.0 2021-01-08
 */
public abstract class FileHandlingException extends BaseException {
    /**
     * UID of the file resource this exception is generated for.
     */
    final UUID uid;

    /**
     * @param uid UID of the file resource.
     * @param message the failure message.
     */
    @SuppressWarnings("CdiInjectionPointsInspection")
    public FileHandlingException(final UUID uid, final String message) {
        super(message);

        this.uid = uid;
    }

    /**
     * @param uid UID of the file resource.
     * @param cause the failure cause.
     */
    public FileHandlingException(final UUID uid, final Throwable cause) {
        super(cause.getMessage(), cause);

        this.uid = uid;
    }

    /**
     * @param uid UID of the file resource.
     * @param message the failure message.
     * @param cause the failure cause.
     */
    public FileHandlingException(final UUID uid, final String message, final Throwable cause) {
        super(message, cause);

        this.uid = uid;
    }


    /**
     * @return the UID of the file resource with problems.
     */
    public UUID getUid() {
        return uid;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", getClass().getSimpleName() + "@" + System.identityHashCode(this) + "[", "]")
                .add("uid=" + uid)
                .toString();
    }
}
