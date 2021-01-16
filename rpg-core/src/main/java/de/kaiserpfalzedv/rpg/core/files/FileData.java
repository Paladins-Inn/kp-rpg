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

package de.kaiserpfalzedv.rpg.core.files;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.immutables.value.Value;

import java.io.Serializable;
import java.util.Optional;

/**
 * FileData is the generic resource data type for storing files on this service.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.0.0 2021-01-07
 */
@Value.Immutable
@Value.Modifiable
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonSerialize(as = ImmutableFileData.class)
@JsonDeserialize(builder = ImmutableFileData.Builder.class)
@Schema(name = "fileData", description = "The data spec of the file.")
public interface FileData extends Serializable {
    /**
     * @return the size in bytes of this file.
     */
    @Schema(name = "size", description = "The filesize in bytes.", required = true)
    Long getSize();

    /**
     * @return The MIME type of this file.
     */
    @Schema(name = "fileType", description = "The MIME type of the file.", required = true)
    String getMimeType();

    /**
     * @return The file data itself
     */
    @Schema(name = "file", description = "The file data itself.")
    Optional<Serializable> getFile();
}
