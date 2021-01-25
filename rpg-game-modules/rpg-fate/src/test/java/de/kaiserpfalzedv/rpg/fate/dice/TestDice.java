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

package de.kaiserpfalzedv.rpg.fate.dice;

import de.kaiserpfalzedv.rpg.core.dice.Die;
import de.kaiserpfalzedv.rpg.core.dice.bag.D2;
import de.kaiserpfalzedv.rpg.core.dice.mat.DieResult;
import org.junit.jupiter.api.*;
import org.slf4j.MDC;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestDice {
    private final Die[] dice = {
            new FATE(),
            new D2()
    };

    @Test
    public void shouldReturnOnlyPlusMinusOrEmpty() {
        MDC.put("test", "check-valid-results");

        DieResult result = new FATE().roll();

        assertTrue(result.getShortDisplay().matches("[-+ ]"), "The result should be '+', ' ' or '-'.");
    }

    @Test
    public void shouldBeEqualWhenCompatibleDiceAreCompared() {
        MDC.put("test", "equals-compatible-dice");

        assertEquals(dice[0], dice[0]);
    }


    @Test
    public void shouldNotBeEqualWhenDifferentDiceAreCompared() {
        MDC.put("test", "not-equal-dice");

        Assertions.assertNotEquals(dice[0], dice[1]);
    }

    @Test
    public void shouldReturnAHash() {
        MDC.put("test", "check-hashcode");

        assertEquals(34, dice[0].hashCode(), "Wrong hash code for " + dice[0].getClass().getSimpleName());
    }

    @Test
    public void shouldGiveANiceTostring() {
        MDC.put("test", "check-string");

        for (Die die : dice) {
            String result = die.toString();

            assertTrue(result.startsWith(die.getClass().getSimpleName() + "["));
        }
    }


    @AfterEach
    void tearDownEach() {
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
