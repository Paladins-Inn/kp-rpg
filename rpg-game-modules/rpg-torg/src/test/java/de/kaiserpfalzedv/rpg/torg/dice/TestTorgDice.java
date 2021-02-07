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

package de.kaiserpfalzedv.rpg.torg.dice;

import de.kaiserpfalzedv.rpg.core.dice.Die;
import de.kaiserpfalzedv.rpg.core.dice.mat.DieResult;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author rlichti {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 2020-08-12
 */
public class TestTorgDice {
    private static final Logger LOG = LoggerFactory.getLogger(TestTorgDice.class);

    private static final Die[] dice = {
            new BD(),
            new T20(),
            new T20M()
    };


    @Test
    public void ShouldBeEqualWhenCompatibleDiceAreCompared() {
        MDC.put("test", "equals-compatible-dice");

        for(Die die : dice) {
            assertEquals(die, die, "Dice '" + die.getDieType() + "' should be equal to '" + die.getDieType() + "'.");
        }
    }

    @Test
    public void ShouldReturnValidDiceThrows() {
        MDC.put("test", "test-throws");

        for (Die die : dice) {
            LOG.trace("Testing rolling die: {}", die);

            DieResult result = die.roll();
            DieResult[] results = die.roll(10);

            LOG.info("die={}, result={}, results={}", die.getClass().getSimpleName(), result, results);
        }
    }

    @Test
    public void ShouldNotBeEqualWhenDifferentDiceAreCompared() {
        MDC.put("test", "not-equal-dice");

        LOG.trace("equals: dice[1]={}, dice[0]={}", dice[1].hashCode(), dice[0].hashCode());

        Assertions.assertNotEquals(dice[1], dice[0]);
        Assertions.assertNotEquals(dice[0], dice[1]);
    }

    @Test
    public void ShouldReturnAHash() {
        MDC.put("test", "check-hashcode");

        assertEquals(37, dice[0].hashCode(), "Wrong hash code " + dice[0].getClass().getSimpleName());
        assertEquals(51, dice[1].hashCode(), "Wrong hash code " + dice[1].getClass().getSimpleName());
        assertEquals(51, dice[2].hashCode(), "Wrong hash code " + dice[2].getClass().getSimpleName());
    }

    @Test
    public void ShouldGiveANiceToString() {
        MDC.put("test", "check-string");

        for (Die die : dice) {
            String result = die.toString();

            assertTrue(result.startsWith(die.getDieType() + "["));
        }
    }

    @AfterEach
    void tearDownEach() {
        MDC.remove("test");
    }

    @BeforeAll
    static void setUp() {
        MDC.put("test-class", TestTorgDice.class.getSimpleName());
    }

    @AfterAll
    static void tearDown() {
        MDC.clear();
    }
}
