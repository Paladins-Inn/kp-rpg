/*
 * Copyright (c) 2020 Kaiserpfalz EDV-Service, Roland T. Lichti
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package de.kaiserpfalzedv.rpg.base.dice;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;

/**
 * Tests the basicDie implementation and the semantic sugar of default
 * roleplaying dice (D2, D4, D6, D8, D10, D12, D20 and D100).
 *
 * @author rlichti {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 2020-08-12
 */
public class TestDice {
    private static final int D2 = 0;
    private static final int D4 = 1;
    private static final int D6 = 2;
    private static final int D8 = 3;
    private static final int D10 = 4;
    private static final int D12 = 5;
    private static final int D20 = 6;
    private static final int D100 = 7;

    private final Die[] dice = {
            new D2(),
            new D4(),
            new D6(),
            new D8(),
            new D10(),
            new D12(),
            new D20(),
            new D100()
    };

    @BeforeAll
    static void setUp() {
        MDC.put("test-class", TestDice.class.getSimpleName());
    }

    @AfterAll
    static void tearDown() {
        MDC.clear();
    }

    @Test
    public void ShouldReturnOnlyNumbersBetween1andDieSize() {
        MDC.put("test", "range-match");

        for (Die die : dice) {
            Integer[] result = die.roll(1000);

            int sum = 0;
            for (int j = 1; j < result.length; j++) {
                sum += result[j];
                Assertions.assertTrue(result[j] <= die.getSides(), "No die roll should be above " + die.getSides());
                Assertions.assertTrue(result[j] >= 1, "No die roll should be below 1");
            }

            Assertions.assertEquals(sum, result[0], "The result should match.");
        }
    }

    @Test
    public void ShouldBeEqualWhenCompatibleDiceAreCompared() {
        MDC.put("test", "equals-compatible-dice");

        Assertions.assertEquals(dice[D4], dice[D4]);
    }

    @Test
    public void ShouldNotBeEqualWhenDifferentDiceAreCompared() {
        MDC.put("test", "not-equal-dice");

        Assertions.assertNotEquals(dice[D6], dice[D2]);
    }

    @Test
    public void ShouldReturnAHash() {
        MDC.put("test", "check-hashcode");

        Assertions.assertEquals(33, dice[D2].hashCode(), "Wrong hash code");
        Assertions.assertEquals(35, dice[D4].hashCode(), "Wrong hash code");
        Assertions.assertEquals(37, dice[D6].hashCode(), "Wrong hash code");
        Assertions.assertEquals(39, dice[D8].hashCode(), "Wrong hash code");
        Assertions.assertEquals(41, dice[D10].hashCode(), "Wrong hash code");
        Assertions.assertEquals(43, dice[D12].hashCode(), "Wrong hash code");
        Assertions.assertEquals(51, dice[D20].hashCode(), "Wrong hash code");
        Assertions.assertEquals(131, dice[D100].hashCode(), "Wrong hash code");
    }

    @Test
    public void ShouldGiveANiceToString() {
        MDC.put("test", "check-string");

        for (Die die : dice) {
            String result = die.toString();
            int max = die.getSides();

            Assertions.assertTrue(result.startsWith("BasicDie@"));
            Assertions.assertTrue(result.endsWith("[max=" + max + "]"));
        }
    }

    @Test
    public void ShouldReturnModifiedValues() {
        MDC.put("test", "range-match-modified");

        Die die = dice[D10];

        for (int i = 0; i < 1000; i++) {
            int result = die.modifiedRoll(5);
            Assertions.assertTrue(result > 5, "Roll should never be lower than the modifier + 1");
            Assertions.assertTrue(result <= 16, "Roll should never exceed die max + 5");
        }
    }

    @Test
    public void ShouldGiveNumberOfSuccesses() {
        MDC.put("test", "check-success-default");

        Die die = dice[D6];

        for (int i = 0; i < 5; i++) {
            int successes = die.success(1000, 3);
            Assertions.assertTrue(successes <= 1000 && successes != 0);
        }
    }

    @Test
    public void ShouldGiveNumberOfSuccessesLow() {
        MDC.put("test", "check-success-low");

        Die die = dice[D6];

        for (int i = 0; i < 5; i++) {
            int successes = die.successLow(1000, 3);
            Assertions.assertTrue(successes <= 1000 && successes != 0);
        }
    }

    @Test
    public void ShouldGiveNumberOfSuccessesHigh() {
        MDC.put("test", "check-success-high");

        Die die = dice[D6];

        for (int i = 0; i < 5; i++) {
            int successes = die.successHigh(1000, 3);
            Assertions.assertTrue(successes <= 1000 && successes != 0);
        }
    }

    @AfterEach
    void tearDownEach() {
        MDC.remove("test");
    }
}
