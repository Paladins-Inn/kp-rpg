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

import javax.enterprise.context.Dependent;

/**
 * This is an exploding D20 with a minimum of 10 when exploding.
 *
 * Every 10 and 20 is rerolled and added until no 10 or 20 is rolled. If the last roll is less than 10, then at least 10
 * is added.
 *
 * @author rlichti {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 2021-01-02
 */
@Dependent
public class T20M extends TorgExplodingDie implements TorgDie {
    public T20M() {
        super(new D20(), 10);
    }
}
