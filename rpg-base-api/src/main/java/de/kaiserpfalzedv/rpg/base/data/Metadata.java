/*
 * Copyright (c) 2020 Kaiserpfalz EDV-Service, Roland T. Lichti
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package de.kaiserpfalzedv.rpg.base.data;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.kaiserpfalzedv.rpg.base.Immutable;
import de.kaiserpfalzedv.rpg.base.data.MetadataImmutable;
import de.kaiserpfalzedv.rpg.base.data.MetadataImmutable;
import io.smallrye.common.constraint.NotNull;
import io.smallrye.common.constraint.Nullable;
import org.immutables.value.Value;

import java.net.URL;
import java.util.Map;
import java.util.UUID;

/**
 * This is a generic metadata set for all types of datasets.
 *
 * @author rlichti
 * @version 1.0.0 2020-08-14
 * @since 1.0.0 2020-08-14
 */
@Immutable
@Value.Immutable
@Value.Modifiable
@JsonSerialize(as = MetadataImmutable.class)
@JsonDeserialize(builder = MetadataImmutable.Builder.class)
public interface Metadata {
    /**
     * @return a UUID of this dataset.
     */
    UUID uuid();

    @Value.Default
    default String scope() {
        return "./.";
    };

    /**
     * @return a version of this object.
     */
    @Nullable
    String version();

    /**
     * @return the authoritative URL for this dataset
     */
    @Nullable
    URL url();

    /**
     * The dataset may be part of another one or "owned" by another dataset.
     * @return the owning construct.
     */
    @Nullable
    BaseDataSet owner();

    /**
     * Technical annotations to this dataset.
     *
     * @return the annotations of this dataset.
     */
    @Nullable
    Map<String, String> annotations();

    /**
     * Every dataset can be labeled. These labels are free purpose for other
     * systems or users to use.
     *
     * @return the labels of the dataset
     */
    @Nullable
    Map<String, String> labels();
}
