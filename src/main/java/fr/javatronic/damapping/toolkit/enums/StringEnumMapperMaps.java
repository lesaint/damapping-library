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

import java.util.EnumMap;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import static java.util.Objects.requireNonNull;

/**
 * This class holds the backend maps for a StringEnumMapper that relies on maps to store mappings and implements the
 * StringEnumMapper except methods.
 *
 * @param <E> any enum type
 */
@Immutable
class StringEnumMapperMaps<E extends Enum<E>> {
  private static final String NULL_ENUM_VALUE_ERROR_MESSAGE = "Can not create an except for a null enum value";
  private static final String NULL_STRING_ERROR_MESSAGE = "Can not create an except for a null String";

  @Nonnull
  private final ImmutableMap<E, String> enumToString;
  @Nonnull
  private final ImmutableMap<String, E> stringToEnum;

  private StringEnumMapperMaps(@Nonnull ImmutableMap<E, String> enumToString,
                               @Nonnull ImmutableMap<String, E> stringToEnum) {
    this.enumToString = enumToString;
    this.stringToEnum = stringToEnum;
  }

  /**
   * Creates a new instance of StringEnumMapperMaps which backing maps will be populated from the result of the
   * specified BijectiveTransformer.
   *
   * @param clazz                an enum type class
   * @param bijectiveTransformer a {@link fr.javatronic.damapping.toolkit.enums.BijectiveTransformer}
   */
  public StringEnumMapperMaps(@Nonnull Class<E> clazz,
                              @Nonnull BijectiveTransformer<E> bijectiveTransformer) {
    EnumMap<E, String> enumStrMap = Maps.newEnumMap(clazz);
    ImmutableMap.Builder<String, E> strEnumBuilder = ImmutableMap.builder();
    for (E enumConstant : clazz.getEnumConstants()) {
      String str = requireNonNull(
          bijectiveTransformer.apply(enumConstant),
          "String representation of value " + enumConstant + " can not be null"
      );
      enumStrMap.put(enumConstant, str);
      strEnumBuilder.put(str, enumConstant);
    }
    this.enumToString = Maps.immutableEnumMap(enumStrMap);
    this.stringToEnum = strEnumBuilder.build();
  }

  @Nonnull
  public ImmutableMap<E, String> getEnumToString() {
    return enumToString;
  }

  @Nonnull
  public ImmutableMap<String, E> getStringToEnum() {
    return stringToEnum;
  }

  @Nonnull
  public StringEnumMapperMaps<E> except(@Nonnull E enumValue) {
    requireNonNull(enumValue, NULL_ENUM_VALUE_ERROR_MESSAGE);
    return new StringEnumMapperMaps<>(
        exceptEnumMap(enumValue, null),
        this.stringToEnum
    );
  }

  @Nonnull
  public StringEnumMapperMaps<E> except(@Nonnull String str) {
    requireNonNull(str, NULL_STRING_ERROR_MESSAGE);
    return new StringEnumMapperMaps<>(
        this.enumToString,
        exceptStringMap(str, null)
    );
  }

  @Nonnull
  public StringEnumMapperMaps<E> except(@Nonnull E enumValue, @Nonnull String str) {
    requireNonNull(enumValue, NULL_ENUM_VALUE_ERROR_MESSAGE);
    requireNonNull(str, "method except(E, String) can not be used to remove a mapping from an enum value. Use method except(E)");
    return new StringEnumMapperMaps<>(
        exceptEnumMap(enumValue, str),
        this.stringToEnum
    );
  }

  @Nonnull
  private ImmutableMap<E, String> exceptEnumMap(E enumValue, @Nullable String str) {
    EnumMap<E, String> enumMap = Maps.newEnumMap(this.enumToString);
    if (str == null) {
      enumMap.remove(enumValue);
    }
    else {
      enumMap.put(enumValue, str);
    }
    return Maps.immutableEnumMap(enumMap);
  }

  @Nonnull
  public StringEnumMapperMaps<E> except(@Nonnull String str, @Nullable E e) {
    requireNonNull(str, NULL_STRING_ERROR_MESSAGE);
    requireNonNull(e, "method except(String, E) can not be used to remove a mapping from an String. Use method except(String)");
    return new StringEnumMapperMaps<>(
        this.enumToString,
        exceptStringMap(str, e)
    );
  }

  @Nonnull
  private ImmutableMap<String, E> exceptStringMap(String str, @Nullable E e) {
    Map<String, E> map = Maps.newHashMap(this.stringToEnum);
    if (e == null) {
      map.remove(str);
    }
    else {
      map.put(str, e);
    }
    return ImmutableMap.copyOf(map);
  }

  @Nonnull
  public StringEnumMapperMaps<E> except(@Nonnull BiMapping<E, String> exceptionMapping) {
    requireNonNull(exceptionMapping, "BiMapping can not be null");
    return new StringEnumMapperMaps<>(
        exceptEnumMap(exceptionMapping.from(), exceptionMapping.to()),
        exceptStringMap(exceptionMapping.to(), exceptionMapping.from())
    );
  }
}
