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

package de.kaiserpfalzedv.rpg.hexxen.dice;

import de.kaiserpfalzedv.rpg.core.dice.bag.D6;
import de.kaiserpfalzedv.rpg.core.dice.mat.DieResult;
import de.kaiserpfalzedv.rpg.core.dice.mat.ImmutableDieResult;

import javax.enterprise.context.Dependent;

/**
 * Blut die -- a die with symbols used by HeXXen 1733.
 *
 * The following table is used for lookups:
 *
 * 2,3 = B (1 Blut), 4,5 = BB (2 Blut), 6 = BB (3 Blut)
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.0.0 2021-01-06
 */
@Dependent
public class Blut extends D6 {
    @Override
    public DieResult roll() {
        int roll = rollSingle();

        String result = " ";
        switch (roll) {
            case 2:
            case 3:
                result = "B";
                break;
            case 4:
            case 5:
                result = "BB";
                break;
            case 6:
                result = "BBB";
                break;
        }

        return ImmutableDieResult.builder()
                .die(this)
                .total(result)
                .rolls(result)
                .build();
    }

    @Override
    public boolean isNumericDie() {
        return false;
    }
}
