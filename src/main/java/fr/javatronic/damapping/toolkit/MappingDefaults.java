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
package fr.javatronic.damapping.toolkit;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

/**
 * MappingDefaults - Describe default value(s) to use when mapping can not be performed by a mapper because either:
 * <ul>
 * <li>the source value is {@code null}</li>
 * <li>the source value is empty</li>
 * <li>the source value is unknown/unsupported</li>
 * </ul>
 * <p>
 * It is left to each Mapper implementation to use all or only some of the default values defined in this class.
 * </p>
 * <p>
 * To create a MappingDefaults instance, start by creating an instance which uses the same default value for all
 * cases (either using {@link #defaultToNull()} or {@link #defaultTo(Object)} and change exception cases using either
 * {@link #withNullDefault(Object)}, {@link #withEmptyDefault(Object)} or {@link #withUnknownDefault(Object)}.
 * </p>
 * <p>
 * Example:
 * <pre>
 * MappingDefaults.defaultsTo("Unknown Input")
 *     .withNullDefault("Null input")
 *     .withEmptyDefault("Empty input");
 * </pre>
 * </p>
 *
 * @author Sébastien Lesaint
 */
@Immutable
public final class MappingDefaults<T> {
  @Nullable
  private final T nullDefault;
  @Nullable
  private final T emptyDefault;
  @Nullable
  private final T unknownDefault;

  /**
   * The singleton MappingDefaults which returns {@code null} in every case.
   */
  private static final MappingDefaults<?> NULL_DEFAULTS = new MappingDefaults<>(null, null, null);

  /**
   * Private constructor.
   */
  private MappingDefaults(@Nullable T nullDefault, @Nullable T emptyDefault, @Nullable T unknownDefault) {
    this.nullDefault = nullDefault;
    this.emptyDefault = emptyDefault;
    this.unknownDefault = unknownDefault;
  }

  /**
   * Default value to use when source value is {@code null}.
   */
  @Nullable
  public T whenNull() {
    return nullDefault;
  }

  /**
   * Default value to use when source value is empty (applies only to mapping from a String, array, Collection, etc...).
   */
  @Nullable
  public T whenEmpty() {
    return emptyDefault;
  }

  /**
   * Default value to use when source value is unknown to the mapper.
   */
  @Nullable
  public T whenUnknown() {
    return unknownDefault;
  }

  /**
   * Creates a new MappingDefaults with the same values as the current one except for
   * a {@code null} source value which will have the specified default value.
   *
   * @param nullDefault a T or {@code null}
   *
   * @return a {@link MappingDefaults}
   */
  @Nonnull
  public MappingDefaults<T> withNullDefault(@Nullable T nullDefault) {
    return new MappingDefaults<>(nullDefault, this.emptyDefault, unknownDefault);
  }

  /**
   * Creates a new MappingDefaults with the same values as the current one except for
   * an empty source value which will have the specified default value.
   *
   * @param emptyDefault a T or {@code null}
   *
   * @return a {@link MappingDefaults}
   */
  @Nonnull
  public MappingDefaults<T> withEmptyDefault(@Nullable T emptyDefault) {
    return new MappingDefaults<>(this.nullDefault, emptyDefault, this.unknownDefault);
  }

  /**
   * Creates a new MappingDefaults with the same values as the current one except for
   * an unknown source value which will have the specified default value.
   *
   * @param unknownDefault a T or {@code null}
   *
   * @return a {@link MappingDefaults}
   */
  @Nonnull
  public MappingDefaults<T> withUnknownDefault(@Nullable T unknownDefault) {
    return new MappingDefaults<>(this.nullDefault, this.emptyDefault, unknownDefault);
  }


  /*===================*
   * factory functions *
   *===================*/

  /**
   * Creates a MappingDefaults that uses {@code null} for every case.
   *
   * @param <T> any type
   *
   * @return a {@link MappingDefaults}
   */
  @SuppressWarnings("unchecked")
  public static <T> MappingDefaults<T> defaultToNull() {
    return (MappingDefaults<T>) NULL_DEFAULTS;
  }

  /**
   * Creates a MappingDefaults that uses the specified value for every case.
   * <p>
   * Invoking this method with a {@code null} argument is totally equivalent to invoking {@link #defaultToNull()}.
   * </p>
   *
   * @param defaultValueForAll a object of type {@code T} or {@code null}
   * @param <T>                any type
   *
   * @return a {@link MappingDefaults}
   */
  public static <T> MappingDefaults<T> defaultTo(@Nullable T defaultValueForAll) {
    if (defaultValueForAll == null) {
      return defaultToNull();
    }
    return new MappingDefaults<T>(defaultValueForAll, defaultValueForAll, defaultValueForAll);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    MappingDefaults that = (MappingDefaults) o;

    if (nullDefault == null ? that.nullDefault != null : !nullDefault.equals(that.nullDefault)) {
      return false;
    }
    if (emptyDefault == null ? that.emptyDefault != null : !emptyDefault.equals(that.emptyDefault)) {
      return false;
    }
    return unknownDefault == null ? that.unknownDefault == null : unknownDefault.equals(that.unknownDefault);
  }

  @Override
  public int hashCode() {
    int result = nullDefault == null ? 0 : nullDefault.hashCode();
    result = 31 * result + (emptyDefault == null ? 0 : emptyDefault.hashCode());
    result = 31 * result + (unknownDefault == null ? 0 : unknownDefault.hashCode());
    return result;
  }

  @Override
  public String toString() {
    return "MappingDefaults{" +
        "null=" + nullDefault +
        ", empty=" + emptyDefault +
        ", unknown=" + unknownDefault +
        '}';
  }
}
