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

package de.kaiserpfalzedv.rpg.core.dice;

import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * @author rlichti {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 2020-08-12
 */
public class TestDice {
    static final private Logger LOGGER = LoggerFactory.getLogger(TestDice.class);

    private final Die[] dice = {
            new D2(),
            new D4(),
            new D6(),
            new D8(),
            new D10(),
            new D12(),
            new D20(),
            new D100(),
            new BasicDie(3)
    };

    @Test
    public void ShouldReturnOnlyNumbersBetween1andDieSize() {
        MDC.put("test", "range-match");

        for (int i = 0; i < dice.length; i++) {
            Integer[] result = dice[i].roll(1000);

            int sum = 0;
            for(int j = 1; j < result.length; j++) {
                sum += result[j];
                Assertions.assertTrue(result[j] <= dice[i].getMax(), "No die roll should be above " + dice[i].getMax());
                Assertions.assertTrue(result[j] >= 1, "No die roll should be below 1");
            }

            Assertions.assertEquals(sum, result[0], "The result should match.");
        }
    }

    @Test
    public void ShouldBeEqualWhenCompatibleDiceAreCompared() {
        MDC.put("test", "equals-compatible-dice");

        Assertions.assertTrue(dice[1].equals(dice[1]));
    }


    @Test
    public void ShouldNotBeEqualWhenDifferentDiceAreCompared() {
        MDC.put("test", "not-equal-dice");

        Assertions.assertFalse(dice[2].equals(dice[1]));
    }

    @Test
    public void ShouldReturnAHash() {
        MDC.put("test", "check-hashcode");

        Assertions.assertEquals(33, dice[0].hashCode(), "Wrong hash code for " + dice[0].getClass().getSimpleName());
        Assertions.assertEquals(35, dice[1].hashCode(), "Wrong hash code for " + dice[1].getClass().getSimpleName());
        Assertions.assertEquals(37, dice[2].hashCode(), "Wrong hash code for " + dice[2].getClass().getSimpleName());
        Assertions.assertEquals(39, dice[3].hashCode(), "Wrong hash code for " + dice[3].getClass().getSimpleName());
        Assertions.assertEquals(41, dice[4].hashCode(), "Wrong hash code for " + dice[4].getClass().getSimpleName());
        Assertions.assertEquals(43, dice[5].hashCode(), "Wrong hash code for " + dice[5].getClass().getSimpleName());
        Assertions.assertEquals(51, dice[6].hashCode(), "Wrong hash code for " + dice[6].getClass().getSimpleName());
        Assertions.assertEquals(131, dice[7].hashCode(), "Wrong hash code for " + dice[7].getClass().getSimpleName());
    }

    @Test
    public void ShouldGiveANiceTostring() {
        MDC.put("test", "check-string");

        for(int i = 0; i < dice.length; i++) {
            String result = dice[i].toString();
            int max = dice[i].getMax();

            Assertions.assertTrue(result.startsWith(dice[i].getClass().getSimpleName() + "@"));
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
