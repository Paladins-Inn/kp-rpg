/*
 * Copyright (c) 2022 Kaiserpfalz EDV-Service, Roland T. Lichti.
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

/**
 * TestJsonConversion --
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 0.3.0  2021-05-16
 */
@Slf4j
public class TestJsonConversion {
    @Test
    public void shouldConvert() throws JsonProcessingException {
        ProductMessage source = ProductMessage.builder()
                .status("success")
                .message(
                        Product.builder()
                                .productsId("1")
                                .productsName("Name 1")
                                .publisherId("1")
                                .publisherName("Publisher 1")
                                .coverURL("https://url")
                                .build()
                )
                .build();

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(source);
        ProductMessage target = mapper.readValue(json, ProductMessage.class);

        log.info("source={}, json='{}', target={}", source, json, target);
    }
}
