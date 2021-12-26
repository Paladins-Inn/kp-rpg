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

import de.kaiserpfalzedv.rpg.core.dice.bag.*;
import de.kaiserpfalzedv.rpg.core.dice.mat.DieResult;
import org.junit.jupiter.api.*;
import org.slf4j.MDC;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author rlichti {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 2020-08-12
 */
public class TestDice {
    private final GenericNumericDie[] dice = {
            new D2(),
            new D4(),
            new D6(),
            new D8(),
            new D10(),
            new D12(),
            new D20(),
            new D100(),
            new GenericNumericDie(3)
    };

    @Test
    public void ShouldReturnOnlyNumbersBetween1andDieSize() {
        MDC.put("test", "range-match");

        for (Die die : dice) {
            DieResult[] result = die.roll(1000);

            for (int j = 1; j < result.length; j++) {
                int roll = Integer.parseInt(result[j].getTotal(), 10);

                assertTrue(roll >= 1, "No die roll should be below 1");
                assertTrue(roll <= ((GenericNumericDie) result[j].getDie()).max, "No die roll should be above the maximum of the die");

            }
        }
    }

    @Test
    public void ShouldBeEqualWhenCompatibleDiceAreCompared() {
        MDC.put("test", "equals-compatible-dice");

        Assertions.assertEquals(dice[1], dice[1]);
    }


    @Test
    public void ShouldNotBeEqualWhenDifferentDiceAreCompared() {
        MDC.put("test", "not-equal-dice");

        Assertions.assertNotEquals(dice[1], dice[2]);
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

        for (GenericNumericDie die : dice) {
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
        MDC.put("test-class", TestDice.class.getSimpleName());
    }

    @AfterAll
    static void tearDown() {
        MDC.clear();
    }
}
