/*
 * Copyright 2020 Kaiserpfalz EDV-Service, Roland T. Lichti
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package de.kaiserpfalzedv.rpg.traveller.solarsystem.data;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.github.zafarkhaja.semver.Version;
import de.kaiserpfalzedv.rpg.base.Immutable;
import de.kaiserpfalzedv.rpg.base.data.DataSet;
import org.immutables.value.Value;

/**
 * A solar system in the universe of traveller. It will be cartographed inside a
 * {@link SubSector} wich will be the owner of this solar system.
 *
 * @author rlichti {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 2020-08-12
 */
@Immutable
@Value.Immutable
@Value.Modifiable
@JsonSerialize(as = SolarSystemImmutable.class)
@JsonDeserialize(builder = SolarSystemImmutable.Builder.class)
public interface SolarSystem extends DataSet<SolarSystemData> {
    String KIND = "de.kaiserpfalz-edv.rpg.traveller/solar-system";
    Version VERSION = Version.valueOf("0.1.0");

    @Override
    @Value.Default
    default String kind() {
        return KIND;
    }

    @Override
    @Value.Default
    default Version version() {
        return VERSION;
    }
}
