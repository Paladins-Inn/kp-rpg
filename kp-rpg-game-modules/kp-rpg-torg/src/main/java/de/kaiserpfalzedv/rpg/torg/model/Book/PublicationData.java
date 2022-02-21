/*
 * Copyright (c) &today.year Kaiserpfalz EDV-Service, Roland T. Lichti
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.kaiserpfalzedv.rpg.torg.model.Book;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * PublicationData -- The metadata for a single publication.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0  2021-05-23
 */
@Jacksonized
@Builder(toBuilder = true)
@Getter
@ToString
@EqualsAndHashCode
@Schema(name = "PublicationData", description = "This is the data for an publication")
public class PublicationData implements Serializable {
    /**
     * Order Id of this publication at the publishing house.
     */
    @Schema(description = "The official SKU for this publication", maxLength = 20)
    @NotEmpty
    @Size(max = 20, message = "The order id limited to 20 characters.")
    private final String orderId;

    /**
     * Official title of the module.
     */
    @Schema(description = "The official title of the publication", maxLength = 200)
    @NotEmpty
    @Size(max = 200, message = "The title is limited to 200 characters.")
    private final String title;

    /**
     * Title to display (max 50 characters)
     */
    @Schema(description = "The short title of the publication", maxLength = 50)
    @NotEmpty
    @Size(max = 50, message = "The display title is limited to 50 characters.")
    private final String displayTitle;

    @Schema(description = "The imprint of the publication", maxLength = 5000, nullable = true)
    @Size(max = 5000, message = "The imprint must not be longer than 5000 characters.")
    private final String imprint;

    @Schema(description = "The editorial of the publication", maxLength = 5000, nullable = true)
    @Size(max = 5000, message = "The description must not be longer than 5000 characters.")
    private final String editorial;

    @Schema(description = "ID of this publication at https://drivethrurpg.com", nullable = true)
    private final Integer driveThroughId;

    @Schema(description = "Total number of pages of this publications", nullable = true)
    private final Integer pages;
}
