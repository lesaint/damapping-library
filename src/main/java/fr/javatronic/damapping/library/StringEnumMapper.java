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
package fr.javatronic.damapping.library;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static java.util.Objects.requireNonNull;

/**
 * StringEnumMapper - This class provides the developer with a convenient way to create mappers to map an enum value to
 * or from a String and specify its behavior in a readable way.
 * <p>
 * Mappers without explicit behavior expressed:
 * <ul>
 *   <li>never throw an exception but instead return a null value</li>
 *   <li>use strict String comparison, ie. are based on {@link String#compareTo(String)}</li>
 * </ul>
 * </p>
 * <p>
 * The following behavior can be customized:
 * <ul>
 *   <li>case insensitive String comparison can be used</li>
 *   <li>String value returned when mapping from a {@code null} enum value, a specific value can be returned instead of {@code null}</li>
 *   <li>enum value returned from a {@code null}, empty or unknown String value can be customized as a whole or selectively</li>
 * </ul>
 * </p>
 * <p>
 * Behavior customization is achieved using a meaningful method chaining, for example:
 * <pre>
 * StringEnumMapper.ByNameEnumMapper mapper = StringEnumMapper.map(Bar.class)
 *     .byName()
 *     .ignoreCase()
 *     .withDefault("no_bar_value")
 *     .withNullDefault(Bar.ACME)
 *     .withEmptyDefault(Bar.VISEN)
 * </pre>
 * </p>
 * <p>
 * Most common enum mappings are based on the name, sometimes on the value of toString(). These mappings have prime support:
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
 *   public String transform(@Nonnull Bar enumValue) {
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
 *
 * @author Sébastien Lesaint
 */
public class StringEnumMapper {
  private StringEnumMapper() {
    // prevents instantiation
  }

  public static <T extends Enum<T>> EnumMapperFactory<T> map(@Nonnull final Class<T> enumAClass) {
    return new EnumMapperFactory<T>() {
      @Override
      @Nonnull
      public CustomStringEnumMapper<T> by(@Nonnull EnumToString<T> transformer) {
        return new CustomStringEnumMapperImpl<T>(enumAClass, transformer);
      }

      @Override
      @Nonnull
      public ByNameEnumMapper<T> byName() {
        return new ByNameEnumMapperImpl<T>(enumAClass);
      }

      @Nonnull
      @Override
      public ByToStringEnumMapper<T> byToString() {
        return new ByToStringEnumMapperImpl<T>(enumAClass);
      }
    };
  }

  /**
   * Defines a rule use by a {@link CustomStringEnumMapper} to transform an enum value into a String.
   * <p>
   * This rule can not have any exception, ie. any enum value must be transformed into a non null String.
   * </p>
   * <p>
   * If the transformation returns the same String for more than one enum value, the behavior of the
   * {@link CustomStringEnumMapper} that uses it is undefined.
   * </p>
   *
   * @param <T> an enum type
   */
  public static interface EnumToString<T extends Enum<T>> {
    /**
     * Transform the specified enum value into a String which must not be {@code null}.
     * <p>
     * The specified enum value is guaranteed to be non null.
     * </p>
     *
     * @param enumValue a enum value of type T
     *
     * @return a {@link String}
     */
    @Nonnull
    String transform(@Nonnull T enumValue);
  }

  public static interface EnumMapperFactory<T extends Enum<T>> {
    /**
     * Builds a mapper that maps an enum value to and from a String that matches Strings to the {@link Enum#name()}.
     *
     * @return a {@link ByNameEnumMapper} instance
     */
    @Nonnull
    ByNameEnumMapper<T> byName();

    /**
     * Builds a mapper that maps an enum value to and from a String that matches Strings to the {@link Enum#toString()}.
     *
     * @return a {@link ByToStringEnumMapper} instance
     */
    @Nonnull
    ByToStringEnumMapper<T> byToString();

    /**
     * Builds a mapper that maps an enum value to and from a String that matches Strings to the result of the
     * specified {@link CustomStringEnumMapper} instance.
     *
     * @param transformer a {@link EnumToString} instance
     *
     * @return a {@link CustomStringEnumMapper} instance
     *
     * @throws NullPointerException if the specified {@link EnumToString} instance is {@code null}
     */
    @Nonnull
    CustomStringEnumMapper<T> by(@Nonnull EnumToString<T> transformer);
  }

  /**
   * Interface implemented by all mapper mapping an enum to and from a String.
   *
   * @param <E> an enum type
   */
  public static interface IStringEnumMapper<E extends Enum<E>> {
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
  }

  /**
   * A mapper that uses custom method represented by a {@linkn EnumToString} implementation to match an enum to a
   * String (and the other way around).
   *
   * @param <E> an enum type
   */
  public static interface CustomStringEnumMapper<E extends Enum<E>> extends IStringEnumMapper<E> {
  }

  /**
   * A mapper that uses the {@link Enum#toString()} method to match an enum to a String (and the other way around).
   *
   * @param <T> an enum type
   */
  public static interface ByToStringEnumMapper<T extends Enum<T>> extends IStringEnumMapper<T> {
    @Nonnull
    ByToStringEnumMapper<T> ignoreCase();
  }

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
  public static interface ByNameEnumMapper<T extends Enum<T>> extends IStringEnumMapper<T> {

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

  private static class CustomStringEnumMapperImpl<E extends Enum<E>> implements CustomStringEnumMapper<E> {
    @Nonnull
    private final Class<E> clazz;
    @Nonnull
    private final EnumToString<E> transformer;

    public CustomStringEnumMapperImpl(@Nonnull Class<E> clazz, @Nonnull EnumToString<E> transformer) {
      this.clazz = requireNonNull(clazz);
      this.transformer = requireNonNull(transformer);
    }

    @Nullable
    @Override
    public String toString(@Nullable E enumValue) {
      if (enumValue == null) {
        return null;
      }

      return transformer.transform(enumValue);
    }

    @Nullable
    @Override
    public E toEnum(@Nullable String str) {
      if (str == null || str.isEmpty()) {
        return null;
      }

      for (E e : clazz.getEnumConstants()) {
        if (transformer.transform(e).equals(str)) {
          return e;
        }
      }
      return null;
    }
  }

  private static abstract class BaseByNameEnumMapperImpl<E extends Enum<E>>
      implements StringEnumMapper.ByNameEnumMapper<E> {
    protected static final String NUll_DEFAULT_VALUE_ERROR_MSG = "When specifying a default value, " +
        "the value can not be null";

    @Nonnull
    protected final Class<E> clazz;
    @Nullable
    protected final String defaultValue;
    @Nullable
    protected final E nullDefaultValue;
    @Nullable
    protected final E emptyDefaultValue;
    @Nullable
    protected final E unknownDefaultValue;

    public BaseByNameEnumMapperImpl(@Nonnull Class<E> clazz,
                                    @Nullable String defaultValue,
                                    @Nullable E nullDefaultValue,
                                    @Nullable E emptyDefaultValue,
                                    @Nullable E unknownDefaultValue) {
      this.clazz = requireNonNull(clazz);
      this.defaultValue = defaultValue;
      this.nullDefaultValue = nullDefaultValue;
      this.emptyDefaultValue = emptyDefaultValue;
      this.unknownDefaultValue = unknownDefaultValue;
    }

    @Nullable
    @Override
    public String toString(@Nullable E enumValue) {
      if (enumValue == null) {
        return defaultValue;
      }

      return enumValue.name();
    }
  }

  private static class ByNameEnumMapperImpl<E extends Enum<E>> extends BaseByNameEnumMapperImpl<E> {


    public ByNameEnumMapperImpl(@Nonnull Class<E> clazz) {
      super(clazz, null, null, null, null);
    }

    @Nullable
    @Override
    public E toEnum(@Nullable String str) {
      if (str == null) {
        return nullDefaultValue;
      }

      if (str.isEmpty()) {
        return emptyDefaultValue;
      }

      for (E e : clazz.getEnumConstants()) {
        if (e.name().equals(str)) {
          return e;
        }
      }
      return unknownDefaultValue;
    }

    @Override
    @Nonnull
    public ByNameEnumMapper<E> ignoreCase() {
      return new IgnoreCaseByNameEnumMapperImpl<E>(clazz, defaultValue, nullDefaultValue, emptyDefaultValue,
          unknownDefaultValue
      );
    }

    @Nonnull
    @Override
    public ByNameEnumMapper<E> withDefault(@Nonnull String defaultValue) {
      requireNonNull(defaultValue, NUll_DEFAULT_VALUE_ERROR_MSG);
      return new IgnoreCaseByNameEnumMapperImpl<E>(clazz, defaultValue, nullDefaultValue, emptyDefaultValue,
          unknownDefaultValue
      );
    }

    @Nonnull
    @Override
    public ByNameEnumMapper<E> withDefault(@Nonnull E defaultValueForAll) {
      requireNonNull(defaultValueForAll, NUll_DEFAULT_VALUE_ERROR_MSG);
      return new IgnoreCaseByNameEnumMapperImpl<E>(clazz, defaultValue, defaultValueForAll, defaultValueForAll,
          defaultValueForAll
      );
    }

    @Nonnull
    @Override
    public ByNameEnumMapper<E> withNullDefault(@Nonnull E nullDefaultValue) {
      requireNonNull(nullDefaultValue, NUll_DEFAULT_VALUE_ERROR_MSG);
      return new IgnoreCaseByNameEnumMapperImpl<E>(clazz, defaultValue, nullDefaultValue, emptyDefaultValue,
          unknownDefaultValue
      );
    }

    @Nonnull
    @Override
    public ByNameEnumMapper<E> withEmptyDefault(@Nonnull E emptyDefaultValue) {
      requireNonNull(emptyDefaultValue, NUll_DEFAULT_VALUE_ERROR_MSG);
      return new IgnoreCaseByNameEnumMapperImpl<E>(clazz, defaultValue, nullDefaultValue, emptyDefaultValue,
          unknownDefaultValue
      );
    }

    @Nonnull
    @Override
    public ByNameEnumMapper<E> withUnknownDefault(@Nonnull E unknownDefaultValue) {
      requireNonNull(unknownDefaultValue, NUll_DEFAULT_VALUE_ERROR_MSG);
      return new IgnoreCaseByNameEnumMapperImpl<E>(clazz, defaultValue, nullDefaultValue, emptyDefaultValue,
          unknownDefaultValue
      );
    }
  }

  private static class IgnoreCaseByNameEnumMapperImpl<E extends Enum<E>> extends BaseByNameEnumMapperImpl<E> {

    public IgnoreCaseByNameEnumMapperImpl(@Nonnull Class<E> clazz,
                                          @Nullable String defaultValue,
                                          @Nullable E nullDefaultValue,
                                          @Nullable E emptyDefaultValue,
                                          @Nullable E unknownDefaultValue) {
      super(clazz, defaultValue, nullDefaultValue, emptyDefaultValue, unknownDefaultValue);
    }

    @Nullable
    @Override
    public E toEnum(@Nullable String str) {
      if (str == null) {
        return nullDefaultValue;
      }

      if (str.isEmpty()) {
        return emptyDefaultValue;
      }

      for (E e : clazz.getEnumConstants()) {
        if (e.name().compareToIgnoreCase(str) == 0) {
          return e;
        }
      }
      return unknownDefaultValue;
    }

    @Override
    @Nonnull
    public ByNameEnumMapper<E> ignoreCase() {
      return this;
    }

    @Override
    @Nonnull
    public ByNameEnumMapper<E> withDefault(@Nonnull String defaultValue) {
      requireNonNull(defaultValue, NUll_DEFAULT_VALUE_ERROR_MSG);
      return new IgnoreCaseByNameEnumMapperImpl<E>(clazz, defaultValue, nullDefaultValue, emptyDefaultValue,
          unknownDefaultValue
      );
    }

    @Override
    @Nonnull
    public ByNameEnumMapper<E> withDefault(@Nonnull E defaultValueForAll) {
      requireNonNull(defaultValueForAll, NUll_DEFAULT_VALUE_ERROR_MSG);
      return new IgnoreCaseByNameEnumMapperImpl<E>(clazz, defaultValue, defaultValueForAll, defaultValueForAll,
          defaultValueForAll
      );
    }

    @Override
    @Nonnull
    public ByNameEnumMapper<E> withNullDefault(@Nonnull E nullDefaultValue) {
      requireNonNull(nullDefaultValue, NUll_DEFAULT_VALUE_ERROR_MSG);
      return new IgnoreCaseByNameEnumMapperImpl<E>(clazz, defaultValue, nullDefaultValue, emptyDefaultValue,
          unknownDefaultValue
      );
    }

    @Override
    @Nonnull
    public ByNameEnumMapper<E> withEmptyDefault(@Nonnull E emptyDefaultValue) {
      requireNonNull(emptyDefaultValue, NUll_DEFAULT_VALUE_ERROR_MSG);
      return new IgnoreCaseByNameEnumMapperImpl<E>(clazz, defaultValue, nullDefaultValue, emptyDefaultValue,
          unknownDefaultValue
      );
    }

    @Override
    @Nonnull
    public ByNameEnumMapper<E> withUnknownDefault(@Nonnull E unknownDefaultValue) {
      requireNonNull(unknownDefaultValue, NUll_DEFAULT_VALUE_ERROR_MSG);
      return new IgnoreCaseByNameEnumMapperImpl<E>(clazz, defaultValue, nullDefaultValue, emptyDefaultValue,
          unknownDefaultValue
      );
    }
  }

  private static abstract class BaseByToStringEnumMapperImpl<E extends Enum<E>> implements ByToStringEnumMapper<E> {
    protected final Class<E> clazz;

    protected BaseByToStringEnumMapperImpl(Class<E> clazz) {
      this.clazz = clazz;
    }

    @Override
    @Nullable
    public String toString(@Nullable E enumValue) {
      if (enumValue == null) {
        return null;
      }

      return enumValue.toString();
    }

  }

  private static class ByToStringEnumMapperImpl<E extends Enum<E>> extends BaseByToStringEnumMapperImpl<E> {

    public ByToStringEnumMapperImpl(Class<E> clazz) {
      super(clazz);
    }

    @Override
    @Nullable
    public E toEnum(@Nullable String str) {
      if (str == null || str.isEmpty()) {
        return null;
      }

      for (E e : clazz.getEnumConstants()) {
        if (e.toString().equals(str)) {
          return e;
        }
      }
      return null;
    }

    @Override
    @Nonnull
    public ByToStringEnumMapper<E> ignoreCase() {
      return new IgnoreCaseByToStringEnumMapperImpl<E>(clazz);
    }

  }

  private static class IgnoreCaseByToStringEnumMapperImpl<E extends Enum<E>> extends BaseByToStringEnumMapperImpl<E> {

    public IgnoreCaseByToStringEnumMapperImpl(Class<E> clazz) {
      super(clazz);
    }

    @Override
    @Nullable
    public E toEnum(@Nullable String str) {
      if (str == null || str.isEmpty()) {
        return null;
      }

      for (E e : clazz.getEnumConstants()) {
        if (e.toString().compareToIgnoreCase(str) == 0) {
          return e;
        }
      }
      return null;
    }

    @Override
    @Nonnull
    public ByToStringEnumMapper<E> ignoreCase() {
      return this;
    }
  }
}
