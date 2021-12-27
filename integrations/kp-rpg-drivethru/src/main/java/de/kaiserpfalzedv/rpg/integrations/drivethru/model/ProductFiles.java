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

package de.kaiserpfalzedv.rpg.integrations.drivethru.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import de.kaiserpfalzedv.rpg.integrations.drivethru.resource.DriveThruResource;
import lombok.*;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Builder(setterPrefix = "with", toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonDeserialize(builder = ProductFiles.ProductFilesBuilder.class)
@Schema(name = "Product", description = "A product fileset from DriveThruRPG.")
public class ProductFiles implements DriveThruResource {
    @JsonProperty("bundle_id")
    private String bundleId;

    @JsonProperty("filename")
    private String filename;

    @Builder.Default
    @JsonProperty("last_modified")
    private OffsetDateTime lastModified = OffsetDateTime.now(ZoneOffset.UTC);

    @JsonProperty("raw_filesize_bytes")
    private long filesizeBytes;

    @JsonProperty("raw_filesize")
    private Double fileSizeMegaBytes;
}
