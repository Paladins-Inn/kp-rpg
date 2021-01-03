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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * This is the exploding D20 with a min add.
 *
 * @author rlichti {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 2021-01-02
 */
class TorgExplodingDie implements TorgDie {
    static private final Logger LOG = LoggerFactory.getLogger(TorgExplodingDie.class);
    static private final int MAX = Integer.MAX_VALUE;

    private final Die baseDie;
    private final int min;

    protected TorgExplodingDie(final Die baseDie, final int min) {
        this.min = min;
        this.baseDie = baseDie;
    }


    @Override
    public int roll() {
        int result;

        if (baseDie.getMax() == 6) {
            result = explodeD6();
        } else { // max == 20
            result = explodeD20();
        }


        LOG.debug("TORG Die({}): total={}", baseDie.getMax(), result);
        return result;
    }

    private int explodeD6() {
        int result = 0;
        int roll, add;

        do {
            roll = baseDie.roll();
            add = Math.min(roll, 5);

            LOG.trace("TORG Die({}): total={}, roll={}, add={}", baseDie.getMax(), result, roll, add);

            result += add;
        } while (roll == 6);

        return result;
    }

    private int explodeD20() {
        int result = 0;
        int roll, add;

        do {
            roll = baseDie.roll();
            add = Math.max(roll, min);

            LOG.trace("TORG Die({}): result={}, roll={}, add={}", baseDie.getMax(), result, roll, add);

            result += add;
        } while (roll == 10 || roll == 20);

        return lookupBonus(result);
    }

    private int lookupBonus(final int roll) {
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
                    result = 7 + (roll-20) % 5;
                } else if (roll >= 15) {
                    result = 2 + (roll-15) % 5;
                }
                break;
        }

        return result;
    }

    @Override
    public Integer[] roll(final int number) {
        ArrayList<Integer> result = new ArrayList<>();
        result.add(0); // reserve the first element for the total

        int total = 0;
        for (int i = 0; i < number; i++) {
            int roll = roll();
            total += roll;
            result.add(roll);
        }
        result.set(0, total);

        return result.toArray(new Integer[0]);
    }

    public int getMin() {
        return min;
    }

    @Override
    public int getMax() {
        return MAX;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TorgExplodingDie)) return false;

        TorgExplodingDie other = (TorgExplodingDie) o;

        LOG.trace("min={}, other.min={}", min, other.min);
        if (min != other.min) return false;
        return baseDie.equals(other.baseDie);
    }

    @Override
    public int hashCode() {
        return Objects.hash(baseDie, min);
    }

    @Override
    public final String toString() {
        return new StringJoiner(", ",
                TorgExplodingDie.class.getSimpleName() + "@" + System.identityHashCode(this) + "[",
                "]")
                .add("max=" + MAX)
                .add("min=" + min)
                .toString();
    }
}
