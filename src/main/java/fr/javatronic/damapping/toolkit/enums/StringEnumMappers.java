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

import javax.annotation.Nonnull;

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
      return new CaseSensitiveMapStringEnumMapper<>(
          enumAClass,
          new StringEnumMapperMaps<>(enumAClass, new EnumNameFunction<T>()),
          StringEnumMapperDefaults.<T>defaultsToNull()
      );
    }

    @Nonnull
    @Override
    public StringEnumMapper<T> byToString() {
      return new CaseSensitiveMapStringEnumMapper<>(
          enumAClass,
          new StringEnumMapperMaps<>(enumAClass, new EnumToStringFunction<T>()),
          StringEnumMapperDefaults.<T>defaultsToNull()
      );
    }

    @Override
    @Nonnull
    public StringEnumMapper<T> by(@Nonnull BijectiveTransformer<T> transformer) {
      return new CaseSensitiveMapStringEnumMapper<>(
          enumAClass,
          new StringEnumMapperMaps<>(enumAClass, transformer),
          StringEnumMapperDefaults.<T>defaultsToNull()
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
