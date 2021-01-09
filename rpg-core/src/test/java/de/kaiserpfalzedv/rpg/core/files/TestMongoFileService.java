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

import de.kaiserpfalzedv.rpg.core.dice.TestDice;
import de.kaiserpfalzedv.rpg.core.files.mongodb.MongoDBFileResources;
import de.kaiserpfalzedv.rpg.core.mongodb.MongoDBResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.inject.Inject;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@QuarkusTest
@QuarkusTestResource(MongoDBResource.class)
public class TestMongoFileService {
    private static final Logger LOG = LoggerFactory.getLogger(TestMongoFileService.class);

    private static final UUID UID = UUID.randomUUID();
    private static final String FILENAME = "testfile";

    @Inject
    MongoDBFileResources sut;

    private InputStream data;

    @Test
    public void shouldSaveAndRetrieveANewFile() throws FileCouldNotBeSavedException {
        sut.create(UID, FILENAME, data);

        try {
            InputStream outData = sut.retrieve(UID);
            String result = new String(outData.readAllBytes());

            Assertions.assertTrue(result.contains("Kaiserpfalz EDV-Service"));
        } catch (FileNotFoundException|IOException e) {
            Assertions.fail("Can't load the data back!");
        }
    }

    @Test
    public void shouldSaveAndDeleteANewFile() throws FileCouldNotBeSavedException, FileCouldNotBeDeletedException {
        sut.create(UID, FILENAME, data);

        sut.delete(UID);
    }

    @BeforeEach
    void setUpEach() throws java.io.FileNotFoundException {
        data = new FileInputStream("target/test-classes/log4j2.xml");
    }

    @AfterEach
    void tearDownEach() {
        try {
            data.close();
        } catch (IOException e) {
            LOG.warn("Can't close input stream!");

            data = null;
        }

        MDC.remove("test");
    }

    @BeforeAll
    static void setUp() {
        MDC.put("test-class", TestDice.class.getSimpleName());
    }

    @AfterAll
    static void tearDown() {
        MDC.clear();
    }
}
