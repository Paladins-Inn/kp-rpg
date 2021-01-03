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

import de.kaiserpfalzedv.rpg.core.dice.D20;

/**
 * This is an exploding D20 with no minimum value when exploding.
 *
 * Every 10 and 20 is rerolled until no 10 or 20 is rolled any more.
 *
 * @author rlichti {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 2021-01-02
 */
public class T20 extends TorgExplodingDie implements TorgDie {
    public T20() {
        super(new D20(), 0);
    }
}
