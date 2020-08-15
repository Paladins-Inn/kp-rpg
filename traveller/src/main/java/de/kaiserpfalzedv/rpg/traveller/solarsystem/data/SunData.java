package de.kaiserpfalzedv.rpg.traveller.solarsystem.data;

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
@Value.Modifiable
@JsonSerialize(as = SunDataImmutable.class)
@JsonDeserialize(builder = SunDataImmutable.Builder.class)
public interface SunData extends Serializable {
    /**
     * @return TRUE when this is the primary sun of a solar system.
     */
    boolean isPrimary();

    /**
     * @return TRUE when this a secondary or tertiary sun of a solar system.
     */
    @Value.Default
    default boolean isSecondary() {
        return !isPrimary();
    }

    /**
     * @return The spectral class of this sun.
     */
    int spectralClass();

    /**
     * @return The size of this sun.
     */
    int size();

    /**
     * @return the magnitude of this sun.
     */
    int magnitude();
}
