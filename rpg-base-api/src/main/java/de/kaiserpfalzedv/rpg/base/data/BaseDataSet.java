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
import org.immutables.value.Value;

/**
 * A generic base definition for datasets. This base does not contain any data
 * but may be used as a pointer to some data.
 *
 * @author rlichti
 * @version 1.0.0 2020-08-15
 * @since 1.0.0 2020-08-15
 */
public interface BaseDataSet {
    /**
     * The type of dataset. Needs to be unique.
     * @return the type of this dataset.
     */
    String kind();

    /**
     * The version of this dataset defintion. Some services may support multiple
     * versions of this kind.
     * @return the version of the dataset defintion.
     */
    String version();

    /**
     * The metadata of the dataset.
     *
     * @return the metadata for this dataset.
     */
    Metadata metadata();
}
