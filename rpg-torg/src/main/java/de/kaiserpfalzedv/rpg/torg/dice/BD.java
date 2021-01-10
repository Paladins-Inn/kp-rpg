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

package de.kaiserpfalzedv.rpg.torg.dice;

import de.kaiserpfalzedv.rpg.core.dice.bag.D6;
import de.kaiserpfalzedv.rpg.core.dice.mat.DieResult;
import de.kaiserpfalzedv.rpg.core.dice.mat.ImmutableDieResult;

import javax.enterprise.context.Dependent;
import java.util.ArrayList;

/**
 * BD is an exploding D6.
 *
 * If a 6 is rolled, it is added as 5 and another die is rolled and added. If a 6 is rolled again, 5 will be added and
 * another die is rolled again. You recognize the pattern.
 *
 * @author rlichti {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 2021-01-02
 */
@Dependent
public class BD extends D6 {
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

        return ImmutableDieResult.builder()
                .die(this)
                .total(Integer.toString(total, 10))
                .rolls(rolls.toArray(new String[0]))
                .build();
    }
}
