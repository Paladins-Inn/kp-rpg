/*
 * Copyright (c) 2022 Kaiserpfalz EDV-Service, Roland T. Lichti.
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
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.enterprise.context.Dependent;
import java.util.ArrayList;

/**
 * BD is an exploding D6.
 * <p>
 * If a 6 is rolled, it is added as 5 and another die is rolled and added. If a 6 is rolled again, 5 will be added and
 * another die is rolled again. You recognize the pattern.
 *
 * @author rlichti {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 2021-01-02
 */
@Dependent
@ToString
@EqualsAndHashCode
public class BD implements Die {
    public final DieResult[] roll(final int number) {
        ArrayList<DieResult> results = new ArrayList<>(number);

        for (int i = 1; i <= number; i++) {
            results.add(roll());
        }

        return results.toArray(new DieResult[0]);
    }

    @Override
    public boolean isNumericDie() {
        return true;
    }

    /**
     * The die roll itself.
     * @return the numeric result of the roll of this die.
     */
    private int rollSingle() {
        return (int) (Math.random() * 7);
    }


    @Override
    public DieResult roll() {
        int total = 0;
        ArrayList<String> rolls = new ArrayList<>(5);

        int roll;
        do {
            roll = rollSingle();

            if (roll == 6) {
                total--; // only add 5 to the total instead of 6.
            }

            total += roll;
            rolls.add(Integer.toString(roll, 10));
        } while (roll == 6);

        return DieResult.builder()
                .die(this)
                .total(Integer.toString(total, 10))
                .rolls(rolls.toArray(new String[0]))
                .build();
    }
}
