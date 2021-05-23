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

package de.kaiserpfalzedv.rpg.torg.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * Damage -- The damage of an {@link Attack}.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 0.3.0  2021-05-23
 */
@Builder(setterPrefix = "with", toBuilder = true)
@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
@JsonDeserialize(builder = Damage.DamageBuilder.class)
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@Schema(description = "Type of damage and the value")
public class Damage {
    public enum DamageType {
        fixed,
        cha,
        dex,
        min,
        spi,
        str
    }

    @Schema(description = "Type of the damage. Either 'fixed' or the base attribute.")
    private final DamageType type;

    @Schema(description = "The total value of damage or the adds for the damage (to be added to the attribute specified in type.")
    private final Integer value;
}
