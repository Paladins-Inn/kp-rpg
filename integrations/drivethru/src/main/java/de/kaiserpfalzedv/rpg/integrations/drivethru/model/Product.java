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

@Builder(builderClassName = "ProductBuilder", toBuilder = true, setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonDeserialize(builder = Product.ProductBuilder.class)
@Schema(name = "Product", description = "A product from DriveThruRPG.")
public class Product implements DriveThruResource {
    @JsonProperty("products_id")
    private String productsId;

    @JsonProperty("products_name")
    private String productsName;

    @JsonProperty("publishers_id")
    private String publisherId;

    @JsonProperty("publishers_name")
    private String publisherName;

    @JsonProperty("cover_url")
    private String coverURL;

    @JsonProperty("products_thumbnail")
    private String thumbnail;

    @JsonProperty("products_thumbnail100")
    private String thumbnail100;

    @JsonProperty("products_thumbnail80")
    private String thumbnail80;

    @JsonProperty("products_thumbnail40")
    private String thumbnail40;
}
