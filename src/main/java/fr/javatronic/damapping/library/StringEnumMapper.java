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
 * StringEnumMapper -
 *
 * TODO add methods from(CharSequence) (covers StringBuffer, StringBuilder,...)? from(Comparable<String>)?
 *
 * @author Sébastien Lesaint
 */
public class StringEnumMapper {
  private StringEnumMapper() {
    // prevents instantiation
  }

  public static interface EnumToString<T extends Enum<T>> {
    @Nonnull
    String transform(@Nonnull T enumValue);
  }

  public static <T extends Enum<T>> EnumMapperFactory<T> map(@Nonnull final Class<T> enumAClass) {
    return new EnumMapperFactory<T>() {
      @Override
      public CustomStringEnumMapper<T> by(@Nonnull EnumToString<T> transformer) {
        return new CustomStringEnumMapperImpl<T>(enumAClass, transformer);
      }
    };
  }

  public static interface EnumMapperFactory<T extends Enum<T>> {
    @Nonnull
    CustomStringEnumMapper<T> by(@Nonnull EnumToString<T> transformer);
  }

  public static interface CustomStringEnumMapper<E extends Enum<E>> {
    @Nullable
    String toString(@Nullable E enumValue);

    @Nullable
    E from(@Nullable String str);
  }

  public static class CustomStringEnumMapperImpl<E extends Enum<E>> implements CustomStringEnumMapper<E> {
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
    public E from(@Nullable String str) {
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

  @Nonnull
  public static <T extends Enum<T>> ByToStringEnumMapper<T> mapByToString(@Nonnull Class<T> enumAClass) {
    return new ByToStringEnumMapperImpl<T>(requireNonNull(enumAClass));
  }

  public static interface ByToStringEnumMapper<T extends Enum<T>> {
    @Nullable
    String toString(@Nullable T enumValue);

    @Nullable
    T from(@Nullable String str);
  }

  private static class ByToStringEnumMapperImpl<E extends Enum<E>> implements ByToStringEnumMapper<E> {
    private final Class<E> clazz;

    public ByToStringEnumMapperImpl(Class<E> clazz) {
      this.clazz = clazz;
    }

    @Nullable
    @Override
    public String toString(@Nullable E enumValue) {
      if (enumValue == null) {
        return null;
      }

      return enumValue.toString();
    }

    @Nullable
    @Override
    public E from(@Nullable String str) {
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

  }

  @Nonnull
  public static <T extends Enum<T>> ByNameEnumMapper<T> mapByName(@Nonnull Class<T> enumAClass) {
    return new ByNameEnumMapperImpl<T>(requireNonNull(enumAClass));
  }


  public static interface ByNameEnumMapper<T extends Enum<T>> {
    @Nullable
    String toString(@Nullable T enumValue);

    @Nullable
    T from(@Nullable String str);

    ByNameEnumMapper<T> ignoreCase();
  }

  private static abstract class BaseByNameEnumMapperImpl<E extends Enum<E>> implements StringEnumMapper.ByNameEnumMapper<E> {
    protected final Class<E> clazz;

    public BaseByNameEnumMapperImpl(Class<E> clazz) {
      this.clazz = clazz;
    }

    @Nullable
    @Override
    public String toString(@Nullable E enumValue) {
      if (enumValue == null) {
        return null;
      }

      return enumValue.name();
    }
  }

  private static class ByNameEnumMapperImpl<E extends Enum<E>> extends BaseByNameEnumMapperImpl<E> {

    public ByNameEnumMapperImpl(Class<E> clazz) {
      super(clazz);
    }

    @Nullable
    @Override
    public E from(@Nullable String str) {
      if (str == null || str.isEmpty()) {
        return null;
      }

      for (E e : clazz.getEnumConstants()) {
        if (e.name().equals(str)) {
          return e;
        }
      }
      return null;
    }

    @Override
    public ByNameEnumMapper<E> ignoreCase() {
      return new IgnoreCaseByNameEnumMapperImpl<E>(clazz);
    }
  }

  private static class IgnoreCaseByNameEnumMapperImpl<E extends Enum<E>> extends BaseByNameEnumMapperImpl<E> {

    public IgnoreCaseByNameEnumMapperImpl(Class<E> clazz) {
      super(clazz);
    }

    @Nullable
    @Override
    public E from(@Nullable String str) {
      if (str == null || str.isEmpty()) {
        return null;
      }

      for (E e : clazz.getEnumConstants()) {
        if (e.name().compareToIgnoreCase(str) == 0) {
          return e;
        }
      }
      return null;
    }

    @Override
    public ByNameEnumMapper<E> ignoreCase() {
      return this;
    }
  }
}
