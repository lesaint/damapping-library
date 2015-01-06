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
package fr.javatronic.damapping.toolkit.enums.impl;

import fr.javatronic.damapping.toolkit.MappingDefaults;
import fr.javatronic.damapping.toolkit.enums.ByNameEnumMapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static java.util.Objects.requireNonNull;

/**
* ByNameEnumMapperImpl -
*
* @author Sébastien Lesaint
*/
public class ByNameEnumMapperImpl<E extends Enum<E>> extends BaseByNameEnumMapper<E> {


  public ByNameEnumMapperImpl(@Nonnull Class<E> clazz) {
    super(clazz, null, null);
  }

  @Nullable
  @Override
  public E toEnum(@Nullable String str) {
    if (str == null) {
      return enumDefaults.whenNull();
    }

    if (str.isEmpty()) {
      return enumDefaults.whenEmpty();
    }

    for (E e : clazz.getEnumConstants()) {
      if (e.name().equals(str)) {
        return e;
      }
    }
    return enumDefaults.whenUnknown();
  }

  @Override
  @Nonnull
  public ByNameEnumMapper<E> ignoreCase() {
    return new IgnoreCaseByNameEnumMapper<E>(clazz, this.stringDefaults, this.enumDefaults);
  }

  @Nonnull
  @Override
  public ByNameEnumMapper<E> withStringDefaults(@Nonnull MappingDefaults<String> defaults) {
    requireNonNull(defaults, NUll_MAPPING_DEFAULTS_ERROR_MSG);
    return new IgnoreCaseByNameEnumMapper<E>(clazz, defaults, this.enumDefaults);
  }

  @Nonnull
  @Override
  public ByNameEnumMapper<E> withEnumDefaults(@Nonnull MappingDefaults<E> defaults) {
    requireNonNull(defaults, NUll_MAPPING_DEFAULTS_ERROR_MSG);
    return new IgnoreCaseByNameEnumMapper<E>(clazz, this.stringDefaults, defaults);
  }

  @Nonnull
  @Override
  public ByNameEnumMapper<E> withDefault(@Nonnull String defaultValue) {
    requireNonNull(defaultValue, NUll_DEFAULT_VALUE_ERROR_MSG);
    return new IgnoreCaseByNameEnumMapper<E>(clazz, MappingDefaults.defaultTo(defaultValue), this.enumDefaults);
  }

  @Nonnull
  @Override
  public ByNameEnumMapper<E> withDefault(@Nonnull E defaultValueForAll) {
    requireNonNull(defaultValueForAll, NUll_DEFAULT_VALUE_ERROR_MSG);
    return new IgnoreCaseByNameEnumMapper<E>(clazz, this.stringDefaults, MappingDefaults.defaultTo(defaultValueForAll));
  }

  @Nonnull
  @Override
  public ByNameEnumMapper<E> withNullDefault(@Nonnull E nullDefaultValue) {
    requireNonNull(nullDefaultValue, NUll_DEFAULT_VALUE_ERROR_MSG);
    return new IgnoreCaseByNameEnumMapper<E>(clazz, this.stringDefaults, this.enumDefaults.withNullDefault(
        nullDefaultValue
    ));
  }

  @Nonnull
  @Override
  public ByNameEnumMapper<E> withEmptyDefault(@Nonnull E emptyDefaultValue) {
    requireNonNull(emptyDefaultValue, NUll_DEFAULT_VALUE_ERROR_MSG);
    return new IgnoreCaseByNameEnumMapper<E>(clazz, this.stringDefaults, this.enumDefaults.withEmptyDefault(
        emptyDefaultValue
    ));
  }

  @Nonnull
  @Override
  public ByNameEnumMapper<E> withUnknownDefault(@Nonnull E unknownDefaultValue) {
    requireNonNull(unknownDefaultValue, NUll_DEFAULT_VALUE_ERROR_MSG);
    return new IgnoreCaseByNameEnumMapper<E>(clazz, this.stringDefaults, this.enumDefaults.withUnknownDefault(
        unknownDefaultValue
    ));
  }
}
