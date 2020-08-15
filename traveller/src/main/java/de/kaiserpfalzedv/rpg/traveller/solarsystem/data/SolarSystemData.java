package de.kaiserpfalzedv.rpg.traveller.solarsystem.data;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.kaiserpfalzedv.rpg.base.Immutable;
import org.immutables.value.Value;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Set;

/**
 * @author rlichti
 * @version 1.0.0 2020-08-15
 * @since 1.0.0 2020-08-15
 */
@Immutable
@Value.Immutable
@Value.Modifiable
@JsonSerialize(as = SolarSystemDataImmutable.class)
@JsonDeserialize(builder = SolarSystemDataImmutable.Builder.class)
public interface SolarSystemData extends Serializable {
    int GRID_X = 8;
    int GRID_Y = 10;

    Set<Sun> suns();
    Set<World> worlds();

    default String[] listWorlds() {
        ArrayList<String> result = new ArrayList<>(16);

        for (World w : worlds()) {
            result.add(w.metadata().name());
        }

        return result.toArray(new String[16]);
    }

    String description();
}
