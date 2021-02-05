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

package de.kaiserpfalzedv.rpg.core.dice.history;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.kaiserpfalzedv.rpg.core.dice.mat.RollTotal;
import de.kaiserpfalzedv.rpg.core.user.User;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.immutables.value.Value;

import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * RollHistoryEntry -- A stored roll result for getting a list of recent rolls.
 * <p>
 * The rolls are stored seperately for users in sessions.
 * <p>
 * For example a session could be represented by a special Discord channel.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0  2021-02-05
 */
@Value.Immutable
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonSerialize(as = ImmutableRollHistoryEntry.class)
@JsonDeserialize(builder = ImmutableRollHistoryEntry.Builder.class)
@Schema(name = "RollHistory", description = "A single dice roll.")
public interface RollHistoryEntry extends Serializable {
    /**
     * @return the session this roll history belongs to.
     */
    String getSession();

    /**
     * @return The user who rolled this roll.
     */
    User getUser();

    /**
     * @return a roll id.
     */
    String getRollId();

    /**
     * @return the time of this roll.
     */
    OffsetDateTime getTimestamp();

    /**
     * @return The roll definition.
     */
    RollTotal getResult();
}
