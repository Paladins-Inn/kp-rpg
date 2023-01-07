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

package de.kaiserpfalzedv.rpg.core.game;

import com.fasterxml.jackson.annotation.JsonInclude;
import de.kaiserpfalzedv.commons.core.resources.Resource;
import de.kaiserpfalzedv.commons.core.resources.ResourceImpl;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * Campaign -- A campaign consisting of multiple RPG {@link Game}s.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0  2021-02-06
 */
@Jacksonized
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@Schema(name = "Campaign", description = "A campaign consisting of multiple games.")
public class Campaign extends ResourceImpl<CampaignData> {
    public static String API_VERSION = "v1";
    public static String KIND = "Campaign";
}