package de.kaiserpfalzedv.rpg.traveller.solarsystem.data;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.kaiserpfalzedv.rpg.base.Immutable;
import de.kaiserpfalzedv.rpg.base.data.DataSet;
import org.immutables.value.Value;

/**
 * A sun in the universe of traveller. It is cartographed inside a
 * {@link SolarSystem} which will be the owner of the sun.
 * @author rlichti
 * @version 1.0.0 2020-08-15
 * @since 1.0.0 2020-08-15
 */
@Immutable
@Value.Immutable
@Value.Modifiable
@JsonSerialize(as = SunImmutable.class)
@JsonDeserialize(builder = SunImmutable.Builder.class)
public interface Sun extends DataSet<SunData>{

}
