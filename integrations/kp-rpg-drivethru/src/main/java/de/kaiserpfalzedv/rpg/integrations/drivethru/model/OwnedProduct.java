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

package de.kaiserpfalzedv.rpg.integrations.drivethru.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.kaiserpfalzedv.rpg.integrations.drivethru.resource.DriveThruResource;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.*;
import lombok.extern.jackson.Jacksonized;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.OffsetDateTime;
import java.util.Optional;

@RegisterForReflection
@SuppressWarnings("FieldMayBeFinal")
@Jacksonized
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@Schema(name = "OwnedProduct", description = "a product dataset of DriveThruRPG.")
public class OwnedProduct implements DriveThruResource {
    @JsonProperty("products_id")
    private String id;

    @JsonProperty("products_name")
    private String name;

    @JsonProperty("is_archived")
    @Builder.Default
    private Optional<String> archived = Optional.empty();

    @JsonProperty("cover_url")
    @Builder.Default
    private Optional<String> coverURL = Optional.empty();

    @JsonProperty("date_purchased")
    @Builder.Default
    private Optional<OffsetDateTime> datePurchased = Optional.empty();
}
