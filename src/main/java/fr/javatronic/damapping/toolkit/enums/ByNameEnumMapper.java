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

/**
 * A mapper that uses the {@link Enum#name()} method to match an enum to a String (and the other way around).
 * <p>
 * This mapper never throws an exception when mapping an enum value either from or to a String. By default, it will
 * return {@code null}. This behavior can be altered using the either of the {@link #withDefault(String)},
 * {@link #withDefault(Enum)}, {@link #withNullDefault(Enum)}, {@link #withEmptyDefault(Enum)} or {@link
 * #withUnknownDefault(Enum)} methods.
 * </p>
 *
 * @param <T> an enum type
 */
public interface ByNameEnumMapper<T extends Enum<T>> extends StringEnumMapper<T> {

  /**
   * Returns a mapper that will map an enum to and from a String the same way as the current mapper but will use a
   * String comparison that ignores case.
   *
   * @return a ByNameEnumMapper
   *
   * @throws NullPointerException if the specified value is null
   * @see ByNameEnumMapper#toString(Enum)
   */
  @Nonnull
  ByNameEnumMapper<T> ignoreCase();

  /**
   * Returns a mapper that will map an enum to and from a String the same way as the current mapper but will in
   * addition (or in place) use the specified defaults when mapping from a String.
   *
   * @param defaults a {@link fr.javatronic.damapping.toolkit.MappingDefaults}
   *
   * @return a {@link fr.javatronic.damapping.toolkit.enums.ByNameEnumMapper}
   */
  @Nonnull
  ByNameEnumMapper<T> withStringDefaults(@Nonnull MappingDefaults<String> defaults);

  /**
   * Returns a mapper that will map an enum to and from a String the same way as the current mapper but will in
   * addition (or in place) use the specified defaults when mapping from a enum.
   *
   * @param defaults a {@link fr.javatronic.damapping.toolkit.MappingDefaults}
   *
   * @return a {@link fr.javatronic.damapping.toolkit.enums.ByNameEnumMapper}
   */
  @Nonnull
  ByNameEnumMapper<T> withEnumDefaults(@Nonnull MappingDefaults<T> defaults);

  /**
   * Returns a mapper that will map an enum to and from a String the same way as the current mapper but will in
   * addition return the specified String value when mapping from a {@code null} enum value.
   *
   * @param defaultValue the default String
   *
   * @return a ByNameEnumMapper
   *
   * @throws NullPointerException if the specified value is null
   * @see ByNameEnumMapper#toString(Enum)
   */
  @Nonnull
  ByNameEnumMapper<T> withDefault(@Nonnull String defaultValue);

  /**
   * Returns a mapper that will map an enum to and from a String the same way as the current mapper but will in
   * addition return the specified enum value when mapping from a {@code null}, empty String or one that doesn't match
   * any enum value.
   * <p>
   * This is a shorthand equivalent to calling all three methods {@link #withNullDefault(Enum)},
   * {@link #withEmptyDefault(Enum)} and {@link #withUnknownDefault(Enum)} with the same enum value.
   * </p>
   *
   * @param defaultValueForAll the default enum value
   *
   * @return a ByNameEnumMapper instance
   *
   * @throws NullPointerException if the specified value is null
   * @see ByNameEnumMapper#toString(Enum)
   */
  @Nonnull
  ByNameEnumMapper<T> withDefault(@Nonnull T defaultValueForAll);

  /**
   * Creates a new mapper that will map an enum to and from a String the same way as the current mapper but will in
   * addition return the specified enum value when mapping from a {@code null} String.
   *
   * @param nullDefaultValue the default enum value
   *
   * @return a ByNameEnumMapper instance
   *
   * @throws NullPointerException if the specified value is {@code null}
   * @see ByNameEnumMapper#toString(Enum)
   */
  @Nonnull
  ByNameEnumMapper<T> withNullDefault(@Nonnull T nullDefaultValue);

  /**
   * Creates a new mapper that will map an enum to and from a String the same way as the current mapper but will in
   * addition return the specified enum value when mapping from an empty String.
   *
   * @param emptyDefaultValue the default enum value
   *
   * @return a ByNameEnumMapper instance
   *
   * @throws NullPointerException if the specified value is {@code null}
   * @see ByNameEnumMapper#toString(Enum)
   */
  @Nonnull
  ByNameEnumMapper<T> withEmptyDefault(@Nonnull T emptyDefaultValue);

  /**
   * Creates a new mapper that will map an enum to and from a String the same way as the current mapper but will in
   * addition return the specified enum value when mapping from a String that doesn't match any value of the enum and
   * is neither {@code null} nor empty.
   *
   * @param unknownDefaultValue the default enum value
   *
   * @return a ByNameEnumMapper instance
   *
   * @throws NullPointerException if the specified value is {@code null}
   * @see ByNameEnumMapper#toString(Enum)
   */
  @Nonnull
  ByNameEnumMapper<T> withUnknownDefault(@Nonnull T unknownDefaultValue);
}
