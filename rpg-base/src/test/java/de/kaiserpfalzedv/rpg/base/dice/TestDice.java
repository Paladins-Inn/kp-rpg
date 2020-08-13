/*
 * Copyright 2020 Kaiserpfalz EDV-Service, Roland T. Lichti
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package de.kaiserpfalzedv.rpg.base.dice;

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

    private Die dice[] = {
            new D2(),
            new D4(),
            new D6(),
            new D8(),
            new D10(),
            new D12(),
            new D20(),
            new D100()
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

        Assertions.assertEquals(33, dice[0].hashCode(), "Wrong hash code");
        Assertions.assertEquals(35, dice[1].hashCode(), "Wrong hash code");
        Assertions.assertEquals(37, dice[2].hashCode(), "Wrong hash code");
        Assertions.assertEquals(39, dice[3].hashCode(), "Wrong hash code");
        Assertions.assertEquals(41, dice[4].hashCode(), "Wrong hash code");
        Assertions.assertEquals(43, dice[5].hashCode(), "Wrong hash code");
        Assertions.assertEquals(51, dice[6].hashCode(), "Wrong hash code");
    }

    @Test
    public void ShouldGiveANiceTostring() {
        MDC.put("test", "check-string");

        for(int i = 0; i < dice.length; i++) {
            String result = dice[i].toString();
            int max = dice[i].getMax();

            Assertions.assertTrue(result.startsWith("BasicDie@"));
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
