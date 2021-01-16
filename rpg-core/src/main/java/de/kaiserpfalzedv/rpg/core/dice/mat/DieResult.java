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

package de.kaiserpfalzedv.rpg.core.dice.mat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.kaiserpfalzedv.rpg.core.dice.Die;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.immutables.value.Value;

import java.io.Serializable;
import java.util.Arrays;
import java.util.StringJoiner;

/**
 * DieResult -- The technical result of a single die.
 *
 * The result may carry technical information about the die roll. That may be very information when rolling a numbered
 * die. But "exploding" dice add some more information and there may be special die like FATE or HeXXen, that are not
 * displayed as numbers at all.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.0.0 2021-01-09
 */
@Value.Immutable
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonSerialize(as = ImmutableDieResult.class)
@JsonDeserialize(builder = ImmutableDieResult.Builder.class)
@Schema(name = "DieResult", description = "A generic result of a single die roll.")
public interface DieResult extends Serializable {
    /**
     * @return a pure text display of the die roll.
     */
    default String getDisplay() {
        String rolls = "";

        if (getRolls().length >= 2) {
            StringJoiner sj = new StringJoiner(",", "[", "]");
            Arrays.stream(getRolls()).forEach(sj::add);
            rolls = sj.toString();
        }

        return getTotal() + rolls;
    }

    /**
     * @return the most terse display of the die roll.
     */
    default String getShortDisplay() {
        return getTotal();
    }

    /**
     * @return The total result (may be the sum or number of successes).
     */
    String getTotal();

    /**
     * These are the rolls. Raw data.
     *
     * @return The single technical rolls.
     */
    String[] getRolls();

    /**
     * @return the die to roll.
     */
    Die getDie();
}
