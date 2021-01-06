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

import de.kaiserpfalzedv.rpg.core.dice.D2;
import de.kaiserpfalzedv.rpg.core.dice.Die;
import org.junit.jupiter.api.*;
import org.slf4j.MDC;

public class TestDice {
    private final Die[] dice = {
            new FATE(),
            new D2()
    };

    @Test
    public void ShouldReturnOnlyNumbersBetween1andDieSize() {
        MDC.put("test", "range-match");

        for (Die die : dice) {
            Integer[] result = die.roll(1000);

            int sum = 0;
            for (int j = 1; j < result.length; j++) {
                sum += result[j];
                Assertions.assertTrue(result[j] <= die.getMax(), "No die roll should be above " + die.getMax());
                Assertions.assertTrue(result[j] >= -1, "No die roll should be below -1");
            }

            Assertions.assertEquals(sum, result[0], "The result should match.");
        }
    }

    @Test
    public void ShouldBeEqualWhenCompatibleDiceAreCompared() {
        MDC.put("test", "equals-compatible-dice");

        Assertions.assertEquals(dice[0], dice[0]);
    }


    @Test
    public void ShouldNotBeEqualWhenDifferentDiceAreCompared() {
        MDC.put("test", "not-equal-dice");

        Assertions.assertNotEquals(dice[0], dice[1]);
    }

    @Test
    public void ShouldReturnAHash() {
        MDC.put("test", "check-hashcode");

        Assertions.assertEquals(34, dice[0].hashCode(), "Wrong hash code for " + dice[0].getClass().getSimpleName());
    }

    @Test
    public void ShouldGiveANiceTostring() {
        MDC.put("test", "check-string");

        for (Die die : dice) {
            String result = die.toString();
            int max = die.getMax();

            Assertions.assertTrue(result.startsWith(die.getClass().getSimpleName() + "@"));
            Assertions.assertTrue(result.endsWith("[max=" + max + "]"));
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
