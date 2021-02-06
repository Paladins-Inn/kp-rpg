/*
 * Copyright (c) 2021 Kaiserpfalz EDV-Service, Roland T. Lichti.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package de.kaiserpfalzedv.rpg.core.resources;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.immutables.value.Value;

import java.beans.Transient;
import java.io.Serializable;
import java.util.*;

/**
 * The basic data for every card.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.0.0 2021-01-06
 */
@SuppressWarnings("unused")
@Value.Immutable
@Value.Modifiable
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonSerialize(as = ImmutableDefaultResourceSpec.class)
@JsonDeserialize(builder = ImmutableDefaultResourceSpec.Builder.class)
@Schema(name = "DefaultResourceSpec", description = "A standardized resource.")
public interface DefaultResourceSpec extends Serializable {
    /**
     * @return A pointer to a picture of this user.
     */
    @Schema(name = "picture", description = "The resource address of the picture of this card.")
    Optional<ResourcePointer> getPicture();

    /**
     * @return A description of this user.
     */
    @Schema(name = "description", description = "A description of the card.")
    Optional<String> getDescription();


    /**
     * @return Hashmap of configuration properties.
     */
    @Schema(name = "properties", description = "A map of plugin properties for this user.")
    @Value.Default
    default Map<String, String> getProperties() {
        return new HashMap<>();
    }

    /**
     * Returns a property.
     *
     * @param key The unique key of the property within the user dataset.
     * @return The property saved with the user.
     */
    @Transient
    @JsonIgnore
    default Optional<String> getProperty(final String key) {
        return Optional.ofNullable(getProperties().get(key));
    }

    /**
     * Returns an array of property names which should be saved by a {@link de.kaiserpfalzedv.rpg.core.store.StoreService}
     * implementation. You should really overwrite it when needed.
     *
     * @return the names of the default properties of this resource.
     */
    @Value.Default
    @Transient
    @JsonIgnore
    default String[] getDefaultProperties() {
        throw new UnsupportedOperationException();
    }

    /**
     * Reads a resource pointer from a property.
     *
     * @param key The name of the property.
     * @return The resource pointer.
     * @throws IllegalStateException    If the property can't be converted.
     * @throws IllegalArgumentException If the UUID of the pointer can't be read from the property.
     * @throws NoSuchElementException   There is no such property.
     */
    @Transient
    @JsonIgnore
    default Optional<ResourcePointer> getResourcePointer(final String key) {
        try {
            String property = getProperty(key).orElseThrow();

            return Optional.of(convertStringToResourcePointer(property));
        } catch (NoSuchElementException e) {
            return Optional.empty();
        }
    }

    @Transient
    @JsonIgnore
    default List<ResourcePointer> getResourcePointers(final String key) {
        try {
            String property = getProperty(key).orElseThrow();

            String[] data = property.split(",");

            ArrayList<ResourcePointer> result = new ArrayList<>(data.length);
            for (String p : data) {
                result.add(convertStringToResourcePointer(p));
            }

            return result;
        } catch (NoSuchElementException e) {
            return new ArrayList<>();
        }
    }

    private ResourcePointer convertStringToResourcePointer(final String property) {
        String[] data = property.split("/", 5);
        if (data.length != 5) {
            throw new IllegalStateException("Invalid property for resource pointers: " + property);
        }

        return ImmutableResourcePointer.builder()
                .kind(data[0])
                .apiVersion(data[1])

                .namespace(data[2])
                .name(data[3])

                .uid(UUID.fromString(data[4]))

                .build();
    }

    /**
     * Saves a resource pointer as property.
     *
     * @param key     The name of the property.
     * @param pointer the pointer to save.
     */
    @Transient
    @JsonIgnore
    default void saveResourcePointer(final String key, final ResourcePointer pointer) {
        String data = convertResourcePointerToString(pointer);

        getProperties().put(key, data);
    }

    private String convertResourcePointerToString(ResourcePointer pointer) {
        return new StringJoiner("/")
                .add(pointer.getKind())
                .add(pointer.getApiVersion())
                .add(pointer.getNamespace())
                .add(pointer.getName())
                .add(pointer.getUid().toString())
                .toString();
    }

    @Transient
    @JsonIgnore
    default void saveResourcePointers(final String key, final Collection<ResourcePointer> pointers) {
        StringJoiner data = new StringJoiner(",");

        pointers.forEach(p -> data.add(convertResourcePointerToString(p)));

        getProperties().put(key, data.toString());
    }
}
