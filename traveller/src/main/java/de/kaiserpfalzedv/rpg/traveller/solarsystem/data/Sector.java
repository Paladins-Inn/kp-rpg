package de.kaiserpfalzedv.rpg.traveller.solarsystem.data;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.kaiserpfalzedv.rpg.base.Immutable;
import de.kaiserpfalzedv.rpg.base.data.DataSet;
import org.immutables.value.Value;

/**
 * A sector in the traveller universe. It holds 16 {@link SubSector}s in a
 * 4 by 4 grid.
 *
 * @author rlichti
 * @version 1.0.0 2020-08-15
 * @since 1.0.0 2020-08-15
 */
@Immutable
@Value.Immutable
@Value.Modifiable
@JsonSerialize(as = SectorImmutable.class)
@JsonDeserialize(builder = SectorImmutable.Builder.class)
public interface Sector extends DataSet<SectorData> {
}
