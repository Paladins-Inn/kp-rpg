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

package de.kaiserpfalzedv.rpg.bot.dice;

import io.quarkus.test.junit.QuarkusTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests the dice roller.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.0.0 2021-01-11
 */
@QuarkusTest
@Slf4j
public class TestDiceRoller {
    @Inject
    DiceRoller sut;

    @Test
    public void shouldReturnTheResultIfNoCommentIsGiven() {
        MDC.put("test", "check-roll-without-comment");

        String input = "1d12";
        String result = sut.work(input);

        assertTrue(result.startsWith("D12:"));
    }

    @Test
    public void shouldReturnTheCommentIfACommentIsGiven() {
        MDC.put("test", "check-comment");

        String comment = "Kommentar";
        String input = "1d6 # " + comment;

        log.info("Calling {} with input: {}", sut, input);
        String result = sut.work(input);

        assertTrue(result.startsWith(comment), "The comment should be returned!");
    }


    @AfterEach
    void tearDownEach() {
        MDC.remove("test");
    }

    @BeforeAll
    static void setUp() {
        MDC.put("test-class", TestDiceRoller.class.getSimpleName());
    }

    @AfterAll
    static void tearDown() {
        MDC.clear();
    }
}
