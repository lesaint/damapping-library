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
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import static com.google.common.base.MoreObjects.firstNonNull;
import static java.util.Objects.requireNonNull;

/**
 * StringEnumMappers - This class provides the developer with a convenient way to create mappers to map an enum value to
 * or from a String and specify its behavior in a readable way.
 * <p>
 * Mappers without explicit behavior expressed:
 * <ul>
 * <li>never throw an exception but instead return a null value</li>
 * <li>use strict String comparison, ie. are based on {@link String#compareTo(String)}</li>
 * </ul>
 * </p>
 * <p>
 * The following behavior can be customized:
 * <ul>
 * <li>case insensitive String comparison can be used</li>
 * <li>String value returned when mapping from a {@code null} enum value, a specific value can be returned instead of
 * {@code null}</li>
 * <li>enum value returned from a {@code null}, empty or unknown String value can be customized as a whole or
 * selectively</li>
 * </ul>
 * </p>
 * <p>
 * Behavior customization is achieved using a meaningful method chaining, for example:
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
 * Most enum mappings are based on the name, sometimes on the value of toString(). These mappings have prime
 * support:
 * <pre>
 * assert StringEnumMapper.map(Bar.class).byName().toEnum("ACME") == Bar.ACME;
 * assert StringEnumMapper.map(Bar.class).byName().toString(Bar.ACME).equals("ACME");
 * assert StringEnumMapper.map(Bar.class).byToString().toEnum("bar_acme") == Bar.ACME;
 * assert StringEnumMapper.map(Bar.class).byToString().toString(Bar.ACME).equals("bar_acme");
 * </pre>
 * </p>
 * <p>
 * Any other String to enum value matching rule is supported using a custom transformation rule, for example:
 * <pre>
 * private static enum CustomBarToString implements StringEnumMapper.EnumToString&lt;EnumA&gt; {
 *   INSTANCE;
 *
 *  {@literal @}Override
 *  {@literal @}Nonnull
 *   public String transform({@literal @}Nonnull Bar enumValue) {
 *     switch (enumValue) {
 *       case ACME:
 *         return "Foo";
 *       default:
 *         throw new IllegalArgumentException("value is not supported in toString(), fix toString(): " + enumValue);
 *     }
 *   }
 * }
 *
 * assert StringEnumMapper.map(Bar.class).by(CustomBarToString.INSTANCE).toEnum("Foo") == Bar.ACME;
 * assert StringEnumMapper.map(Bar.class).by(CustomBarToString.INSTANCE).toString(Bar.ACME).equals("Foo");
 * </pre>
 * </p>
 * <p>
 * <strong>Note on thread-safety:</strong> mapper produced by this class are thread-safe and can safely be shared,
 * stored in class property and/or be static.
 * </p>
 *
 * @author Sébastien Lesaint
 */
/*
 * TODO add shorthand for case-sensitive matching on name with optional such as Guava's <T extends Enum<T>>
 * Optional<T> getIfPresent(Class<T>, String)
 * TODO Java8 support: make EnumToString a Functional interface, add methods returning optional instead of null? quid
 * of defaults?
 */
public final class StringEnumMappers {
  private StringEnumMappers() {
    // prevents instantiation
  }

  public static <T extends Enum<T>> EnumMapperFactory<T> map(@Nonnull final Class<T> enumAClass) {
    return new EnumMapperFactoryImpl<T>(enumAClass);
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
     * specified {@link EnumToString} instance.
     *
     * @param transformer a {@link EnumToString} instance
     *
     * @return a {@link StringEnumMapper} instance
     *
     * @throws NullPointerException if the specified {@link EnumToString} instance is {@code null}
     */
    @Nonnull
    StringEnumMapper<T> by(@Nonnull EnumToString<T> transformer);

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
    public StringEnumMapper<T> by(@Nonnull EnumToString<T> transformer) {
      return new StringEnumMapperImpl<T>(enumAClass, transformer);
    }

    @Override
    @Nonnull
    public StringEnumMapper<T> byName() {
      return new NameStringEnumMapper<T>(enumAClass, StringEnumMapperDefaults.<T>defaultsToNull());
    }

    @Nonnull
    @Override
    public StringEnumMapper<T> byToString() {
      return new ToStringStringEnumMapper<T>(enumAClass, StringEnumMapperDefaults.<T>defaultsToNull());
    }
  }

  /**
   * StringEnumMapperImpl - Implementation of StringEnumMapper interface which supports MappingDefaults and optionally
   * using case insensitive comparison to match a String and the string representations of an enum.
   */
  private static class StringEnumMapperImpl<E extends Enum<E>> implements StringEnumMapper<E> {
    private static final String NUll_DEFAULT_VALUE_ERROR_MSG = "When specifying a default value, " +
        "the value can not be null";
    private static final String NUll_MAPPING_DEFAULTS_ERROR_MSG = "provided " + MappingDefaults.class.getSimpleName()
        + " object can not be null";
    private static final String EXCEPTIONS_ARE_NOT_YET_IMPLEMENTED = "Exceptions are not yet implemented";

    @Nonnull
    private final Class<E> clazz;
    @Nonnull
    private final EnumToString<E> transformer;
    @Nonnull
    private final MappingDefaults<String> stringDefaults;
    @Nonnull
    private final MappingDefaults<E> enumDefaults;
    private final boolean ignoreCase;

    private StringEnumMapperImpl(@Nonnull Class<E> clazz, @Nonnull EnumToString<E> transformer) {
      this(clazz, transformer, null, null, false);
    }

    private StringEnumMapperImpl(@Nonnull Class<E> clazz,
                                 @Nonnull EnumToString<E> transformer,
                                 @Nullable MappingDefaults<String> stringDefaults,
                                 @Nullable MappingDefaults<E> enumDefaults,
                                 boolean ignoreCase) {
      this.clazz = requireNonNull(clazz);
      this.transformer = requireNonNull(transformer);
      this.stringDefaults = firstNonNull(stringDefaults, MappingDefaults.<String>defaultToNull());
      this.enumDefaults = firstNonNull(enumDefaults, MappingDefaults.<E>defaultToNull());
      this.ignoreCase = ignoreCase;
    }

    /*=================*
     * Mapping methods *
     *=================*/
    @Nullable
    @Override
    public String toString(@Nullable E enumValue) {
      if (enumValue == null) {
        return stringDefaults.whenNull();
      }

      return transformer.transform(enumValue);
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
        if (compare(transformer.transform(e), str) == 0) {
          return e;
        }
      }
      return enumDefaults.whenUnknown();
    }

    private int compare(String transform, String str) {
      if (ignoreCase) {
        return transform.compareToIgnoreCase(str);
      }
      return transform.compareTo(str);
    }

    /*=============*
     * case switch *
     *=============*/

    /**
     * Returns a mapper that will map an enum to and from a String the same way as the current mapper but will use a
     * String comparison that ignores case.
     *
     * @return the current StringEnumMapper if String comparison is already case insensitive, otherwise a new one
     */
    @Nonnull
    public StringEnumMapper<E> ignoreCase() {
      if (ignoreCase) {
        return this;
      }
      return new StringEnumMapperImpl<E>(clazz, transformer, stringDefaults, enumDefaults, true);
    }

    /*====================*
     * Mapping exceptions *
     *====================*/

    @Nonnull
    @Override
    public StringEnumMapper<E> except(@Nonnull E enumValue, @Nullable String str) {
      throw new UnsupportedOperationException(EXCEPTIONS_ARE_NOT_YET_IMPLEMENTED);
    }

    @Nonnull
    @Override
    public StringEnumMapper<E> except(@Nonnull String str, @Nullable E e) {
      throw new UnsupportedOperationException(EXCEPTIONS_ARE_NOT_YET_IMPLEMENTED);
    }

    @Nonnull
    @Override
    public StringEnumMapper<E> except(@Nonnull BiMapping<E, String> exceptionMapping) {
      throw new UnsupportedOperationException(EXCEPTIONS_ARE_NOT_YET_IMPLEMENTED);
    }

    /*==========*
         * defaults *
         *==========*/
    @Nonnull
    @Override
    public StringEnumMapper<E> withEnumDefaults(@Nonnull MappingDefaults<E> defaults) {
      requireNonNull(defaults, NUll_MAPPING_DEFAULTS_ERROR_MSG);
      return new StringEnumMapperImpl<E>(clazz, transformer, this.stringDefaults, defaults, ignoreCase);
    }

    @Nonnull
    @Override
    public StringEnumMapper<E> withDefault(@Nonnull String defaultValue) {
      requireNonNull(defaultValue, NUll_DEFAULT_VALUE_ERROR_MSG);
      return new StringEnumMapperImpl<E>(
          clazz,
          transformer,
          MappingDefaults.defaultTo(defaultValue),
          this.enumDefaults,
          ignoreCase
      );
    }

    @Nonnull
    @Override
    public StringEnumMapper<E> withDefault(@Nonnull E defaultValueForAll) {
      requireNonNull(defaultValueForAll, NUll_DEFAULT_VALUE_ERROR_MSG);
      return new StringEnumMapperImpl<E>(
          clazz,
          transformer,
          this.stringDefaults,
          MappingDefaults.defaultTo(defaultValueForAll),
          ignoreCase
      );
    }

    @Nonnull
    @Override
    public StringEnumMapper<E> withNullDefault(@Nonnull E nullDefaultValue) {
      requireNonNull(nullDefaultValue, NUll_DEFAULT_VALUE_ERROR_MSG);
      return new StringEnumMapperImpl<E>(
          clazz,
          transformer,
          this.stringDefaults,
          this.enumDefaults.withNullDefault(nullDefaultValue),
          ignoreCase
      );
    }

    @Nonnull
    @Override
    public StringEnumMapper<E> withEmptyDefault(@Nonnull E emptyDefaultValue) {
      requireNonNull(emptyDefaultValue, NUll_DEFAULT_VALUE_ERROR_MSG);
      return new StringEnumMapperImpl<E>(
          clazz,
          transformer,
          this.stringDefaults,
          this.enumDefaults.withEmptyDefault(emptyDefaultValue),
          ignoreCase
      );
    }

    @Nonnull
    @Override
    public StringEnumMapper<E> withUnknownDefault(@Nonnull E unknownDefaultValue) {
      requireNonNull(unknownDefaultValue, NUll_DEFAULT_VALUE_ERROR_MSG);
      return new StringEnumMapperImpl<E>(
          clazz,
          transformer,
          this.stringDefaults,
          this.enumDefaults.withUnknownDefault(unknownDefaultValue),
          ignoreCase
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
     * Returns a StringEnumMapperDefaults with the same enum defaults as the current mapper by the specified String
     * default.
     *
     * @param defaultValue a {@link MappingDefaults}
     *
     * @return a {@link StringEnumMapper}
     */
    @Nonnull
    public StringEnumMapperDefaults<E> withDefault(@Nonnull String defaultValue) {
      requireNonNull(defaultValue, NUll_DEFAULT_VALUE_ERROR_MSG);
      return new StringEnumMapperDefaults<E>(
          MappingDefaults.defaultTo(defaultValue),
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
      return new StringEnumMapperDefaults<E>(this.stringDefaults, defaults);
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
      return new StringEnumMapperDefaults<E>(
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
      return new StringEnumMapperDefaults<E>(
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
      return new StringEnumMapperDefaults<E>(
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
      return new StringEnumMapperDefaults<E>(
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
      return new StringEnumMapperDefaults<T>(
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
    @Nonnull
    private final ImmutableMap<E, String> enumToString;
    @Nonnull
    private final ImmutableMap<String, E> stringToEnum;

    private StringEnumMapperMaps(@Nonnull ImmutableMap<E, String> enumToString,
                                 @Nonnull ImmutableMap<String, E> stringToEnum) {
      this.enumToString = enumToString;
      this.stringToEnum = stringToEnum;
    }

    public StringEnumMapperMaps(@Nonnull Class<E> clazz,
                                @Nonnull Function<E, String> transformer) {
      EnumMap<E, String> enumStrMap = Maps.newEnumMap(clazz);
      ImmutableMap.Builder<String, E> strEnumBuilder = ImmutableMap.builder();
      for (E enumConstant : clazz.getEnumConstants()) {
        String str = requireNonNull(
            transformer.apply(enumConstant),
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
    public StringEnumMapperMaps<E> except(@Nonnull E enumValue, @Nullable String str) {
      requireNonNull(enumValue, "Can not create an except for a null enum value");
      return new StringEnumMapperMaps<>(
          exceptEnumMap(enumValue, str),
          this.stringToEnum
      );
    }

    @Nonnull
    private ImmutableMap<E, String> exceptEnumMap(E enumValue, String str) {
      EnumMap<E, String> enumMap = Maps.newEnumMap(this.enumToString);
      enumMap.put(enumValue, str);
      return Maps.immutableEnumMap(enumMap);
    }

    @Nonnull
    public StringEnumMapperMaps<E> except(@Nonnull String str, @Nullable E e) {
      requireNonNull(str, "Can not create an except for a null String");
      return new StringEnumMapperMaps<>(
          this.enumToString,
          excepStringMap(str, e)
      );
    }

    @Nonnull
    private ImmutableMap<String, E> excepStringMap(String str, E e) {
      HashMap<String, E> map = Maps.newHashMap(this.stringToEnum);
      map.put(str, e);
      return ImmutableMap.copyOf(map);
    }

    @Nonnull
    public StringEnumMapperMaps<E> except(@Nonnull BiMapping<E, String> exceptionMapping) {
      requireNonNull(exceptionMapping, "BiMapping can not be null");
      return new StringEnumMapperMaps<>(
          exceptEnumMap(exceptionMapping.from(), exceptionMapping.to()),
          excepStringMap(exceptionMapping.to(), exceptionMapping.from())
      );
    }
  }

  /**
   * Abstract implementation of StringEnumMapper that implements holds mapping from an enum to a String and from a
   * String to an enum as a two immutable maps.
   * <p>
   * This abstract class populates these maps using the Function returned by the abstract method {@link
   * #getEnumToStringTransformer()} assuming the mapping is bijective.
   * </p>
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
                                          @Nonnull StringEnumMapperDefaults<E> defaults) {
      super(clazz, defaults);
      this.maps = new StringEnumMapperMaps<>(clazz, getEnumToStringTransformer());
    }

    @Nonnull
    protected abstract Function<E, String> getEnumToStringTransformer();

    /*=================*
     * Mapping methods *
     *=================*/

    @Override
    @Nullable
    public String toString(@Nullable E enumValue) {
      if (enumValue == null) {
        return defaults.getStringDefaults().whenNull();
      }

      return maps.getEnumToString().get(enumValue);
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
                                               @Nonnull StringEnumMapperDefaults<E> defaults) {
      super(clazz, defaults);
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

  /**
   * Implementation of a StringEnumMapper that matches String to and from enum values using the enum's name method,
   * uses a case insensitive comparison to compare an input String to name() result and is backed by Maps.
   *
   * @param <E> any enum type
   */
  private static class NameInsensitiveStringEnumMapper<E extends Enum<E>>
      extends CaseInsensitiveMapStringEnumMapper<E>
      implements StringEnumMapper<E> {

    protected NameInsensitiveStringEnumMapper(@Nonnull Class<E> clazz,
                                              @Nonnull StringEnumMapperMaps<E> maps,
                                              @Nonnull StringEnumMapperDefaults<E> defaults) {
      super(clazz, maps, defaults);
    }

    @Override
    @Nonnull
    protected Function<E, String> getEnumToStringTransformer() {
      return new EnumNameFunction<E>();
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
    public StringEnumMapper<E> except(@Nonnull E enumValue, @Nullable String str) {
      return new NameInsensitiveStringEnumMapper<>(
          this.clazz,
          this.maps.except(enumValue, str),
          this.defaults
      );
    }

    @Override
    @Nonnull
    public StringEnumMapper<E> except(@Nonnull String str, @Nullable E e) {
      return new NameInsensitiveStringEnumMapper<>(
          this.clazz,
          this.maps.except(str, e),
          this.defaults
      );
    }

    @Override
    @Nonnull
    public StringEnumMapper<E> except(@Nonnull BiMapping<E, String> exceptionMapping) {
      return new NameInsensitiveStringEnumMapper<>(
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
    public StringEnumMapper<E> withEnumDefaults(@Nonnull MappingDefaults<E> defaults) {
      return new NameInsensitiveStringEnumMapper<>(
          this.clazz,
          this.maps,
          this.defaults.withEnumDefaults(defaults)
      );
    }

    @Nonnull
    @Override
    public StringEnumMapper<E> withDefault(@Nonnull String defaultValue) {
      return new NameInsensitiveStringEnumMapper<>(
          this.clazz,
          this.maps,
          this.defaults.withDefault(defaultValue)
      );
    }

    @Nonnull
    @Override
    public StringEnumMapper<E> withDefault(@Nonnull E defaultValueForAll) {
      return new NameInsensitiveStringEnumMapper<>(
          this.clazz,
          this.maps,
          this.defaults.withDefault(defaultValueForAll)
      );
    }

    @Nonnull
    @Override
    public StringEnumMapper<E> withNullDefault(@Nonnull E nullDefaultValue) {
      return new NameInsensitiveStringEnumMapper<>(
          this.clazz,
          this.maps,
          this.defaults.withNullDefault(nullDefaultValue)
      );
    }

    @Nonnull
    @Override
    public StringEnumMapper<E> withEmptyDefault(@Nonnull E emptyDefaultValue) {
      return new NameInsensitiveStringEnumMapper<>(
          this.clazz,
          this.maps,
          this.defaults.withEmptyDefault(emptyDefaultValue)
      );
    }

    @Nonnull
    @Override
    public StringEnumMapper<E> withUnknownDefault(@Nonnull E unknownDefaultValue) {
      return new NameInsensitiveStringEnumMapper<>(
          this.clazz,
          this.maps,
          this.defaults.withUnknownDefault(unknownDefaultValue)
      );
    }
  }

  /**
   * Implementation of a StringEnumMapper that matches String to and from enum values using the enum's name method,
   * uses a case sensitive comparison to compare an input String to name() result and is backed by Maps.
   *
   * @param <E> any enum type
   */
  private static class NameStringEnumMapper<E extends Enum<E>>
      extends CaseSensitiveMapStringEnumMapper<E>
      implements StringEnumMapper<E> {

    public NameStringEnumMapper(@Nonnull Class<E> clazz, @Nonnull StringEnumMapperDefaults<E> defaults) {
      super(clazz, defaults);
    }

    private NameStringEnumMapper(@Nonnull Class<E> clazz,
                                 @Nonnull StringEnumMapperMaps<E> maps,
                                 @Nonnull StringEnumMapperDefaults<E> defaults) {
      super(clazz, maps, defaults);
    }

    @Override
    @Nonnull
    protected Function<E, String> getEnumToStringTransformer() {
      return new EnumNameFunction<E>();
    }

    /*=============*
     * case switch *
     *=============*/
    @Override
    @Nonnull
    public StringEnumMapper<E> ignoreCase() {
      return new NameInsensitiveStringEnumMapper<>(clazz, maps, defaults);
    }

    /*====================*
     * Mapping exceptions *
     *====================*/

    @Override
    @Nonnull
    public StringEnumMapper<E> except(@Nonnull E enumValue, @Nullable String str) {
      return new NameStringEnumMapper<>(
          this.clazz,
          this.maps.except(enumValue, str),
          this.defaults
      );
    }

    @Override
    @Nonnull
    public StringEnumMapper<E> except(@Nonnull String str, @Nullable E e) {
      return new NameStringEnumMapper<>(
          this.clazz,
          this.maps.except(str, e),
          this.defaults
      );
    }

    @Override
    @Nonnull
    public StringEnumMapper<E> except(@Nonnull BiMapping<E, String> exceptionMapping) {
      return new NameStringEnumMapper<>(
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
    public StringEnumMapper<E> withEnumDefaults(@Nonnull MappingDefaults<E> defaults) {
      return new NameStringEnumMapper<>(
          this.clazz,
          this.maps,
          this.defaults.withEnumDefaults(defaults)
      );
    }

    @Override
    @Nonnull
    public StringEnumMapper<E> withDefault(@Nonnull String defaultValue) {
      return new NameStringEnumMapper<>(
          this.clazz,
          this.maps,
          this.defaults.withDefault(defaultValue)
      );
    }

    @Override
    @Nonnull
    public StringEnumMapper<E> withDefault(@Nonnull E defaultValueForAll) {
      return new NameStringEnumMapper<>(
          this.clazz,
          this.maps,
          this.defaults.withDefault(defaultValueForAll)
      );
    }

    @Override
    @Nonnull
    public StringEnumMapper<E> withNullDefault(@Nonnull E nullDefaultValue) {
      return new NameStringEnumMapper<>(
          this.clazz,
          this.maps,
          this.defaults.withNullDefault(nullDefaultValue)
      );
    }

    @Override
    @Nonnull
    public StringEnumMapper<E> withEmptyDefault(@Nonnull E emptyDefaultValue) {
      return new NameStringEnumMapper<>(
          this.clazz,
          this.maps,
          this.defaults.withEmptyDefault(emptyDefaultValue)
      );
    }

    @Override
    @Nonnull
    public StringEnumMapper<E> withUnknownDefault(@Nonnull E unknownDefaultValue) {
      return new NameStringEnumMapper<>(
          this.clazz,
          this.maps,
          this.defaults.withUnknownDefault(unknownDefaultValue)
      );
    }
  }

  /**
   * Implementation of a StringEnumMapper that matches String to and from enum values using the enum's toString method,
   * uses a case insensitive comparison to compare an input String to toString() result and is backed by Maps.
   *
   * @param <E> any enum type
   */
  private static class ToStringInsensitiveEnumMapper<E extends Enum<E>>
      extends CaseInsensitiveMapStringEnumMapper<E>
      implements StringEnumMapper<E> {

    protected ToStringInsensitiveEnumMapper(@Nonnull Class<E> clazz,
                                            @Nonnull StringEnumMapperMaps<E> maps,
                                            @Nonnull StringEnumMapperDefaults<E> defaults) {
      super(clazz, maps, defaults);
    }

    @Override
    @Nonnull
    protected Function<E, String> getEnumToStringTransformer() {
      return new EnumToStringFunction<>();
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
    public StringEnumMapper<E> except(@Nonnull E enumValue, @Nullable String str) {
      return new ToStringInsensitiveEnumMapper<>(
          this.clazz,
          this.maps.except(enumValue, str),
          this.defaults
      );
    }

    @Override
    @Nonnull
    public StringEnumMapper<E> except(@Nonnull String str, @Nullable E e) {
      return new ToStringInsensitiveEnumMapper<>(
          this.clazz,
          this.maps.except(str, e),
          this.defaults
      );
    }

    @Override
    @Nonnull
    public StringEnumMapper<E> except(@Nonnull BiMapping<E, String> exceptionMapping) {
      return new ToStringInsensitiveEnumMapper<>(
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
    public StringEnumMapper<E> withEnumDefaults(@Nonnull MappingDefaults<E> defaults) {
      return new ToStringInsensitiveEnumMapper<>(
          this.clazz,
          this.maps,
          this.defaults.withEnumDefaults(defaults)
      );
    }

    @Nonnull
    @Override
    public StringEnumMapper<E> withDefault(@Nonnull String defaultValue) {
      return new ToStringInsensitiveEnumMapper<>(
          this.clazz,
          this.maps,
          this.defaults.withDefault(defaultValue)
      );
    }

    @Nonnull
    @Override
    public StringEnumMapper<E> withDefault(@Nonnull E defaultValueForAll) {
      return new ToStringInsensitiveEnumMapper<>(
          this.clazz,
          this.maps,
          this.defaults.withDefault(defaultValueForAll)
      );
    }

    @Nonnull
    @Override
    public StringEnumMapper<E> withNullDefault(@Nonnull E nullDefaultValue) {
      return new ToStringInsensitiveEnumMapper<>(
          this.clazz,
          this.maps,
          this.defaults.withNullDefault(nullDefaultValue)
      );
    }

    @Nonnull
    @Override
    public StringEnumMapper<E> withEmptyDefault(@Nonnull E emptyDefaultValue) {
      return new ToStringInsensitiveEnumMapper<>(
          this.clazz,
          this.maps,
          this.defaults.withEmptyDefault(emptyDefaultValue)
      );
    }

    @Nonnull
    @Override
    public StringEnumMapper<E> withUnknownDefault(@Nonnull E unknownDefaultValue) {
      return new ToStringInsensitiveEnumMapper<>(
          this.clazz,
          this.maps,
          this.defaults.withUnknownDefault(unknownDefaultValue)
      );
    }
  }

  /**
   * Implementation of a StringEnumMapper that matches String to and from enum values using the enum's toString method,
   * uses a case sensitive comparison to compare an input String to toString() result and is backed by Maps.
   *
   * @param <E> any enum type
   */
  private static class ToStringStringEnumMapper<E extends Enum<E>>
      extends CaseSensitiveMapStringEnumMapper<E>
      implements StringEnumMapper<E> {

    public ToStringStringEnumMapper(@Nonnull Class<E> clazz, @Nonnull StringEnumMapperDefaults<E> defaults) {
      super(clazz, defaults);
    }

    private ToStringStringEnumMapper(@Nonnull Class<E> clazz,
                                     @Nonnull StringEnumMapperMaps<E> maps,
                                     @Nonnull StringEnumMapperDefaults<E> defaults) {
      super(clazz, maps, defaults);
    }

    @Override
    @Nonnull
    protected Function<E, String> getEnumToStringTransformer() {
      return new EnumToStringFunction<>();
    }

    /*=============*
     * case switch *
     *=============*/
    @Override
    @Nonnull
    public StringEnumMapper<E> ignoreCase() {
      return new ToStringInsensitiveEnumMapper<>(clazz, maps, defaults);
    }

    /*====================*
     * Mapping exceptions *
     *====================*/

    @Override
    @Nonnull
    public StringEnumMapper<E> except(@Nonnull E enumValue, @Nullable String str) {
      return new ToStringStringEnumMapper<>(
          this.clazz,
          this.maps.except(enumValue, str),
          this.defaults
      );
    }

    @Override
    @Nonnull
    public StringEnumMapper<E> except(@Nonnull String str, @Nullable E e) {
      return new ToStringStringEnumMapper<>(
          this.clazz,
          this.maps.except(str, e),
          this.defaults
      );
    }

    @Override
    @Nonnull
    public StringEnumMapper<E> except(@Nonnull BiMapping<E, String> exceptionMapping) {
      return new ToStringStringEnumMapper<>(
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
    public StringEnumMapper<E> withEnumDefaults(@Nonnull MappingDefaults<E> defaults) {
      return new ToStringStringEnumMapper<>(
          this.clazz,
          this.maps,
          this.defaults.withEnumDefaults(defaults)
      );
    }

    @Override
    @Nonnull
    public StringEnumMapper<E> withDefault(@Nonnull String defaultValue) {
      return new ToStringStringEnumMapper<>(
          this.clazz,
          this.maps,
          this.defaults.withDefault(defaultValue)
      );
    }

    @Override
    @Nonnull
    public StringEnumMapper<E> withDefault(@Nonnull E defaultValueForAll) {
      return new ToStringStringEnumMapper<>(
          this.clazz,
          this.maps,
          this.defaults.withDefault(defaultValueForAll)
      );
    }

    @Override
    @Nonnull
    public StringEnumMapper<E> withNullDefault(@Nonnull E nullDefaultValue) {
      return new ToStringStringEnumMapper<>(
          this.clazz,
          this.maps,
          this.defaults.withNullDefault(nullDefaultValue)
      );
    }

    @Override
    @Nonnull
    public StringEnumMapper<E> withEmptyDefault(@Nonnull E emptyDefaultValue) {
      return new ToStringStringEnumMapper<>(
          this.clazz,
          this.maps,
          this.defaults.withEmptyDefault(emptyDefaultValue)
      );
    }

    @Override
    @Nonnull
    public StringEnumMapper<E> withUnknownDefault(@Nonnull E unknownDefaultValue) {
      return new ToStringStringEnumMapper<>(
          this.clazz,
          this.maps,
          this.defaults.withUnknownDefault(unknownDefaultValue)
      );
    }
  }

  /**
   * Function that transforms an enum value into the String returned by its name method.
   *
   * @param <E> any enum type
   */
  private static class EnumNameFunction<E extends Enum<E>> implements Function<E, String> {
    @Override
    @Nullable
    public String apply(@Nullable E input) {
      if (input == null) {
        return null;
      }
      return input.name();
    }
  }

  /**
   * Function that transforms an enum value into the String returned by its toString method.
   *
   * @param <E> any enum type
   */
  private static class EnumToStringFunction<E extends Enum<E>> implements Function<E, String> {
    @Override
    @Nullable
    public String apply(@Nullable E input) {
      if (input == null) {
        return null;
      }
      return input.toString();
    }
  }
}
