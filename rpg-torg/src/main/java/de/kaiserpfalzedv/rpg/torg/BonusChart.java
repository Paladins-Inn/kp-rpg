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

package de.kaiserpfalzedv.rpg.torg;

import de.kaiserpfalzedv.rpg.core.dice.LookupTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.Dependent;

/**
 * BonusChart -- the bonus chart of TORG:Eternity.
 *
 * The bonus chart translates an exploded roll into a bonus for a test.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 2020-01-03
 */
@Dependent
public class BonusChart implements LookupTable {
    static private final Logger LOG = LoggerFactory.getLogger(BonusChart.class);

    @Override
    public int lookup(final int roll) {
        int result = 0;

        switch (roll) {
            case 14:
            case 13:
                result = 1;
                break;
            case 12:
            case 11:
                result = 0;
                break;
            case 10:
            case 9:
                result = -1;
                break;
            case 8:
            case 7:
                result = -2;
                break;
            case 6:
            case 5:
                result = -4;
                break;
            case 4:
            case 3:
                result = -6;
                break;
            case 2:
                result = -8;
                break;
            case 1:
                result = -10;
                break;
            default:
                if (roll >= 20) {
                    result = 7 + (roll-20) / 5;
                } else if (roll >= 15) {
                    result = 2 + (roll-15) % 5;
                }
                break;
        }

        LOG.debug("Bonus chart lookup: roll={}, result={}", roll, result);
        return result;
    }
}
