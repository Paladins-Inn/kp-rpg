package de.kaiserpfalzedv.rpg.traveller.solarsystem.data;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.kaiserpfalzedv.rpg.base.Immutable;
import org.immutables.value.Value;

import java.io.Serializable;

/**
 * The data of a world. A world may be a planet or a moon of a planet.
 *
 * @author rlichti
 * @version 1.0.0 2020-08-15
 * @since 1.0.0 2020-08-15
 */
@Immutable
@Value.Immutable
@Value.Modifiable
@JsonSerialize(as = WorldDataImmutable.class)
@JsonDeserialize(builder = WorldDataImmutable.Builder.class)
public interface WorldData extends Serializable {
    /**
     * @return The orbit of this world.
     */
    int orbit();

    /**
     * Will be 0 for planets and a positive integer for moons.
     * @return the orbit of the moon
     */
    default int subOrbit() {
        return 0;
    }

    /**
     * @return The UPP of the world.
     */
    int[] upp();
    /**
     * @return short description of the world.
     */
    String description();
}
