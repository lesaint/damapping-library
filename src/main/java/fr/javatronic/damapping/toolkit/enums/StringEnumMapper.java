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
import javax.annotation.concurrent.Immutable;

/**
 * StringEnumMapper - Defines a fluent interface to configure and use a mapper mapping an enum to and from a String.
 * <p>
 * StringEnumMapper implementations should not raise exception whenever they fail to perform the mapping. They should
 * return a "default value".
 * <br/>
 * Cases where a default value should be returned are limited and implementation could support each case individually or
 * globally.
 * <br/>
 * Implementations can choose to provide only limited support for default values, in which case StringEnumMapper methods
 * which are not supported should raise a {@link java.lang.UnsupportedOperationException}.
 * </p>
 * <p>
 * Here are all the default value cases:
 * <ul>
 * <li>when mapping from an enum to a String: the input is {@code null} or the input is unknown/unsupported</li>
 * <li>
 * when mapping from a String to an enum: the input is {@code null} or the input is empty or the input is {@code null}
 * </li>
 * </ul>
 * </p>
 * <p>
 * Prime methods define mapping an Enum value from a String ({@link #toEnum(String)}) and a String to an enum value
 * ({@link #toString(Enum)}).
 * </p>
 * <p>
 * Other methods allow to create new StringEnumMapper instances from the current one which have slightly different
 * behavior:
 * <ul>
 * <li>
 * use other default enum value returned when mapping from a {@code null}, empty or unknown String:
 * <br/>
 * (see {@link StringEnumMapper#withDefault(Enum)}, {@link StringEnumMapper#withNullDefault(Enum)}, {@link
 * StringEnumMapper#withEmptyDefault(Enum)}, {@link StringEnumMapper#withUnknownDefault(Enum)} and {@link
 * StringEnumMapper#withEnumDefaults(MappingDefaults)})
 * </li>
 * <li>
 * use other default String value returned when mapping from a {@code null} or unknown enum value
 * <br/>
 * (see {@link StringEnumMapper#withDefault(String)}, {@link StringEnumMapper#withNullDefault(String)}),
 * {@link StringEnumMapper#withUnknownDefault(String)} and {@link StringEnumMapper#withStringDefaults(MappingDefaults)})
 * </li>
 * <li>
 * use a case sensitive String comparison instead of a strict one
 * <br/>
 * (see {@link #ignoreCase()})
 * </li>
 * <li>
 * add extra mappings from String to enum value or disable existing ones
 * <br/>
 * (see {@link #except(String, Enum)}
 * </li>
 * <li>
 * override mapping from enum to String
 * <br/>
 * (see {@link #except(Enum, String)})
 * </li>
 * <li>
 * remove mapping from a String
 * <br/>
 * (see {@link #except(String)})
 * </li>
 * <li>
 * remove mapping from an enum value
 * <br/>
 * (see {@link #except(Enum)})
 * </li>
 * <li>
 * override mapping from enum to String and from String to enum (aka. bijective mapping)
 * <br/>
 * (see {@link #except(fr.javatronic.damapping.toolkit.BiMapping)})
 * </li>
 * </ul>.
 * </p>
 * <p>
 * This interface is primarily implemented by objects created by class
 * {@link fr.javatronic.damapping.toolkit.enums.StringEnumMappers} and behavior customisation methods can be used
 * sequentially using methods cascading (as one would expect from a fluent interface) as demonstrated below:
 * <pre>
 * StringEnumMapper.StringEnumMapper mapper = StringEnumMapper.map(Bar.class)
 *     .byName()
 *     .ignoreCase()
 *     .withDefault("no_bar_value")
 *     .withNullDefault(Bar.ACME)
 *     .withEmptyDefault(Bar.VISEN)
 * </pre>
 * </p>
 * <p>
 * <strong>implementation constraints:</strong><br/>
 * Implementations of this interface must be immutable.
 * </p>
 *
 * @param <E> any enum type
 *
 * @see fr.javatronic.damapping.toolkit.enums.StringEnumMappers
 */
@Immutable
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
   * Creates a new StringEnumMapper instance which is the same as the current one except that the mapping from the
   * specified enum value has been removed.
   *
   * @param enumValue an enum value
   *
   * @return a {@link StringEnumMapper}
   *
   * @throws java.lang.NullPointerException if enumValue is {@code null}
   */
  @Nonnull
  StringEnumMapper<E> except(@Nonnull E enumValue);

  /**
   * Creates a new StringEnumMapper instance which is the same as the current one except that the mapping from the
   * specified String has been removed.
   *
   * @param str a {@link String}
   *
   * @return a {@link StringEnumMapper}
   *
   * @throws java.lang.NullPointerException if str is {@code null}
   */
  @Nonnull
  StringEnumMapper<E> except(@Nonnull String str);

  /**
   * Creates a new StringEnumMapper instance which is the same as the current one except that it overrides the
   * mapping from the specified enum value to the specified String or {@code null}.
   * <p>
   * The Enum parameter can not be {@code null}. To define the mapping from the {@code null} enum value to a String,
   * use {@link #withNullDefault(String)}.
   * </p>
   * <p>
   * The String parameter can not be {@code null} either. To remove the mapping from a specific enum value, use
   * {@link #except(Enum)}.
   * </p>
   * <p>
   * This method is used to define a <strong>unidirectional</strong> mapping from an enum value to a String or
   * {@code null}. To define a mapping from and to an enum value from and to a String, use
   * {@link #except(fr.javatronic.damapping.toolkit.BiMapping)}.
   * </p>
   *
   * @param enumValue an enum value
   * @param str       a {@link String}
   *
   * @return a {@link StringEnumMapper}
   *
   * @throws java.lang.NullPointerException if enumValue or str is {@code null}
   * @see #withNullDefault(String)
   * @see #except(Enum)
   * @see #except(fr.javatronic.damapping.toolkit.BiMapping)
   */
  @Nonnull
  StringEnumMapper<E> except(@Nonnull E enumValue, @Nonnull String str);

  /**
   * Creates a new StringEnumMapper instance which is the same as the current one except that it overrides (or defines)
   * the mapping from the specified String value to the specified enum value or {@code null}.
   * <p>
   * The String parameter can not be {@code null}. To define the mapping from the {@code null} String to an enum
   * value, use {@link #withNullDefault(Enum)}.
   * </p>
   * <p>
   * The enum value can not be {@code null}. To remove a mapping from a String, use {@link #except(String)}.
   * </p>
   * <p>
   * This method is used to define a <strong>unidirectional</strong> mapping from a String to an enum value or
   * {@code null}. To define a mapping from and to an enum value from and to a String, use
   * {@link #except(fr.javatronic.damapping.toolkit.BiMapping)}.
   * </p>
   *
   * @param str       a {@link String}
   * @param enumValue an enum value
   *
   * @return a {@link StringEnumMapper}
   *
   * @throws java.lang.NullPointerException if str or enumValue is {@code null}
   * @see #withNullDefault(Enum)
   * @see #except(String)
   * @see #except(fr.javatronic.damapping.toolkit.BiMapping)
   */
  @Nonnull
  StringEnumMapper<E> except(@Nonnull String str, @Nonnull E enumValue);

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
   * addition (or in place) use the specified defaults when mapping from an enum value.
   *
   * @param defaults a {@link MappingDefaults}
   *
   * @return a {@link StringEnumMapper}
   */
  @Nonnull
  StringEnumMapper<E> withStringDefaults(@Nonnull MappingDefaults<String> defaults);

  /**
   * Creates a new mapper that will map an enum to and from a String the same way as the current mapper but will in
   * addition return the specified String value when mapping from a {@code null} enum value.
   *
   * @param nullDefaultValue the default String
   *
   * @return a StringEnumMapper instance
   *
   * @throws NullPointerException if nullDefaultValue is {@code null}
   * @see StringEnumMapper#toEnum(String)
   */
  @Nonnull
  StringEnumMapper<E> withNullDefault(@Nonnull String nullDefaultValue);

  /**
   * Creates a new mapper that will map an enum to and from a String the same way as the current mapper but will in
   * addition return the specified String value when mapping from an enum value that is not mapped to any String and is
   * not {@code null}.
   *
   * @param unknownDefaultValue the default String
   *
   * @return a StringEnumMapper instance
   *
   * @throws NullPointerException if unknownDefaultValue is {@code null}
   * @see StringEnumMapper#toEnum(String)
   */
  @Nonnull
  StringEnumMapper<E> withUnknownDefault(@Nonnull String unknownDefaultValue);

  /**
   * Returns a mapper that will map an enum to and from a String the same way as the current mapper but will in
   * addition return the specified String value when mapping from a {@code null} or unknown enum value.
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
