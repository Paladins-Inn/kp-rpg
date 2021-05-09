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
import de.kaiserpfalzedv.rpg.core.dice.mat.ExpressionTotal;
import de.kaiserpfalzedv.rpg.core.dice.mat.RollTotal;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TestDiceParser {
    private static final Logger LOG = LoggerFactory.getLogger(TestDiceParser.class);

    private static final int DEFAULT_THROW = 2;
    private static final DieTestResult[] tests = {
            new DieTestResult("sin(d6)", "D6", 1),
            new DieTestResult("(W20+5+2)/2", "D20", 1),
            new DieTestResult("(2d6+2)/2", "D6", 2),
            new DieTestResult("1D6", "D6", 1),
            new DieTestResult("2D6", "D6", 2),
            new DieTestResult("D6", "D6", 1),
            new DieTestResult("d6", "D6", 1),
            new DieTestResult("W6", "D6", 1),
            new DieTestResult("w6", "D6", 1),
            new DieTestResult("D10+5", "D10", 1),
            new DieTestResult("D10-6", "D10", 1),
            new DieTestResult("D10+2+5", "D10", 1),
            new DieTestResult("D10*10", "D10", 1),
            new DieTestResult("D10*2*2", "D10", 1),
            new DieTestResult("D10*2+8", "D10", 1),
            new DieTestResult("D10*(2+8)", "D10", 1),
            new DieTestResult("D10/2", "D10", 1)
    };

    private DiceParser sut;



    @Test
    public void ShouldDeliverResultsWhenValidExpressionsAreGiven() {
        MDC.put("test", "valid-expression");

        for (DieTestResult testInput : tests) {
            RollTotal result = sut.parse(testInput.input);

            checkValidResult(result, testInput);
        }
    }

    @Test
    public void ShouldReturnNoDieRollWhenInvalidExpressionIsGiven() {
        MDC.put("test", "invalid-expression");


        RollTotal result = sut.parse("(d8+*9");

        assertTrue(result.isEmpty(), "The string '(d8+*9' should not generate a result!");
    }


    /**
     * Checks the result against the given parameters.
     *
     * @param result The optional result.
     * @param testInput The predefined test input.
     */
    private void checkValidResult(
            @SuppressWarnings("OptionalUsedAsFieldOrParameterType") final RollTotal result,
            final DieTestResult testInput
    ) {
        LOG.trace("Checking test: expected={}, input={}, amount={}, type={}",
                result, testInput.input, testInput.amount, testInput.dieType);


        assertFalse(result.isEmpty(), "There should be one die roll in the result!");
        assertEquals(result.getExpressions().size(), 1, "There should be exactly ONE roll in the result!");

        ExpressionTotal roll = result.getExpressions().get(0);

        assertEquals(testInput.dieType, roll.getRolls()[0].getDie().getDieType(), "The die type of '" + testInput.input + "' should be '" + testInput.dieType + "'");
    }

    @BeforeEach
    void setupEach() {
        ArrayList<Die> dice = new ArrayList<>();
        dice.add(new D2());
        dice.add(new D4());
        dice.add(new D6());
        dice.add(new D8());
        dice.add(new D10());
        dice.add(new D12());
        dice.add(new D20());
        dice.add(new D100());

        sut = new DiceParser(dice);
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

    private static class DieTestResult {
        final String input;
        final String dieType;
        final int amount;

        DieTestResult(final String input, final String dieType, final int amount) {
            this.input = input;
            this.dieType = dieType;
            this.amount = amount;
        }
    }
}
