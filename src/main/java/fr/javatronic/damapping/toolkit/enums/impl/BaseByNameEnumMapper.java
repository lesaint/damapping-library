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

import static com.google.common.base.MoreObjects.firstNonNull;
import static java.util.Objects.requireNonNull;

/**
* BaseByNameEnumMapper -
*
* @author Sébastien Lesaint
*/
abstract class BaseByNameEnumMapper<E extends Enum<E>>
    implements ByNameEnumMapper<E> {
  protected static final String NUll_DEFAULT_VALUE_ERROR_MSG = "When specifying a default value, " +
      "the value can not be null";
  protected static final String NUll_MAPPING_DEFAULTS_ERROR_MSG = "provided " + MappingDefaults.class.getSimpleName()
      + " object can not be null";

  @Nonnull
  protected final Class<E> clazz;
  @Nonnull
  protected final MappingDefaults<String> stringDefaults;
  @Nonnull
  protected final MappingDefaults<E> enumDefaults;

  public BaseByNameEnumMapper(@Nonnull Class<E> clazz,
                              @Nullable MappingDefaults<String> stringDefaults,
                              @Nullable MappingDefaults<E> enumDefaults) {
    this.clazz = requireNonNull(clazz);
    this.stringDefaults = firstNonNull(stringDefaults, MappingDefaults.<String>defaultToNull());
    this.enumDefaults = firstNonNull(enumDefaults, MappingDefaults.<E>defaultToNull());
  }

  @Nullable
  @Override
  public String toString(@Nullable E enumValue) {
    if (enumValue == null) {
      return stringDefaults.whenNull();
    }

    return enumValue.name();
  }
}
