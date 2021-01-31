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

package de.kaiserpfalzedv.rpg.integrations.datastore.file;

import com.mongodb.MongoGridFSException;
import com.mongodb.client.gridfs.GridFSBucket;
import org.bson.BsonString;
import org.bson.BsonValue;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;

/**
 * MongoDBFileResources -- The MongoDB file resource implementation.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de.>}
 * @since 1.0.0 2021-01-08
 */
@ApplicationScoped
public class MongoDBFileResources {
    /**
     * The GridFS bucket for storing files.
     */
    @Inject
    GridFSBucket store;

    public void create(final UUID uid, final String fileName, final InputStream data) {
        try {
            store.uploadFromStream(mongofyUUID(uid), fileName, data);
        } catch (MongoGridFSException e) {
            // FIXME 2021-01-31 rlichti Handle the exception
        }
    }

    public Optional<InputStream> retrieve(final UUID uid) {
        ByteArrayOutputStream data = new ByteArrayOutputStream();

        try {
            store.downloadToStream(mongofyUUID(uid), data);
        } catch (MongoGridFSException e) {
            // FIXME 2021-01-31 rlichti Handle the exception
        }

        return Optional.of(new ByteArrayInputStream(data.toByteArray()));
    }

    public void delete(final UUID uid) {
        try {
            store.delete(mongofyUUID(uid));
        } catch (MongoGridFSException e) {
            // FIXME 2021-01-31 rlichti Handle the exception
        }
    }


    /**
     * This is the default UID handling method.
     *
     * @param uid The UUID to be used as GridFS id to a document.
     * @return The GridFS compatible id.
     */
    @SuppressWarnings("SpellCheckingInspection")
    private BsonValue mongofyUUID(final UUID uid) {
        return new BsonString(uid.toString());
    }
}
