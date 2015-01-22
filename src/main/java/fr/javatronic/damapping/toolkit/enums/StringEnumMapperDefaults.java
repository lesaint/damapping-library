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

import fr.javatronic.damapping.toolkit.MappingDefaults;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import static java.util.Objects.requireNonNull;

/**
 * This class holds the defaults for a StringEnumMapper implementation as two MappingDefaults objects and implements
 * the StringEnumMapper methods to update defaults.
 *
 * @param <E> any enum type
 */
@Immutable
class StringEnumMapperDefaults<E> {
  private static final String NUll_MAPPING_DEFAULTS_ERROR_MSG = "provided " + MappingDefaults.class.getSimpleName()
      + " object can not be null";
  private static final String NUll_DEFAULT_VALUE_ERROR_MSG = "When specifying a default value, " +
      "the value can not be null";

  @Nonnull
  private final MappingDefaults<String> stringDefaults;
  @Nonnull
  private final MappingDefaults<E> enumDefaults;

  public StringEnumMapperDefaults(@Nonnull MappingDefaults<String> defaults,
                                  @Nonnull MappingDefaults<E> enumDefaults) {
    this.stringDefaults = requireNonNull(defaults);
    this.enumDefaults = requireNonNull(enumDefaults);
  }

  @Nonnull
  public MappingDefaults<String> getStringDefaults() {
    return stringDefaults;
  }

  @Nonnull
  public MappingDefaults<E> getEnumDefaults() {
    return enumDefaults;
  }

  /**
   * Returns a StringEnumMapperDefaults with the same enum defaults as the current mapper but the specified String
   * defaults.
   *
   * @param defaults a {@link fr.javatronic.damapping.toolkit.MappingDefaults}
   *
   * @return a {@link StringEnumMapperDefaults}
   */
  @Nonnull
  public StringEnumMapperDefaults<E> withStringDefaults(@Nonnull MappingDefaults<String> defaults) {
    requireNonNull(defaults, NUll_MAPPING_DEFAULTS_ERROR_MSG);
    return new StringEnumMapperDefaults<>(defaults, this.enumDefaults);
  }

  /**
   * Returns a StringEnumMapperDefaults with the same enum defaults as the current mapper but the specified String
   * default.
   *
   * @param defaultValue a {@link fr.javatronic.damapping.toolkit.MappingDefaults}
   *
   * @return a {@link StringEnumMapperDefaults}
   */
  @Nonnull
  public StringEnumMapperDefaults<E> withDefault(@Nonnull String defaultValue) {
    requireNonNull(defaultValue, NUll_DEFAULT_VALUE_ERROR_MSG);
    return new StringEnumMapperDefaults<>(
        MappingDefaults.defaultTo(defaultValue),
        this.enumDefaults
    );
  }

  /**
   * Returns a StringEnumMapperDefaults with the same String defaults and the same enum defaults as the current mapper
   * but will use the specified value when the input enum value is {@code null}.
   *
   * @param nullDefaultValue a {@link String}
   *
   * @return a {@link StringEnumMapperDefaults}
   */
  @Nonnull
  public StringEnumMapperDefaults<E> withNullDefault(@Nonnull String nullDefaultValue) {
    requireNonNull(nullDefaultValue, NUll_DEFAULT_VALUE_ERROR_MSG);
    return new StringEnumMapperDefaults<>(
        this.stringDefaults.withNullDefault(nullDefaultValue),
        this.enumDefaults
    );
  }

  /**
   * Returns a StringEnumMapperDefaults with the same String defaults and the same enum defaults as the current mapper
   * but will use the specified value when the input enum value is unknown.
   *
   * @param unknownDefaultValue a {@link String}
   *
   * @return a {@link StringEnumMapperDefaults}
   */
  @Nonnull
  public StringEnumMapperDefaults<E> withUnknownDefault(@Nonnull String unknownDefaultValue) {
    requireNonNull(unknownDefaultValue, NUll_DEFAULT_VALUE_ERROR_MSG);
    return new StringEnumMapperDefaults<>(
        this.stringDefaults.withUnknownDefault(unknownDefaultValue),
        this.enumDefaults
    );
  }

  /**
   * Returns a StringEnumMapperDefaults with the same String defaults as the current mapper but the specified enum
   * defaults.
   *
   * @param defaults a {@link fr.javatronic.damapping.toolkit.MappingDefaults}
   *
   * @return a {@link fr.javatronic.damapping.toolkit.enums.StringEnumMapper}
   */
  @Nonnull
  public StringEnumMapperDefaults<E> withEnumDefaults(@Nonnull MappingDefaults<E> defaults) {
    requireNonNull(defaults, NUll_MAPPING_DEFAULTS_ERROR_MSG);
    return new StringEnumMapperDefaults<>(this.stringDefaults, defaults);
  }

  /**
   * Returns a StringEnumMapperDefaults with the same String defaults as the current mapper but will use the
   * specified value for all enum defaults.
   *
   * @param defaultValueForAll an enum value
   *
   * @return a {@link fr.javatronic.damapping.toolkit.enums.StringEnumMapper}
   */
  @Nonnull
  public StringEnumMapperDefaults<E> withDefault(@Nonnull E defaultValueForAll) {
    requireNonNull(defaultValueForAll, NUll_DEFAULT_VALUE_ERROR_MSG);
    return new StringEnumMapperDefaults<>(
        this.stringDefaults,
        MappingDefaults.defaultTo(defaultValueForAll)
    );
  }

  /**
   * Returns a StringEnumMapperDefaults with the same String defaults and the same enum defaults as the current mapper
   * but will use the specified value when the input String is {@code null}.
   *
   * @param nullDefaultValue an enum value
   *
   * @return a {@link fr.javatronic.damapping.toolkit.enums.StringEnumMapper}
   */
  @Nonnull
  public StringEnumMapperDefaults<E> withNullDefault(@Nonnull E nullDefaultValue) {
    requireNonNull(nullDefaultValue, NUll_DEFAULT_VALUE_ERROR_MSG);
    return new StringEnumMapperDefaults<>(
        this.stringDefaults,
        this.enumDefaults.withNullDefault(nullDefaultValue)
    );
  }

  /**
   * Returns a StringEnumMapperDefaults with the same String defaults and the same enum defaults as the current mapper
   * but will use the specified value when the input String is empty.
   *
   * @param emptyDefaultValue an enum value
   *
   * @return a {@link fr.javatronic.damapping.toolkit.enums.StringEnumMapper}
   */
  @Nonnull
  public StringEnumMapperDefaults<E> withEmptyDefault(@Nonnull E emptyDefaultValue) {
    requireNonNull(emptyDefaultValue, NUll_DEFAULT_VALUE_ERROR_MSG);
    return new StringEnumMapperDefaults<>(
        this.stringDefaults,
        this.enumDefaults.withEmptyDefault(emptyDefaultValue)
    );
  }

  /**
   * Returns a StringEnumMapperDefaults with the same String defaults and the same enum defaults as the current mapper
   * but will use the specified value when the input String is unknown.
   *
   * @param unknownDefaultValue an enum value
   *
   * @return a {@link fr.javatronic.damapping.toolkit.enums.StringEnumMapper}
   */
  @Nonnull
  public StringEnumMapperDefaults<E> withUnknownDefault(@Nonnull E unknownDefaultValue) {
    requireNonNull(unknownDefaultValue, NUll_DEFAULT_VALUE_ERROR_MSG);
    return new StringEnumMapperDefaults<>(
        this.stringDefaults,
        this.enumDefaults.withUnknownDefault(unknownDefaultValue)
    );
  }

  /**
   * Creates a StringEnumMapperDefaults with {@code null} as String and enum defaults.
   *
   * @param <T> any enum type
   *
   * @return a {@link fr.javatronic.damapping.toolkit.enums.StringEnumMapper}
   */
  public static <T extends Enum<T>> StringEnumMapperDefaults<T> defaultsToNull() {
    return new StringEnumMapperDefaults<>(
        MappingDefaults.<String>defaultToNull(),
        MappingDefaults.<T>defaultToNull()
    );
  }
}
