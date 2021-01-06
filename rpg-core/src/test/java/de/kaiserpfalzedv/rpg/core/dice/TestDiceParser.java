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

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class TestDiceParser {
    private static final int DEFAULT_THROW = 2;
    private static final DieResult[] tests = {
            new DieResult("(W20+5+2)/2", "D20", 1, 5),
            new DieResult("(2d6+2)/2", "D6", 2, 3),
            new DieResult("1D6", "D6", 1,2),
            new DieResult("2D6", "D6", 2,4),
            new DieResult("D6", "D6", 1,2),
            new DieResult("d6", "D6", 1,2),
            new DieResult("W6", "D6", 1,2),
            new DieResult("w6", "D6", 1,2),
            new DieResult("D10+5", "D10", 1,7),
            new DieResult("D10-6", "D10", 1, -4),
            new DieResult("D10+2+5", "D10", 1, 9),
            new DieResult("D10*10", "D10", 1, 20),
            new DieResult("D10*2*2", "D10", 1, 8),
            new DieResult("D10*2+8", "D10", 1, 12),
            new DieResult("D10*(2+8)", "D10", 1, 20),
            new DieResult("D10/2", "D10", 1, 1)
    };
    private static final Die TEST_DIE = new TestDie();


    private final DiceParser sut = new DiceParser();

    @Test
    public void ShouldDeliverResultsWhenValidExpressionsAreGiven() {
        MDC.put("test", "valid-expression");

        for (DieResult testInput : tests) {
            Optional<DieRoll> result = sut.parse(testInput.input);

            checkValidResult(result, testInput);
        }
    }

    @Test
    public void ShouldReturnNoDieRollWhenInvalidExpressionIsGiven() {
        MDC.put("test", "invalid-expression");

        Optional<DieRoll> result = sut.parse("(d8+*9");

        assertFalse(result.isPresent(), "The string '(d8+*9' should not generate a result!");
    }


    /**
     * Checks the result against the given parameters.
     *
     * @param result The optional result.
     * @param testInput The predefined test input.
     */
    private void checkValidResult(
            @SuppressWarnings("OptionalUsedAsFieldOrParameterType") final Optional<DieRoll> result,
            final DieResult testInput
    ) {
        assertTrue(result.isPresent(), "There should be a DiceRollCommand for '" + testInput.input + "'!");
        DieRoll roll = result.get();

        assertEquals(testInput.dieType, roll.getDieIdentifier(), "The die type of '" + testInput.input + "' should be '" + testInput.dieType + "'");
        assertEquals(testInput.result, roll.eval(TEST_DIE)[0], "The result of '" + testInput.input + "' should be: " + testInput.result);
    }


    @AfterEach
    void tearDownEach() {
        MDC.remove("test");
    }

    @BeforeAll
    static void setUp() {
        MDC.put("test-class", TestDiceParser.class.getSimpleName());
    }

    @AfterAll
    static void tearDown() {
        MDC.clear();
    }


    private static class DieResult {
        final String input;
        final String dieType;
        final int amount;
        final Integer result;

        DieResult(final String input, final String dieType, final int amount, final int result) {
            this.input = input;
            this.dieType = dieType;
            this.amount = amount;
            this.result = result;
        }
    }

    private static class TestDie extends BasicDie {
        public TestDie() {
            super(DEFAULT_THROW);
        }

        @Override
        public int roll() {
            return DEFAULT_THROW;
        }
    }
}
