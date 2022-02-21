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

package de.kaiserpfalzedv.rpg.torg.model.perks;

import com.fasterxml.jackson.annotation.JsonInclude;
import de.kaiserpfalzedv.rpg.torg.model.actors.Clearance;
import de.kaiserpfalzedv.rpg.torg.model.actors.SkillValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.io.Serializable;
import java.util.Set;

/**
 * Prerequisites -- The prerequisites for obtaining a perk.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 0.3.0  2021-05-23
 */
@Jacksonized
@Builder(toBuilder = true)
@AllArgsConstructor
@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@Schema(description = "The prerequisites for obtaining a perk.")
public class Prerequisites implements Serializable {
    @Schema(description = "Skills that are a prerequisite.", nullable = true, minItems = 0)
    private Set<SkillValue> skills;

    @Schema(description = "Minimum clearance level as prerequisite", nullable = true)
    @Builder.Default
    private Clearance clearance = Clearance.ALPHA;
}
