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

import fr.javatronic.damapping.toolkit.enums.impl.ByNameEnumMapperImpl;
import fr.javatronic.damapping.toolkit.enums.impl.ByToStringEnumMapperImpl;
import fr.javatronic.damapping.toolkit.enums.impl.CustomStringEnumMapperImpl;

import javax.annotation.Nonnull;

/**
 * StringEnumMappers - This class provides the developer with a convenient way to create mappers to map an enum value to
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
 * TODO use internal Map for case sensitive matching instead of loop to improve performance?
 * TODO finish support for ignoreCase() and default method on CustomEnumMapper and ByToStringEnumMapper
 * TODO add shorthand for case-sensitive matching on name with optional such as Guava's <T extends Enum<T>> Optional<T> getIfPresent(Class<T>, String)
 * TODO Java8 support: make EnumToString a Functional interface, add methods returning optional instead of null? quid of defaults?
 *
 * @author Sébastien Lesaint
 */
public final class StringEnumMappers {
  private StringEnumMappers() {
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
      public fr.javatronic.damapping.toolkit.enums.ByNameEnumMapper<T> byName() {
        return new ByNameEnumMapperImpl<T>(enumAClass);
      }

      @Nonnull
      @Override
      public fr.javatronic.damapping.toolkit.enums.ByToStringEnumMapper<T> byToString() {
        return new ByToStringEnumMapperImpl<T>(enumAClass);
      }
    };
  }

  public static interface EnumMapperFactory<T extends Enum<T>> {
    /**
     * Builds a mapper that maps an enum value to and from a String that matches Strings to the {@link Enum#name()}.
     *
     * @return a {@link fr.javatronic.damapping.toolkit.enums.ByNameEnumMapper} instance
     */
    @Nonnull
    fr.javatronic.damapping.toolkit.enums.ByNameEnumMapper<T> byName();

    /**
     * Builds a mapper that maps an enum value to and from a String that matches Strings to the {@link Enum#toString()}.
     *
     * @return a {@link fr.javatronic.damapping.toolkit.enums.ByToStringEnumMapper} instance
     */
    @Nonnull
    fr.javatronic.damapping.toolkit.enums.ByToStringEnumMapper<T> byToString();

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

}
