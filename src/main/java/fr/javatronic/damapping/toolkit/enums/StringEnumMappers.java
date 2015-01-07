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
 * Most common enum mappings are based on the name, sometimes on the value of toString(). These mappings have prime
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
 * <p/>
 * TODO use internal Map for case sensitive matching instead of loop to improve performance?
 * TODO add shorthand for case-sensitive matching on name with optional such as Guava's <T extends Enum<T>>
 * Optional<T> getIfPresent(Class<T>, String)
 * TODO Java8 support: make EnumToString a Functional interface, add methods returning optional instead of null? quid
 * of defaults?
 *
 * @author Sébastien Lesaint
 */
public final class StringEnumMappers {
  private StringEnumMappers() {
    // prevents instantiation
  }

  public static <T extends Enum<T>> EnumMapperFactory<T> map(@Nonnull final Class<T> enumAClass) {
    return new EnumMapperFactoryImpl<T>(enumAClass);
  }

  public static interface EnumMapperFactory<T extends Enum<T>> {

    /**
     * Builds a mapper that maps an enum value to and from a String that matches Strings to the {@link Enum#name()}.
     *
     * @return a {@link fr.javatronic.damapping.toolkit.enums.StringEnumMapper} instance
     */
    @Nonnull
    StringEnumMapper<T> byName();

    /**
     * Builds a mapper that maps an enum value to and from a String that matches Strings to the {@link Enum#toString()}.
     *
     * @return a {@link fr.javatronic.damapping.toolkit.enums.StringEnumMapper} instance
     */
    @Nonnull
    StringEnumMapper<T> byToString();

    /**
     * Builds a mapper that maps an enum value to and from a String that matches Strings to the result of the
     * specified {@link EnumToString} instance.
     *
     * @param transformer a {@link EnumToString} instance
     *
     * @return a {@link fr.javatronic.damapping.toolkit.enums.StringEnumMapper} instance
     *
     * @throws NullPointerException if the specified {@link EnumToString} instance is {@code null}
     */
    @Nonnull
    StringEnumMapper<T> by(@Nonnull EnumToString<T> transformer);

  }

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
      return new StringEnumMapperImpl<T>(
          enumAClass,
          new EnumToString<T>() {
            @Nonnull
            @Override
            public String transform(@Nonnull T enumValue) {
              return enumValue.name();
            }
          }
      );
    }

    @Nonnull
    @Override
    public StringEnumMapper<T> byToString() {
      return new StringEnumMapperImpl<T>(
          enumAClass,
          new EnumToString<T>() {
            @Nonnull
            @Override
            public String transform(@Nonnull T enumValue) {
              return enumValue.toString();
            }
          }
      );
    }
  }

  /**
   * StringEnumMapperImpl -
   *
   * @author Sébastien Lesaint
   */
  private static class StringEnumMapperImpl<E extends Enum<E>> implements StringEnumMapper<E> {
    private static final String NUll_DEFAULT_VALUE_ERROR_MSG = "When specifying a default value, " +
        "the value can not be null";
    private static final String NUll_MAPPING_DEFAULTS_ERROR_MSG = "provided " + MappingDefaults.class.getSimpleName()
        + " object can not be null";

    @Nonnull
    private final Class<E> clazz;
    @Nonnull
    private final EnumToString<E> transformer;
    @Nonnull
    private final MappingDefaults<String> stringDefaults;
    @Nonnull
    private final MappingDefaults<E> enumDefaults;
    private final boolean ignoreCase;

    private StringEnumMapperImpl(@Nonnull Class<E> clazz,
                                 @Nonnull EnumToString<E> transformer) {
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

    @Nonnull
    @Override
    public StringEnumMapper<E> withStringDefaults(@Nonnull MappingDefaults<String> defaults) {
      requireNonNull(defaults, NUll_MAPPING_DEFAULTS_ERROR_MSG);
      return new StringEnumMapperImpl<E>(clazz, transformer, defaults, this.enumDefaults, ignoreCase);
    }

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
}
