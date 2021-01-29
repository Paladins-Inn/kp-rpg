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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.kaiserpfalzedv.rpg.core.resources.ImmutableResourceHistory;
import de.kaiserpfalzedv.rpg.core.resources.ImmutableResourceMetadata;
import de.kaiserpfalzedv.rpg.core.resources.ImmutableResourceStatus;
import de.kaiserpfalzedv.rpg.integrations.datastore.file.store.FileResource;
import de.kaiserpfalzedv.rpg.integrations.datastore.file.store.ImmutableFileData;
import de.kaiserpfalzedv.rpg.integrations.datastore.file.store.ImmutableFileResource;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.io.IOException;
import java.io.StringWriter;
import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestResource {
    static private final Logger LOG = LoggerFactory.getLogger("TestResource");

    public static final String JSON_STRING = "{\"metadata\":{\"kind\":\"File\",\"apiVersion\":\"v1\",\"namespace\":\"test\",\"name\":\"TestFile\",\"selfLink\":\"/apis/files/v1/namespaces/test/File/7ee3f57b-ed6c-400e-84f4-253afe0c56c0\",\"uid\":\"7ee3f57b-ed6c-400e-84f4-253afe0c56c0\",\"generation\":1,\"created\":\"2021-01-07T18:10:20.168433Z\",\"annotations\":{},\"labels\":{}},\"spec\":{\"size\":0,\"mimeType\":\"text/plain\"},\"status\":{\"observedGeneration\":1,\"history\":[{\"timeStamp\":\"2021-01-07T18:10:20.168433Z\",\"status\":\"created\",\"message\":\"File saved.\"}]}}";
    private static final UUID UID = UUID.fromString("7ee3f57b-ed6c-400e-84f4-253afe0c56c0");
    private static final OffsetDateTime CREATED = OffsetDateTime.parse("2021-01-07T18:10:20.168433Z");

    private final FileResource sut = ImmutableFileResource.builder()
            .metadata(
                    ImmutableResourceMetadata.builder()
                            .kind(FileResource.KIND)
                            .apiVersion(FileResource.API_VERSION)
                            .created(CREATED)
                            .generation(1L)
                            .namespace("test")
                            .name("TestFile")
                            .uid(UID)
                            .selfLink("/apis/files/v1/namespaces/test/" + FileResource.KIND + "/" + UID.toString())
                            .build()
            )
            .spec(
                    ImmutableFileData.builder()
                            .size(0L)
                            .mimeType("text/plain")
                            .build()
            )
            .status(
                ImmutableResourceStatus.<String>builder()
                        .observedGeneration(1L)
                        .addHistory(
                                ImmutableResourceHistory.<String>builder()
                                        .status("created")
                                        .timeStamp(CREATED)
                                        .message("File saved.")
                                        .build()
                        )
                        .build()
            )
            .build();

    private ObjectMapper mapper;

    @Test
    public void ShouldGenerateANiceJsonStringWhenObjectIsGiven() throws IOException {
        MDC.put("test", "serialize-data");

        StringWriter writer = new StringWriter();
        JsonGenerator generator = mapper.createGenerator(writer);
        generator.writeObject(sut);
        String result = writer.toString();

        LOG.trace("result={}", result);
        assertEquals(JSON_STRING, result);
    }

    @Test
    public void ShouldReadTheObjectWhenANiceStringIsGiven() throws JsonProcessingException {
        FileResource result = mapper.readerFor(FileResource.class).readValue(JSON_STRING);

        assertEquals(sut, result);
    }


    @BeforeEach
    void setUpEach() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.registerModule(new Jdk8Module());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.setDateFormat(new StdDateFormat().withColonInTimeZone(true));
    }

    @AfterEach
    void tearDownEach() {
        MDC.remove("test");
    }

    @BeforeAll
    static void setUp() {
        MDC.put("test-class", TestResource.class.getSimpleName());
    }

    @AfterAll
    static void tearDown() {
        MDC.clear();
    }
}
