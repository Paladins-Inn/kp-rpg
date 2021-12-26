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

import de.kaiserpfalzedv.rpg.core.dice.LookupTable;
import de.kaiserpfalzedv.rpg.core.dice.mat.DieResult;
import de.kaiserpfalzedv.rpg.torg.BonusChart;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.enterprise.context.Dependent;
import java.util.ArrayList;
import java.util.Optional;

/**
 * This is an exploding D20 with a minimum of 10 when exploding.
 * <p>
 * Every 10 and 20 is rerolled and added until no 10 or 20 is rolled. If the last roll is less than 10, then at least 10
 * is added.
 *
 * @author rlichti {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 2021-01-02
 */
@Dependent
@ToString
@EqualsAndHashCode
public class T20M extends TorgD20Base {
    @Override
    public DieResult roll() {
        int total = 0;
        ArrayList<String> rolls = new ArrayList<>(5);

        int roll;
        do {
            roll = rollSingle();

            if (total > 0 && roll < 10) {
                total += (10 - roll);
            }

            total += roll;
            rolls.add(Integer.toString(roll, 10));
        } while (roll == 10 || roll == 20);

        return DieResult.builder()
                .withDie(this)
                .withTotal(Integer.toString(total, 10))
                .withRolls(rolls.toArray(new String[0]))
                .build();
    }

    @Override
    public Optional<LookupTable> getLookupTable() {
        return Optional.of(new BonusChart());
    }
}
