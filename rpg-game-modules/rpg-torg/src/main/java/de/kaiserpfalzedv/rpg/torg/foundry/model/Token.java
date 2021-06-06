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
    private String displayName;
    private String img;
    private String tint;
    private int width;
    private int height;
    private int scale;
    private boolean mirrorX;
    private boolean mirrorY;
    private boolean lockRotation;
    private int rotation;
    private boolean vision;
    private int dimSight;
    private int brightSight;
    private int dimLight;
    private int brightLight;
    private int sightAngle;
    private int lightAngle;
    private String lightColor;
    private int lightAlpha;
    private LightAnimation lightAnimation;
    private String actorId;
    private boolean actorLink;
    private int disposition;
    private int displayBars;
    private Bar bar1;
    private Bar bar2;
    private boolean randomImg;
}
