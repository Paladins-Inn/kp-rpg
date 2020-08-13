/*
 * Copyright 2020 Kaiserpfalz EDV-Service, Roland T. Lichti
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package de.kaiserpfalzedv.rpg.base.dice;

import java.io.Serializable;

/**
 * @author rlichti {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 2020-08-12
 */
public interface Die {
    /**
     * a single die roll.
     * @return result of the single die roll.
     */
    int roll();

    /**
     * the result of multiple dice rolls.
     * @param number the number of dice rolls.
     * @return ann array of Integer containing the results. The first element contains the sum, starting with index 1
     * the results of the single rolls are returned.
     */
    Integer[] roll(final int number);

    /**
     * The maximum number this die can generate.
     * @return the maximum number on this die.
     */
    int getMax();
}
