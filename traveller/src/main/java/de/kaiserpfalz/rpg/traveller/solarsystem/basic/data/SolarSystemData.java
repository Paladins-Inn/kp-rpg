package de.kaiserpfalz.rpg.traveller.solarsystem.basic.data;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.kaiserpfalzedv.rpg.base.Immutable;
import org.immutables.value.Value;

import java.io.Serializable;

/**
 * @author rlichti
 * @version 1.0.0 2020-08-15
 * @since 1.0.0 2020-08-15
 */
@Immutable
@Value.Immutable
@JsonSerialize(as = SolarSystemDataImmutable.class)
@JsonDeserialize(builder = SolarSystemDataImmutable.Builder.class)
public interface SolarSystemData extends Serializable {

}
