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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Interface implemented by all mapper mapping an enum to and from a String.
 *
 * @param <E> an enum type
 */
public interface StringEnumMapper<E extends Enum<E>> {
  /*=================*
   * Mapping methods *
   *=================*/

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

  /*=============*
   * case switch *
   *=============*/

  /**
   * Returns a mapper that will map an enum to and from a String the same way as the current mapper but will use a
   * String comparison that ignores case.
   *
   * @return a StringEnumMapper
   */
  @Nonnull
  StringEnumMapper<E> ignoreCase();

  /*====================*
   * Mapping exceptions *
   *====================*/

  /**
   * Creates a new StringEnumMapper instance which is the same as the current one except that it overrides the
   * mapping from the specified enum value to the specified String or {@code null}.
   * <p>
   * The Enum parameter can not be {@code null}. To define the mapping from the {@code null} enum value to a String,
   * use {@link #withDefault(String)}.
   * </p>
   * <p>
   * This method is used to define a <strong>unidirectional</strong> mapping from an enum value to a String or
   * {@code null}. To define a mapping from and to an enum value from and to a String, use
   * {@link #except(fr.javatronic.damapping.toolkit.BiMapping)}.
   * </p>
   *
   * @param enumValue an enum value
   * @param str       a {@link String} or {@code null}
   *
   * @return a {@link StringEnumMapper}
   *
   * @throws java.lang.NullPointerException if enumValue is {@code null}
   * @see #withDefault(String)
   * @see #except(fr.javatronic.damapping.toolkit.BiMapping)
   */
  @Nonnull
  StringEnumMapper<E> except(@Nonnull E enumValue, @Nullable String str);

  /**
   * Creates a new StringEnumMapper instance which is the same as the current one except that it overrides (or defines)
   * the mapping from the specified String value to the specified enum value or {@code null}.
   * <p>
   * The String parameter can not be {@code null}. To define the mapping from the {@code null} String to an enum
   * value, use {@link #withNullDefault(Enum)}
   * </p>
   * <p>
   * This method is used to define a <strong>unidirectional</strong> mapping from a String to an enum value or
   * {@code null}. To define a mapping from and to an enum value from and to a String, use
   * {@link #except(fr.javatronic.damapping.toolkit.BiMapping)}.
   * </p>
   *
   * @param str a {@link String}
   * @param e   an enum value or {@code null}
   *
   * @return a {@link StringEnumMapper}
   *
   * @throws java.lang.NullPointerException if str is {@code null}
   * @see #withNullDefault(Enum)
   * @see #except(fr.javatronic.damapping.toolkit.BiMapping)
   */
  @Nonnull
  StringEnumMapper<E> except(@Nonnull String str, @Nullable E e);

  /**
   * Creates a new StringEnumMapper instance which is the same as the current one except that it overrides
   * the mapping to and from the specified String value to and from the specified enum value.
   *
   * @param exceptionMapping a bidirectional mapping as a {@link BiMapping}
   *
   * @return a {@link StringEnumMapper}
   *
   * @throws java.lang.NullPointerException if exceptionMapping is {@code null}
   */
  @Nonnull
  StringEnumMapper<E> except(@Nonnull BiMapping<E, String> exceptionMapping);

  /*==========*
   * defaults *
   *==========*/

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
   * addition (or in place) use the specified defaults when mapping from a String.
   *
   * @param defaults a {@link MappingDefaults}
   *
   * @return a {@link StringEnumMapper}
   */
  @Nonnull
  StringEnumMapper<E> withEnumDefaults(@Nonnull MappingDefaults<E> defaults);

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
   * @throws NullPointerException if defaultValueForAll is {@code null}
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
   * @throws NullPointerException if nullDefaultValue is {@code null}
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
   * @throws NullPointerException if emptyDefaultValue is {@code null}
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
   * @throws NullPointerException if unknownDefaultValue is {@code null}
   * @see StringEnumMapper#toString(Enum)
   */
  @Nonnull
  StringEnumMapper<E> withUnknownDefault(@Nonnull E unknownDefaultValue);
}
