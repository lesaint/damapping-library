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
import javax.annotation.Nullable;

/**
 * Interface implemented by all mapper mapping an enum to and from a String.
 *
 * @param <E> an enum type
 */
public interface StringEnumMapper<E extends Enum<E>> {
  /**
   * Maps the specified enum value to a String according to the behavior defined for the current mapper.
   *
   * @param enumValue a enum value or {@code null}
   *
   * @return a {@link String} or {@code null}
   */
  @Nullable
  String toString(@Nullable E enumValue);

  /**
   * Maps the specified enum value to a String according to the behavior defined for the current mapper.
   *
   * @param str a {@link String} or {@code null}
   *
   * @return a enum value of type E or {@code null}
   */
  @Nullable
  E toEnum(@Nullable String str);

  /**
   * Returns a mapper that will map an enum to and from a String the same way as the current mapper but will use a
   * String comparison that ignores case.
   *
   * @return a StringEnumMapper
   */
  @Nonnull
  StringEnumMapper<E> ignoreCase();

  /**
   * Returns a mapper that will map an enum to and from a String the same way as the current mapper but will in
   * addition (or in place) use the specified defaults when mapping from a enum.
   *
   * @param defaults a {@link MappingDefaults}
   *
   * @return a {@link StringEnumMapper}
   */
  @Nonnull
  StringEnumMapper<E> withEnumDefaults(@Nonnull MappingDefaults<E> defaults);

  /**
   * Returns a mapper that will map an enum to and from a String the same way as the current mapper but will in
   * addition return the specified String value when mapping from a {@code null} enum value.
   *
   * @param defaultValue the default String
   *
   * @return a StringEnumMapper
   *
   * @throws NullPointerException if the specified value is null
   * @see StringEnumMapper#toString(Enum)
   */
  @Nonnull
  StringEnumMapper<E> withDefault(@Nonnull String defaultValue);

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
   * @return a StringEnumMapper instance
   *
   * @throws NullPointerException if the specified value is null
   * @see StringEnumMapper#toString(Enum)
   */
  @Nonnull
  StringEnumMapper<E> withDefault(@Nonnull E defaultValueForAll);

  /**
   * Creates a new mapper that will map an enum to and from a String the same way as the current mapper but will in
   * addition return the specified enum value when mapping from a {@code null} String.
   *
   * @param nullDefaultValue the default enum value
   *
   * @return a StringEnumMapper instance
   *
   * @throws NullPointerException if the specified value is {@code null}
   * @see StringEnumMapper#toString(Enum)
   */
  @Nonnull
  StringEnumMapper<E> withNullDefault(@Nonnull E nullDefaultValue);

  /**
   * Creates a new mapper that will map an enum to and from a String the same way as the current mapper but will in
   * addition return the specified enum value when mapping from an empty String.
   *
   * @param emptyDefaultValue the default enum value
   *
   * @return a StringEnumMapper instance
   *
   * @throws NullPointerException if the specified value is {@code null}
   * @see StringEnumMapper#toString(Enum)
   */
  @Nonnull
  StringEnumMapper<E> withEmptyDefault(@Nonnull E emptyDefaultValue);

  /**
   * Creates a new mapper that will map an enum to and from a String the same way as the current mapper but will in
   * addition return the specified enum value when mapping from a String that doesn't match any value of the enum and
   * is neither {@code null} nor empty.
   *
   * @param unknownDefaultValue the default enum value
   *
   * @return a StringEnumMapper instance
   *
   * @throws NullPointerException if the specified value is {@code null}
   * @see StringEnumMapper#toString(Enum)
   */
  @Nonnull
  StringEnumMapper<E> withUnknownDefault(@Nonnull E unknownDefaultValue);
}
