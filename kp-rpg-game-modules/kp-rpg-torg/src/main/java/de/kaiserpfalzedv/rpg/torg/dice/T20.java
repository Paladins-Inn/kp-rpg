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

import de.kaiserpfalzedv.rpg.core.dice.LookupTable;
import de.kaiserpfalzedv.rpg.core.dice.mat.DieResult;
import de.kaiserpfalzedv.rpg.torg.BonusChart;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import jakarta.enterprise.context.Dependent;
import java.util.ArrayList;
import java.util.Optional;

/**
 * This is an exploding D20 with no minimum value when exploding.
 * <p>
 * Every 10 and 20 is rerolled until no 10 or 20 is rolled any more.
 *
 * @author rlichti {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 2021-01-02
 */
@Dependent
@ToString
@EqualsAndHashCode(callSuper = true)
public class T20 extends TorgD20Base {
    @Override
    public DieResult roll() {
        int total = 0;
        ArrayList<String> rolls = new ArrayList<>(5);

        int roll;
        do {
            roll = rollSingle();

            total += roll;
            rolls.add(Integer.toString(roll, 10));
        } while (roll == 10 || roll == 20);

        return DieResult.builder()
                .die(this)
                .total(Integer.toString(total, 10))
                .rolls(rolls.toArray(new String[0]))
                .build();
    }

    @Override
    public Optional<LookupTable> getLookupTable() {
        return Optional.of(new BonusChart());
    }
}
