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

import java.util.EnumMap;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import static java.util.Objects.requireNonNull;

/**
 * StringEnumMappers - This class provides the developer with a convenient way to create StringEnumMapper objects to map
 * an enum value to or from a String.
 * <p>
 * The StringEnumMapper fluent interface is fully implemented by objects created by this class and respects the
 * constraint of immutability defined by this interface.
 * </p>
 * <p>
 * StringEnumMappers supports creating mappers that uses the {@link Enum#name()} or the {@link Enum#toString()} methods
 * to map an enum value to and from a String or any implementation of the {@link BijectiveTransformer} interface.
 * </p>
 * <p>
 * All StringEnumMapper object creation starts with the syntax: {@code StringEnumMappers.map(SomeEnum.class)} followed
 * either by method {@code byName()}, {@code byToString()} or {@code by(BijectiveTransformer)}.
 * </p>
 * <p>
 * As enforced by the {@link StringEnumMapper} interface, all instances of {@link StringEnumMapper} are immutable and
 * therefor thread safe. They can be safely shared across processes. In fact, user are even encouraged to store these
 * objects in instance or static fields to save on the object creation cost.
 * </p>
 * <p>
 * <pre>
 * enum SomeEnum {
 *   FOO, BAR, ACME;
 *   {@literal @}Override
 *   public String toString() {
 *     return "_" + name();
 *   }
 * }
 *
 * StringEnumMapper<SomeEnum> nameMapper = StringEnumMappers.map(SomeEnum.class).byName();
 * StringEnumMapper<SomeEnum> toStringMapper = StringEnumMappers.map(SomeEnum.class).byToString();
 *
 * assertThat(nameMapper.toEnum("FOO")).isEqualTo(SomeEnum.FOO);
 * assertThat(nameMapper.toString(SomeEnum.FOO)).isEqualTo("FOO");
 * assertThat(toStringMapper.toString(SomeEnum.FOO)).isEqualTo("_FOO");
 * assertThat(toStringMapper.toEnum("_FOO")).isEqualTo(SomeEnum.FOO);
 *
 * assertThat(mapper.toEnum("foo")).isEqualTo(SomeEnum.FOO);
 * assertThat(mapper.toEnum("bar")).isEqualTo(SomeEnum.BAR);
 * assertThat(mapper.toEnum("acme")).isNull();
 *
 * assertThat(nameMapper.ignoreCase().toEnum("foO")).isEqualTo(SomeEnum.FOO);
 * assertThat(toStringMapper.ignoreCase().toEnum("_Foo")).isEqualTo(SomeEnum.FOO);
 *
 * // creates a mapper that uses the name() method, is case sensitive but also supports the lower case strings for
 * // enum values FOO and BAR (and only those)
 * StringEnumMapper<SomeEnum> mapper = StringEnumMappers.map(SomeEnum.class)
 *     .byName()
 *     .except("foo", SomeEnum.FOO)
 *     .except("bar", SomeEnum.BAR);
 *
 * assertThat(mapper.toEnum("foo")).isEqualTo(SomeEnum.FOO);
 * assertThat(mapper.toEnum("bar")).isEqualTo(SomeEnum.BAR);
 * assertThat(mapper.toEnum("acme")).isNull();
 * </pre>
 * </p>
 *
 * @author S&eacute;bastien Lesaint
 */
/*
 * TODO add shorthand for case-sensitive matching on name with optional such as Guava's <T extends Enum<T>>
 * Optional<T> getIfPresent(Class<T>, String)
 * TODO Java8 support: make BijectiveTransformer a Functional interface, add methods returning optional instead of
 * null? quid of defaults?
 * TODO add method StringEnumMapper#trim and a method to specify a custom string formatter
 */
public final class StringEnumMappers {
  private StringEnumMappers() {
    // prevents instantiation
  }

  public static <T extends Enum<T>> EnumMapperFactory<T> map(@Nonnull final Class<T> enumAClass) {
    return new EnumMapperFactoryImpl<>(enumAClass);
  }

  /**
   * Factory for base StringEnumMapper objects.
   *
   * @param <T> any enum type
   */
  public static interface EnumMapperFactory<T extends Enum<T>> {

    /**
     * Builds a mapper that maps an enum value to and from a String that matches Strings to the {@link Enum#name()}.
     *
     * @return a {@link StringEnumMapper} instance
     */
    @Nonnull
    StringEnumMapper<T> byName();

    /**
     * Builds a mapper that maps an enum value to and from a String that matches Strings to the {@link Enum#toString()}.
     *
     * @return a {@link StringEnumMapper} instance
     */
    @Nonnull
    StringEnumMapper<T> byToString();

    /**
     * Builds a mapper that maps an enum value to and from a String that matches Strings to the result of the
     * specified {@link BijectiveTransformer} instance.
     *
     * @param transformer a {@link BijectiveTransformer} instance
     *
     * @return a {@link StringEnumMapper} instance
     *
     * @throws NullPointerException if the specified {@link BijectiveTransformer} instance is {@code null}
     */
    @Nonnull
    StringEnumMapper<T> by(@Nonnull BijectiveTransformer<T> transformer);

  }

  /**
   * Implementation of EnumMapperFactory
   *
   * @param <T> any enum type
   */
  private static final class EnumMapperFactoryImpl<T extends Enum<T>> implements EnumMapperFactory<T> {
    private final Class<T> enumAClass;

    public EnumMapperFactoryImpl(Class<T> enumAClass) {
      this.enumAClass = enumAClass;
    }

    @Override
    @Nonnull
    public StringEnumMapper<T> byName() {
      return new BijectiveTransformerStringEnumMapper<>(enumAClass, new EnumNameFunction<T>(),
          StringEnumMapperDefaults.<T>defaultsToNull()
      );
    }

    @Nonnull
    @Override
    public StringEnumMapper<T> byToString() {
      return new BijectiveTransformerStringEnumMapper<>(enumAClass, new EnumToStringFunction<T>(),
          StringEnumMapperDefaults.<T>defaultsToNull()
      );
    }

    @Override
    @Nonnull
    public StringEnumMapper<T> by(@Nonnull BijectiveTransformer<T> transformer) {
      return new BijectiveTransformerStringEnumMapper<>(enumAClass, transformer,
          StringEnumMapperDefaults.<T>defaultsToNull()
      );
    }
  }

  /**
   * This class holds the defaults for a StringEnumMapper implementation as two MappingDefaults objects and implements
   * the StringEnumMapper methods to update defaults.
   *
   * @param <E> any enum type
   */
  @Immutable
  private static class StringEnumMapperDefaults<E> {
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
     * @param defaults a {@link MappingDefaults}
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
     * @param defaultValue a {@link MappingDefaults}
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
     * @param defaults a {@link MappingDefaults}
     *
     * @return a {@link StringEnumMapper}
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
     * @return a {@link StringEnumMapper}
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
     * @return a {@link StringEnumMapper}
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
     * @return a {@link StringEnumMapper}
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
     * @return a {@link StringEnumMapper}
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
     * @return a {@link StringEnumMapper}
     */
    public static <T extends Enum<T>> StringEnumMapperDefaults<T> defaultsToNull() {
      return new StringEnumMapperDefaults<>(
          MappingDefaults.<String>defaultToNull(),
          MappingDefaults.<T>defaultToNull()
      );
    }
  }

  private static abstract class AbstractStringEnumMapper<T extends Enum<T>>
      implements StringEnumMapper<T> {
    @Nonnull
    protected final Class<T> clazz;
    @Nonnull
    protected final StringEnumMapperDefaults<T> defaults;

    protected AbstractStringEnumMapper(@Nonnull Class<T> clazz, @Nonnull StringEnumMapperDefaults<T> defaults) {
      this.clazz = requireNonNull(clazz);
      this.defaults = requireNonNull(defaults);
    }
  }

  /**
   * This class holds the backend maps for a StringEnumMapper that relies on maps to store mappings and implements the
   * StringEnumMapper methods to register exceptions.
   *
   * @param <E> any enum type
   */
  @Immutable
  private static class StringEnumMapperMaps<E extends Enum<E>> {
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
     * @param bijectiveTransformer a {@link BijectiveTransformer}
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

  /**
   * Abstract implementation of StringEnumMapper that implements holds mapping from an enum to a String and from a
   * String to an enum as a two immutable maps.
   * <p>
   * This class also provide an implementation for the method {@link StringEnumMapper#toString(Enum)} as it is the
   * same for both case sensitive and case insensitive mappers but leaves the implementation of method
   * {@link StringEnumMapper#toEnum(String)} to subclasses.
   * </p>
   *
   * @param <E> any enum type
   */
  private static abstract class AbstractMapStringEnumMapper<E extends Enum<E>>
      extends AbstractStringEnumMapper<E>
      implements StringEnumMapper<E> {

    @Nonnull
    protected final StringEnumMapperMaps<E> maps;

    protected AbstractMapStringEnumMapper(@Nonnull Class<E> clazz,
                                          @Nonnull StringEnumMapperMaps<E> maps,
                                          @Nonnull StringEnumMapperDefaults<E> defaults) {
      super(clazz, defaults);
      this.maps = requireNonNull(maps);
    }

    protected AbstractMapStringEnumMapper(@Nonnull Class<E> clazz,
                                          @Nonnull BijectiveTransformer<E> transformer,
                                          @Nonnull StringEnumMapperDefaults<E> defaults) {
      super(clazz, defaults);
      this.maps = new StringEnumMapperMaps<>(clazz, transformer);
    }

    /*=================*
     * Mapping methods *
     *=================*/

    @Override
    @Nullable
    public String toString(@Nullable E enumValue) {
      if (enumValue == null) {
        return defaults.getStringDefaults().whenNull();
      }

      String res = maps.getEnumToString().get(enumValue);
      if (res == null) {
        return defaults.getStringDefaults().whenUnknown();
      }
      return res;
    }
  }

  /**
   * Abstract class that extends AbstractMapStringEnumMapper by implementing a case sensitive
   * {@link StringEnumMapper#toEnum(String)} method.
   *
   * @param <E> any enum type
   */
  private static abstract class CaseSensitiveMapStringEnumMapper<E extends Enum<E>>
      extends AbstractMapStringEnumMapper<E>
      implements StringEnumMapper<E> {

    protected CaseSensitiveMapStringEnumMapper(@Nonnull Class<E> clazz,
                                               @Nonnull StringEnumMapperMaps<E> maps,
                                               @Nonnull StringEnumMapperDefaults<E> defaults) {
      super(clazz, maps, defaults);
    }

    protected CaseSensitiveMapStringEnumMapper(@Nonnull Class<E> clazz,
                                               @Nonnull BijectiveTransformer<E> transformer,
                                               @Nonnull StringEnumMapperDefaults<E> defaults) {
      super(clazz, transformer, defaults);
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

      E res = maps.getStringToEnum().get(str);
      if (res == null) {
        return defaults.getEnumDefaults().whenUnknown();
      }
      return res;
    }
  }

  /**
   * Abstract class that extends AbstractMapStringEnumMapper by implementing a case insensitive
   * {@link StringEnumMapper#toEnum(String)} method.
   *
   * @param <E> any enum type
   */
  private static abstract class CaseInsensitiveMapStringEnumMapper<E extends Enum<E>>
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
  }

  private static class CaseInsensitiveBijectiveTransformerStringEnumMapper<E extends Enum<E>>
      extends CaseInsensitiveMapStringEnumMapper<E>
      implements StringEnumMapper<E> {

    public CaseInsensitiveBijectiveTransformerStringEnumMapper(@Nonnull Class<E> clazz,
                                                               @Nonnull StringEnumMapperMaps<E> maps,
                                                               @Nonnull StringEnumMapperDefaults<E> defaults) {
      super(clazz, maps, defaults);
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
      return new CaseInsensitiveBijectiveTransformerStringEnumMapper<>(
          this.clazz,
          this.maps.except(enumValue),
          this.defaults
      );
    }

    @Override
    @Nonnull
    public StringEnumMapper<E> except(@Nonnull String str) {
      return new CaseInsensitiveBijectiveTransformerStringEnumMapper<>(
          this.clazz,
          this.maps.except(str),
          this.defaults
      );
    }

    @Override
    @Nonnull
    public StringEnumMapper<E> except(@Nonnull E enumValue, @Nullable String str) {
      return new CaseInsensitiveBijectiveTransformerStringEnumMapper<>(
          this.clazz,
          this.maps.except(enumValue, str),
          this.defaults
      );
    }

    @Override
    @Nonnull
    public StringEnumMapper<E> except(@Nonnull String str, @Nullable E enumValue) {
      return new CaseInsensitiveBijectiveTransformerStringEnumMapper<>(
          this.clazz,
          this.maps.except(str, enumValue),
          this.defaults
      );
    }

    @Override
    @Nonnull
    public StringEnumMapper<E> except(@Nonnull BiMapping<E, String> exceptionMapping) {
      return new CaseInsensitiveBijectiveTransformerStringEnumMapper<>(
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
      return new CaseInsensitiveBijectiveTransformerStringEnumMapper<>(
          this.clazz,
          this.maps,
          this.defaults.withStringDefaults(defaults)
      );
    }

    @Override
    @Nonnull
    public StringEnumMapper<E> withDefault(@Nonnull String defaultValue) {
      return new CaseInsensitiveBijectiveTransformerStringEnumMapper<>(
          this.clazz,
          this.maps,
          this.defaults.withDefault(defaultValue)
      );
    }

    @Override
    @Nonnull
    public StringEnumMapper<E> withNullDefault(@Nonnull String nullDefaultValue) {
      return new CaseInsensitiveBijectiveTransformerStringEnumMapper<>(
          this.clazz,
          this.maps,
          this.defaults.withNullDefault(nullDefaultValue)
      );
    }

    @Override
    @Nonnull
    public StringEnumMapper<E> withUnknownDefault(@Nonnull String unknownDefaultValue) {
      return new CaseInsensitiveBijectiveTransformerStringEnumMapper<>(
          this.clazz,
          this.maps,
          this.defaults.withUnknownDefault(unknownDefaultValue)
      );
    }

    @Override
    @Nonnull
    public StringEnumMapper<E> withEnumDefaults(@Nonnull MappingDefaults<E> defaults) {
      return new CaseInsensitiveBijectiveTransformerStringEnumMapper<>(
          this.clazz,
          this.maps,
          this.defaults.withEnumDefaults(defaults)
      );
    }

    @Override
    @Nonnull
    public StringEnumMapper<E> withDefault(@Nonnull E defaultValueForAll) {
      return new CaseInsensitiveBijectiveTransformerStringEnumMapper<>(
          this.clazz,
          this.maps,
          this.defaults.withDefault(defaultValueForAll)
      );
    }

    @Override
    @Nonnull
    public StringEnumMapper<E> withNullDefault(@Nonnull E nullDefaultValue) {
      return new CaseInsensitiveBijectiveTransformerStringEnumMapper<>(
          this.clazz,
          this.maps,
          this.defaults.withNullDefault(nullDefaultValue)
      );
    }

    @Override
    @Nonnull
    public StringEnumMapper<E> withEmptyDefault(@Nonnull E emptyDefaultValue) {
      return new CaseInsensitiveBijectiveTransformerStringEnumMapper<>(
          this.clazz,
          this.maps,
          this.defaults.withEmptyDefault(emptyDefaultValue)
      );
    }

    @Override
    @Nonnull
    public StringEnumMapper<E> withUnknownDefault(@Nonnull E unknownDefaultValue) {
      return new CaseInsensitiveBijectiveTransformerStringEnumMapper<>(
          this.clazz,
          this.maps,
          this.defaults.withUnknownDefault(unknownDefaultValue)
      );
    }
  }

  private static class BijectiveTransformerStringEnumMapper<E extends Enum<E>>
      extends CaseSensitiveMapStringEnumMapper<E>
      implements StringEnumMapper<E> {

    private BijectiveTransformerStringEnumMapper(@Nonnull Class<E> clazz,
                                                 @Nonnull StringEnumMapperMaps<E> maps,
                                                 @Nonnull StringEnumMapperDefaults<E> defaults) {
      super(clazz, maps, defaults);
    }

    public BijectiveTransformerStringEnumMapper(@Nonnull Class<E> clazz,
                                                @Nonnull BijectiveTransformer<E> transformer,
                                                @Nonnull StringEnumMapperDefaults<E> defaults) {
      super(clazz, new StringEnumMapperMaps<>(clazz, transformer), defaults);
    }

    /*=============*
     * case switch *
     *=============*/
    @Override
    @Nonnull
    public StringEnumMapper<E> ignoreCase() {
      return new CaseInsensitiveBijectiveTransformerStringEnumMapper<>(clazz, maps, defaults);
    }

    /*====================*
     * Mapping exceptions *
     *====================*/

    @Override
    @Nonnull
    public StringEnumMapper<E> except(@Nonnull E enumValue) {
      return new BijectiveTransformerStringEnumMapper<>(
          this.clazz,
          this.maps.except(enumValue),
          this.defaults
      );
    }

    @Override
    @Nonnull
    public StringEnumMapper<E> except(@Nonnull String str) {
      return new BijectiveTransformerStringEnumMapper<>(
          this.clazz,
          this.maps.except(str),
          this.defaults
      );
    }

    @Override
    @Nonnull
    public StringEnumMapper<E> except(@Nonnull E enumValue, @Nonnull String str) {
      return new BijectiveTransformerStringEnumMapper<>(
          this.clazz,
          this.maps.except(enumValue, str),
          this.defaults
      );
    }

    @Override
    @Nonnull
    public StringEnumMapper<E> except(@Nonnull String str, @Nonnull E enumValue) {
      return new BijectiveTransformerStringEnumMapper<>(
          this.clazz,
          this.maps.except(str, enumValue),
          this.defaults
      );
    }

    @Override
    @Nonnull
    public StringEnumMapper<E> except(@Nonnull BiMapping<E, String> exceptionMapping) {
      return new BijectiveTransformerStringEnumMapper<>(
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
      return new BijectiveTransformerStringEnumMapper<>(
          this.clazz,
          this.maps,
          this.defaults.withStringDefaults(defaults)
      );
    }

    @Override
    @Nonnull
    public StringEnumMapper<E> withDefault(@Nonnull String defaultValue) {
      return new BijectiveTransformerStringEnumMapper<>(
          this.clazz,
          this.maps,
          this.defaults.withDefault(defaultValue)
      );
    }

    @Override
    @Nonnull
    public StringEnumMapper<E> withNullDefault(@Nonnull String nullDefaultValue) {
      return new BijectiveTransformerStringEnumMapper<>(
          this.clazz,
          this.maps,
          this.defaults.withNullDefault(nullDefaultValue)
      );
    }

    @Override
    @Nonnull
    public StringEnumMapper<E> withUnknownDefault(@Nonnull String unknownDefaultValue) {
      return new BijectiveTransformerStringEnumMapper<>(
          this.clazz,
          this.maps,
          this.defaults.withUnknownDefault(unknownDefaultValue)
      );
    }

    @Override
    @Nonnull
    public StringEnumMapper<E> withEnumDefaults(@Nonnull MappingDefaults<E> defaults) {
      return new BijectiveTransformerStringEnumMapper<>(
          this.clazz,
          this.maps,
          this.defaults.withEnumDefaults(defaults)
      );
    }

    @Override
    @Nonnull
    public StringEnumMapper<E> withDefault(@Nonnull E defaultValueForAll) {
      return new BijectiveTransformerStringEnumMapper<>(
          this.clazz,
          this.maps,
          this.defaults.withDefault(defaultValueForAll)
      );
    }

    @Override
    @Nonnull
    public StringEnumMapper<E> withNullDefault(@Nonnull E nullDefaultValue) {
      return new BijectiveTransformerStringEnumMapper<>(
          this.clazz,
          this.maps,
          this.defaults.withNullDefault(nullDefaultValue)
      );
    }

    @Override
    @Nonnull
    public StringEnumMapper<E> withEmptyDefault(@Nonnull E emptyDefaultValue) {
      return new BijectiveTransformerStringEnumMapper<>(
          this.clazz,
          this.maps,
          this.defaults.withEmptyDefault(emptyDefaultValue)
      );
    }

    @Override
    @Nonnull
    public StringEnumMapper<E> withUnknownDefault(@Nonnull E unknownDefaultValue) {
      return new BijectiveTransformerStringEnumMapper<>(
          this.clazz,
          this.maps,
          this.defaults.withUnknownDefault(unknownDefaultValue)
      );
    }
  }

  /**
   * A BijectiveTransformer that uses the name method of the specified enum value.
   *
   * @param <E> any enum type
   */
  private static class EnumNameFunction<E extends Enum<E>> implements BijectiveTransformer<E> {
    @Override
    @Nonnull
    public String apply(@Nonnull E input) {
      return input.name();
    }
  }

  /**
   * A BijectiveTransformer that uses the name method of the specified enum value.
   *
   * @param <E> any enum type
   */
  private static class EnumToStringFunction<E extends Enum<E>> implements BijectiveTransformer<E> {
    @Override
    @Nonnull
    public String apply(@Nonnull E input) {
      return input.toString();
    }
  }
}
