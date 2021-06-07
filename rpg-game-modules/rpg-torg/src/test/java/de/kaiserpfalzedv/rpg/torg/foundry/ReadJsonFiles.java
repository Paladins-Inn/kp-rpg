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

package de.kaiserpfalzedv.rpg.torg.foundry;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import de.kaiserpfalzedv.rpg.torg.foundry.actors.FoundryActor;
import de.kaiserpfalzedv.rpg.torg.foundry.actors.FoundryPages;
import de.kaiserpfalzedv.rpg.torg.foundry.items.*;
import de.kaiserpfalzedv.rpg.torg.model.actors.ActorType;
import de.kaiserpfalzedv.rpg.torg.model.items.Item;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * ReadJsonFiles --
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 2.0.0  2021-06-04
 */
@Slf4j
public class ReadJsonFiles {
    private final static ObjectMapper mapper = new ObjectMapper();

    private static FoundryItemMapper mapperFactory;

    @BeforeAll
    protected static void generateMapper() {
        PriceMapper priceMapper = new PriceMapper();

        mapperFactory = new FoundryItemMapper(
                new AttackMapper(priceMapper),
                new ArmorMapper(priceMapper),
                new GearMapper(priceMapper),
                new PerkMapper(priceMapper),
                new AttackAndArmorMapper(priceMapper)
        );

        mapper.registerModule(new JSR310Module());
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ"));
    }

    @Test
    public void shouldReadActorsFromFile() throws IOException {
        List<FoundryActor> result = Arrays.asList(mapper.readValue(Paths.get("./target/classes/te001/Actors.db.json").toFile(), FoundryActor[].class));

        Set<ActorType> type = result.stream().map(FoundryActor::getType).filter(Objects::nonNull).collect(Collectors.toSet());
        log.info("Possible actor types: {}", type);

        log.info("Read actors. count={}", result.size());
    }

    @Test
    public void shouldReadItemsFromFile() throws IOException {
        List<FoundryItem> result = Arrays.asList(mapper.readValue(Paths.get("./target/classes/te001/Items.db.json").toFile(), FoundryItem[].class));
        log.info("Read items. count={}", result.size());

        result.forEach(i -> {
            Item converted = mapperFactory.convert(i);
            String description;
            try {
                description = converted.getData().orElseThrow().getDescription();
            } catch (NullPointerException e) {
                description = "./.";
            }

            try {
                log.debug("Converted item:\n{}", mapper.writeValueAsString(converted));
            } catch (JsonProcessingException e) {
                log.error("{}. item='{}'", e.getMessage(), converted.getSelfLink());
            }
        });
    }

    @Test
    public void shouldReadRulesFromFile() throws IOException {
        List<FoundryPages> result = Arrays.asList(mapper.readValue(Paths.get("./target/classes/te001/core-rules.db.json").toFile(), FoundryPages[].class));

        log.info("Read core rules. count={}", result.size());
    }

    @Test
    public void shouldReadArchetypes() throws IOException {
        List<FoundryActor> result = Arrays.asList(mapper.readValue(Paths.get("./target/classes/data/archetypes.db.json").toFile(), FoundryActor[].class));

        Set<ActorType> type = result.stream().map(FoundryActor::getType).filter(Objects::nonNull).collect(Collectors.toSet());
        log.info("Possible archetype types: {}", type);

        log.info("Read archetypes. count={}", result.size());

        log.info("Actor 0: {}", result.get(1));
    }
}
