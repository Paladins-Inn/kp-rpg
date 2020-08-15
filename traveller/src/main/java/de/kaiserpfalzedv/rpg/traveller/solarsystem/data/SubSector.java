package de.kaiserpfalzedv.rpg.traveller.solarsystem.data;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.kaiserpfalzedv.rpg.base.Immutable;
import de.kaiserpfalzedv.rpg.base.data.DataSet;
import org.immutables.value.Value;

/**
 * A subsector containing {@link SolarSystem}s and owned by a {@link Sector}.
 *
 * @author rlichti
 * @version 1.0.0 2020-08-15
 * @since 1.0.0 2020-08-15
 */
@Immutable
@Value.Immutable
@Value.Modifiable
@JsonSerialize(as = SubSectorImmutable.class)
@JsonDeserialize(builder = SubSectorImmutable.Builder.class)
public interface SubSector extends DataSet<SubSectorData> {
}
