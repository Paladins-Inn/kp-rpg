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

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * TestPriceMapper --
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.3.0  2021-06-06
 */
@Slf4j
public class TestPriceMapper {
    private final PriceMapper sut = new PriceMapper();

    @Test
    public void shouldMapPricesWithK() {
        assertEquals(10000, sut.parse("10K"));
    }

    @Test
    public void shouldMapPricesWithKAndSpace() {
        assertEquals(10000, sut.parse("10 K"));
    }

    @Test
    public void shouldMapPricesWithKAndPoint() {
        assertEquals(1500, sut.parse("1.5K"));
    }

    @Test
    public void shouldMapPricesWithKAndPointAndSpace() {
        assertEquals(1500, sut.parse("1.5 K"));
    }


    @Test
    public void shouldMapPricesWithM() {
        assertEquals(10000000, sut.parse("10M"));
    }

    @Test
    public void shouldMapPricesWithMAndSpace() {
        assertEquals(10000000, sut.parse("10 M"));
    }

    @Test
    public void shouldMapPricesWithMAndPoint() {
        assertEquals(1500000, sut.parse("1.5M"));
    }

    @Test
    public void shouldMapPricesWithBAndPointAndSpace() {
        assertEquals(1500000000, sut.parse("1.5 B"));
    }

    @Test
    public void shouldMapPricesWithMAndCommas() {
        assertEquals(1500000000, sut.parse("1,500 M"));
    }
}
