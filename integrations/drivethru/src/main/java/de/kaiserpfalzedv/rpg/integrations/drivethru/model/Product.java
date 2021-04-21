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
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.kaiserpfalzedv.rpg.integrations.drivethru.resource.DriveThruResource;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.immutables.value.Value;

@Value.Immutable
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonSerialize(as = ImmutableProduct.class)
@JsonDeserialize(builder = ImmutableProduct.Builder.class)
@Schema(name = "Product", description = "A product from DriveThruRPG.")
public interface Product extends DriveThruResource {
    @JsonProperty("products_id")
    String getProductsId();

    @JsonProperty("products_name")
    String getProductsName();

    @JsonProperty("publishers_id")
    String getPublisherId();

    @JsonProperty("publishers_name")
    String getPublisherName();

    @JsonProperty("cover_url")
    String getCoverURL();

    @JsonProperty("products_thumbnail")
    String getThumbnail();

    @JsonProperty("products_thumbnail100")
    String getThumbnail100();

    @JsonProperty("products_thumbnail80")
    String getThumbnail80();

    @JsonProperty("products_thumbnail40")
    String getThumbnail40();
}
