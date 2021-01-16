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

package de.kaiserpfalzedv.rpg.core.files.store;

import com.mongodb.MongoGridFSException;
import com.mongodb.client.gridfs.GridFSBucket;
import de.kaiserpfalzedv.rpg.core.files.FileCouldNotBeDeletedException;
import de.kaiserpfalzedv.rpg.core.files.FileCouldNotBeSavedException;
import de.kaiserpfalzedv.rpg.core.files.FileNotFoundException;
import de.kaiserpfalzedv.rpg.core.files.FileResourceService;
import org.bson.BsonString;
import org.bson.BsonValue;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.UUID;

/**
 * MongoDBFileResources -- The MongoDB file resource implementation.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de.>}
 * @since 1.0.0 2021-01-08
 */
@ApplicationScoped
public class MongoDBFileResources implements FileResourceService {

    /**
     * The GridFS bucket for storing files.
     */
    @Inject
    GridFSBucket store;

    @Override
    public void create(final UUID uid, final String fileName, final InputStream data) throws FileCouldNotBeSavedException {
        try {
            store.uploadFromStream(mongofyUUID(uid), fileName, data);
        } catch (MongoGridFSException e) {
            throw new FileCouldNotBeSavedException(uid, e.getMessage(), e.getCause());
        }
    }

    @Override
    public InputStream retrieve(final UUID uid) throws FileNotFoundException {
        ByteArrayOutputStream data = new ByteArrayOutputStream();

        try {
            store.downloadToStream(mongofyUUID(uid), data);
        } catch (MongoGridFSException e) {
            throw new FileNotFoundException(uid, e.getCause());
        }

        return new ByteArrayInputStream(data.toByteArray());
    }

    @Override
    public void delete(final UUID uid) throws FileCouldNotBeDeletedException {
        try {
            store.delete(mongofyUUID(uid));
        } catch (MongoGridFSException e) {
            throw new FileCouldNotBeDeletedException(uid, e.getCause());
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
