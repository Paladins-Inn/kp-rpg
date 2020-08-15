package de.kaiserpfalzedv.rpg.traveller.solarsystem.data;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.kaiserpfalzedv.rpg.base.Immutable;
import de.kaiserpfalzedv.rpg.base.data.DataSet;
import org.immutables.value.Value;

/**
 * A world in the universe of traveller. It is part of a subsector which will
 * be modelled as owner of this world in the metadata.
 *
 * The real data will be found in the data structure.
 *
 * @author rlichti
 * @version 1.0.0 2020-08-15
 * @since 1.0.0 2020-08-15
 */
@Immutable
@Value.Immutable
@Value.Modifiable
@JsonSerialize(as = WorldImmutable.class)
@JsonDeserialize(builder = WorldImmutable.Builder.class)
public interface World extends DataSet<WorldData> {
}
