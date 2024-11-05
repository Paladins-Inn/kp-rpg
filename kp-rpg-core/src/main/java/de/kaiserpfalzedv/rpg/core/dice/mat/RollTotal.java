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
import lombok.*;
import lombok.extern.jackson.Jacksonized;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;


/**
 * RollTotal -- The result of a numeric die result.
 *
 * This is the result of a numeric die roll.
 */
@Jacksonized
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@Schema(name = "RollTotal", description = "A generic result of a die roll.")
public class RollTotal implements Serializable {
    @Builder.Default
    private final List<ExpressionTotal> expressions = new ArrayList<>();

    @Builder.Default
    private final Optional<String> comment = Optional.empty();

    public String getDescription() {
        StringJoiner result = new StringJoiner(" - ");

        for (ExpressionTotal r : getExpressions()) {
            result.add(r.getDescription());
        }

        return result.toString();
    }

    /**
     * Checks if there are any results from this roll.
     *
     * @return FALSE if there is at least one result, TRUE if there are no results
     */
    public boolean isEmpty() {
        return getExpressions().size() == 0;
    }
}
