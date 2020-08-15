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

package de.kaiserpfalzedv.rpg.base;

import org.immutables.value.Value;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Simpifies some annotation processor configuration for Immutables.
 *
 * @author rlichti
 * @version 1.0.0 2020-08-14
 * @since 1.0.0 2020-08-14
 */

@Value.Style(
        // Detect names starting with underscore
        typeAbstract = "*",
        typeImmutable = "*Immutable",
        typeModifiable = "*Mutable",
        // Make generated public, leave underscored as package private
        visibility = Value.Style.ImplementationVisibility.PUBLIC
)
@Target(ElementType.TYPE)
public @interface Immutable {
}