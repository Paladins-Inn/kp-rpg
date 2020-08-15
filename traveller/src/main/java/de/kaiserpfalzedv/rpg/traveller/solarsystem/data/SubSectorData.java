package de.kaiserpfalzedv.rpg.traveller.solarsystem.data;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.kaiserpfalzedv.rpg.base.Immutable;
import org.immutables.value.Value;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author rlichti
 * @version 1.0.0 2020-08-15
 * @since 1.0.0 2020-08-15
 */
@Immutable
@Value.Immutable
@Value.Modifiable
@JsonSerialize(as = SubSectorDataImmutable.class)
@JsonDeserialize(builder = SubSectorDataImmutable.Builder.class)
public interface SubSectorData extends Serializable {
    int GRID_X = 4;
    int GRID_Y = 4;


    SolarSystem[][] solarSystems();

    default String[] listSolarSystems() {
        ArrayList<String> result = new ArrayList<>(16);

        for (SolarSystem[] r : solarSystems()) {
            for (SolarSystem s : r) {
                if (s != null) {
                    result.add(s.metadata().name());
                }
            }
        }

        return result.toArray(new String[16]);
    }

    String description();
}
