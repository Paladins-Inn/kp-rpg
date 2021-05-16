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

package de.kaiserpfalzedv.rpg.integrations.drivethru;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.kaiserpfalzedv.rpg.integrations.drivethru.model.Product;
import de.kaiserpfalzedv.rpg.integrations.drivethru.model.ProductMessage;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TestJsonConversion --
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 0.3.0  2021-05-16
 */
@Slf4j
public class TestJsonConversion {
    private static final Logger LOG = LoggerFactory.getLogger(TestJsonConversion.class);

    @Test
    public void shouldCOnvert() throws JsonProcessingException {
        ProductMessage source = ProductMessage.builder()
                .withStatus("success")
                .withMessage(
                        Product.builder()
                                .withProductsId("1")
                                .withProductsName("Name 1")
                                .withPublisherId("1")
                                .withPublisherName("Publisher 1")
                                .withCoverURL("https://url")
                                .build()
                )
                .build();

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(source);
        ProductMessage target = mapper.readValue(json, ProductMessage.class);

        log.info("source={}, json='{}', target={}", source, json, target);
    }
}
