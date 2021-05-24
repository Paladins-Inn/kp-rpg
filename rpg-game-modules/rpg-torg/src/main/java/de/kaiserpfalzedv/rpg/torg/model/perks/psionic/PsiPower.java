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

package de.kaiserpfalzedv.rpg.torg.model.perks.psionic;

import de.kaiserpfalzedv.commons.core.resources.Resource;
import de.kaiserpfalzedv.rpg.torg.About;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * PsiPower --
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0  2021-05-23
 */
@SuperBuilder(setterPrefix = "with", toBuilder = true)
@AllArgsConstructor
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "A single spell definition.")
public class PsiPower extends Resource<PsiPowerData> {
    public static final String KIND = "PsiPower";
    public static final String VERSION = "v1";
    public static final String NAMESPACE = About.TORG_NAMESPACE;
}
