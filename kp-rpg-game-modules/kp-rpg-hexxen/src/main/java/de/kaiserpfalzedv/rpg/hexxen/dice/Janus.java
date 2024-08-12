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

package de.kaiserpfalzedv.rpg.hexxen.dice;

import de.kaiserpfalzedv.rpg.core.dice.mat.DieResult;

import jakarta.enterprise.context.Dependent;

/**
 * Janus die -- a die with symbols used by HeXXen 1733.
 *
 * The following table is used for lookups:
 *
 * 4,5,6 = J (Hex)
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.0.0 2021-01-06
 */
@Dependent
public class Janus extends HeXXenDie {
    @Override
    public DieResult roll() {
        DieResult rollResult = die.roll();
        int roll = Integer.parseInt(rollResult.getTotal());

        String result = " ";
        switch (roll) {
            case 4:
            case 5:
            case 6:
                result = "J";
                break;
        }

        return DieResult.builder()
                .die(this)
                .total(result)
                .rolls(new String[]{result})
                .build();
    }
}
