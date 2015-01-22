/**
 * Copyright (C) 2014 Sébastien Lesaint (http://www.javatronic.fr/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.javatronic.damapping.toolkit.enums;

import fr.javatronic.damapping.toolkit.BiMapping;
import fr.javatronic.damapping.toolkit.MappingDefaults;

import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Map based implementation of StringEnumMapper that uses case insensitive string comparison.
 *
 * @param <E> any enum type
 */
class CaseInsensitiveMapStringEnumMapper<E extends Enum<E>>
    extends AbstractMapStringEnumMapper<E>
    implements StringEnumMapper<E> {

  protected CaseInsensitiveMapStringEnumMapper(@Nonnull Class<E> clazz,
                                               @Nonnull StringEnumMapperMaps<E> maps,
                                               @Nonnull StringEnumMapperDefaults<E> defaults) {
    super(clazz, maps, defaults);
  }

  /*=================*
   * Mapping methods *
   *=================*/
  @Override
  @Nullable
  public E toEnum(@Nullable String str) {
    if (str == null) {
      return defaults.getEnumDefaults().whenNull();
    }

    if (str.isEmpty()) {
      return defaults.getEnumDefaults().whenEmpty();
    }

    for (Map.Entry<String, E> entry : maps.getStringToEnum().entrySet()) {
      if (entry.getKey().compareToIgnoreCase(str) == 0) {
        return entry.getValue();
      }
    }
    return defaults.getEnumDefaults().whenUnknown();
  }

  /*=============*
   * case switch *
   *=============*/
  @Override
  @Nonnull
  public StringEnumMapper<E> ignoreCase() {
    return this;
  }

  /*====================*
   * Mapping exceptions *
   *====================*/

  @Override
  @Nonnull
  public StringEnumMapper<E> except(@Nonnull E enumValue) {
    return new CaseInsensitiveMapStringEnumMapper<>(
        this.clazz,
        this.maps.except(enumValue),
        this.defaults
    );
  }

  @Override
  @Nonnull
  public StringEnumMapper<E> except(@Nonnull String str) {
    return new CaseInsensitiveMapStringEnumMapper<>(
        this.clazz,
        this.maps.except(str),
        this.defaults
    );
  }

  @Override
  @Nonnull
  public StringEnumMapper<E> except(@Nonnull E enumValue, @Nullable String str) {
    return new CaseInsensitiveMapStringEnumMapper<>(
        this.clazz,
        this.maps.except(enumValue, str),
        this.defaults
    );
  }

  @Override
  @Nonnull
  public StringEnumMapper<E> except(@Nonnull String str, @Nullable E enumValue) {
    return new CaseInsensitiveMapStringEnumMapper<>(
        this.clazz,
        this.maps.except(str, enumValue),
        this.defaults
    );
  }

  @Override
  @Nonnull
  public StringEnumMapper<E> except(@Nonnull BiMapping<E, String> exceptionMapping) {
    return new CaseInsensitiveMapStringEnumMapper<>(
        this.clazz,
        this.maps.except(exceptionMapping),
        this.defaults
    );
  }

  /*==========*
   * defaults *
   *==========*/
  @Override
  @Nonnull
  public StringEnumMapper<E> withStringDefaults(@Nonnull MappingDefaults<String> defaults) {
    return new CaseInsensitiveMapStringEnumMapper<>(
        this.clazz,
        this.maps,
        this.defaults.withStringDefaults(defaults)
    );
  }

  @Override
  @Nonnull
  public StringEnumMapper<E> withDefault(@Nonnull String defaultValue) {
    return new CaseInsensitiveMapStringEnumMapper<>(
        this.clazz,
        this.maps,
        this.defaults.withDefault(defaultValue)
    );
  }

  @Override
  @Nonnull
  public StringEnumMapper<E> withNullDefault(@Nonnull String nullDefaultValue) {
    return new CaseInsensitiveMapStringEnumMapper<>(
        this.clazz,
        this.maps,
        this.defaults.withNullDefault(nullDefaultValue)
    );
  }

  @Override
  @Nonnull
  public StringEnumMapper<E> withUnknownDefault(@Nonnull String unknownDefaultValue) {
    return new CaseInsensitiveMapStringEnumMapper<>(
        this.clazz,
        this.maps,
        this.defaults.withUnknownDefault(unknownDefaultValue)
    );
  }

  @Override
  @Nonnull
  public StringEnumMapper<E> withEnumDefaults(@Nonnull MappingDefaults<E> defaults) {
    return new CaseInsensitiveMapStringEnumMapper<>(
        this.clazz,
        this.maps,
        this.defaults.withEnumDefaults(defaults)
    );
  }

  @Override
  @Nonnull
  public StringEnumMapper<E> withDefault(@Nonnull E defaultValueForAll) {
    return new CaseInsensitiveMapStringEnumMapper<>(
        this.clazz,
        this.maps,
        this.defaults.withDefault(defaultValueForAll)
    );
  }

  @Override
  @Nonnull
  public StringEnumMapper<E> withNullDefault(@Nonnull E nullDefaultValue) {
    return new CaseInsensitiveMapStringEnumMapper<>(
        this.clazz,
        this.maps,
        this.defaults.withNullDefault(nullDefaultValue)
    );
  }

  @Override
  @Nonnull
  public StringEnumMapper<E> withEmptyDefault(@Nonnull E emptyDefaultValue) {
    return new CaseInsensitiveMapStringEnumMapper<>(
        this.clazz,
        this.maps,
        this.defaults.withEmptyDefault(emptyDefaultValue)
    );
  }

  @Override
  @Nonnull
  public StringEnumMapper<E> withUnknownDefault(@Nonnull E unknownDefaultValue) {
    return new CaseInsensitiveMapStringEnumMapper<>(
        this.clazz,
        this.maps,
        this.defaults.withUnknownDefault(unknownDefaultValue)
    );
  }
}
