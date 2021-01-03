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
import org.slf4j.MDC;

import java.util.Optional;

public class TestDiceParser {
    private final DiceParser sut = new DiceParser();

    @Test
    public void ShouldParseDieWhenOnlyDieTypeIsDefined() {
        MDC.put("test", "type");

        String input = "D3";

        Optional<DieRoll> result = sut.parse(input);

        checkResult(result, 1, input, 0, 1d);
    }

    @Test
    public void ShouldParseDieWhenAmountOfDiceIsDefined() {
        MDC.put("test", "number-type");

        String input = "5D10";

        Optional<DieRoll> result = sut.parse(input);

        checkResult(result, 5, "D10", 0, 1d);
    }

    @Test
    public void ShouldParseDieWhenTypeOfDieAndAddIsDefined() {
        MDC.put("test", "type-add");

        String input = "D10+5";

        Optional<DieRoll> result = sut.parse(input);

        checkResult(result, 1, "D10", 5, 1d);
    }

    @Test
    public void ShouldParseDieWhenNumberOfDieAndTypeOfDieAndAddIsDefined() {
        MDC.put("test", "number-type-add");

        String input = "8D10+5";

        Optional<DieRoll> result = sut.parse(input);

        checkResult(result, 8, "D10", 5, 1d);
    }


    @Test
    public void ShouldParseDieWhenTypeOfDieAndMultiplierIsDefined() {
        MDC.put("test", "type-multi");

        String input = "D10*6";

        Optional<DieRoll> result = sut.parse(input);

        checkResult(result, 1, "D10", 0, 6d);
    }

    @Test
    public void ShouldParseDieWhenTypeOfDieAndAddAndMultiplierIsDefined() {
        MDC.put("test", "type-add-multi");

        String input = "D10+5*6";

        Optional<DieRoll> result = sut.parse(input);

        checkResult(result, 1, "D10", 5, 6d);
    }

    @Test
    public void ShouldParseDieWhenNumberOfDieAndTypeOfDieAndMultiplierIsDefined() {
        MDC.put("test", "number-type-multi");

        String input = "8D10*6";

        Optional<DieRoll> result = sut.parse(input);

        checkResult(result, 8, "D10", 0, 6d);
    }

    @Test
    public void ShouldParseDieWhenNumberOfDieAndTypeOfDieAndAddAndMultiplierIsDefined() {
        MDC.put("test", "number-type-add-multi");

        String input = "8D10+5*6";

        Optional<DieRoll> result = sut.parse(input);

        checkResult(result, 8, "D10", 5, 6d);
    }

    @Test
    public void ShouldParseDieWhenTypeOfDieAndDivisorIsDefined() {
        MDC.put("test", "type-divisor");

        String input = "D10/2";

        Optional<DieRoll> result = sut.parse(input);

        checkResult(result, 1, "D10", 0, 0.5d);
    }

    /**
     * Checks the result against the given parameters.
     *
     * @param result The optional result.
     * @param amountOfDice The number of die expected.
     * @param dieType The type of die expected.
     * @param add The add/subtract expected.
     * @param multiplier The expected multiplier.
     */
    private void checkResult(
            @SuppressWarnings("OptionalUsedAsFieldOrParameterType") final Optional<DieRoll> result,
            final int amountOfDice,
            final String dieType,
            final int add,
            final double multiplier
    ) {
        Assertions.assertTrue(result.isPresent(), "There should be a DiceRollCommand!");
        DieRoll roll = result.get();

        Assertions.assertEquals(amountOfDice, roll.getNumberOfDice(), "The number of dice should be " + amountOfDice);
        Assertions.assertEquals(dieType, roll.getDieIdentifier(), "The die type should be '" + dieType + "'");
        Assertions.assertEquals(add, roll.getAdd(), "'" + add + "' should be added/subtracted.");
        Assertions.assertEquals(multiplier, roll.getMultiply(), "The multiplier should be '" + multiplier + "'");
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
}
