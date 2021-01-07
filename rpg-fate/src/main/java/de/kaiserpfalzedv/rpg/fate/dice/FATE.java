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

import de.kaiserpfalzedv.rpg.core.dice.BasicDie;
import de.kaiserpfalzedv.rpg.core.dice.Die;

import javax.enterprise.context.Dependent;

/**
 * A FATE die only knows +, (empty) and -. We translate it to 1, 0 and -1.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.0.0 2021-01-06
 */
@Dependent
public class FATE extends BasicDie implements Die {
    public FATE() {
        super(3);
    }

    @Override
    public int roll() {
        return super.roll() - 2;
    }
}
