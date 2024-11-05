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

import de.kaiserpfalzedv.rpg.core.dice.Die;
import de.kaiserpfalzedv.rpg.core.dice.mat.DieResult;
import lombok.ToString;

import java.util.ArrayList;

/**
 * TorgD20Base --
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 0.3.0  2021-05-12
 */
@ToString
public abstract class TorgD20Base implements Die {
    /**
     * The die roll itself.
     *
     * @return the numeric result of the roll of this die.
     */
    protected int rollSingle() {
        return (int) (Math.random() * 21);
    }

    public final DieResult[] roll(final int number) {
        ArrayList<DieResult> results = new ArrayList<>(number);

        for (int i = 1; i <= number; i++) {
            results.add(roll());
        }

        return results.toArray(new DieResult[0]);
    }

    @Override
    public String getDieType() {
        return getClass().getSimpleName();
    }

    @Override
    public boolean isNumericDie() {
        return true;
    }
}
