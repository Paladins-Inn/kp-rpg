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

package de.kaiserpfalzedv.rpg.fate.dice;

import de.kaiserpfalzedv.rpg.core.dice.ImmutableDieResult;
import de.kaiserpfalzedv.rpg.core.dice.bag.GenericNumericDie;
import de.kaiserpfalzedv.rpg.core.dice.mat.DieResult;

import javax.enterprise.context.Dependent;

/**
 * A FATE die only knows +, (empty) and -. We translate it to 1, 0 and -1.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.0.0 2021-01-06
 */
@Dependent
public class FATE extends GenericNumericDie {
    public FATE() {
        super(3);
    }

    @Override
    public DieResult roll() {
        int roll = rollSingle();

        String result = " ";
        switch (roll) {
            case 1:
                result = "-";
                break;
            case 3:
                result = "+";
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
