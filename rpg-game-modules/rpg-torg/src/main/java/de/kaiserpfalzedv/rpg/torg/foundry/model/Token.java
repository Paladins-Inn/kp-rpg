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

package de.kaiserpfalzedv.rpg.torg.foundry.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;

/**
 * Token --
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 2.0.0  2021-06-04
 */
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonDeserialize(builder = Token.TokenBuilder.class)
@Builder(setterPrefix = "with", toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Token {
    private Flag flags;
    private String name;
    private float displayName;
    private String img;
    private String tint;
    private float width;
    private float height;
    private float scale;
    private boolean mirrorX;
    private boolean mirrorY;
    private boolean lockRotation;
    private float rotation;
    private boolean vision;
    private float dimSight;
    private float brightSight;
    private float dimLight;
    private float brightLight;
    private float sightAngle;
    private float lightAngle;
    private String lightColor;
    private float lightAlpha;
    private LightAnimation lightAnimation;
    private String actorId;
    private boolean actorLink;
    private float disposition;
    private float displayBars;
    private Bar bar1;
    private Bar bar2;
    private boolean randomImg;
}
