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
package fr.javatronic.damapping.toolkit.collections;

import fr.javatronic.damapping.toolkit.MappingDefaults;

import java.util.Collection;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static fr.javatronic.damapping.toolkit.MappingDefaults.defaultTo;

/**
 * ToArrayMapper - This class provides the developer with a convenient way to map Collection, Iterable, etc.
 * to an array.
 * <p/>
 * TODO: java8 add ToArrayOptional
 *
 * @author Sébastien Lesaint
 */
public abstract class ToArrayMapper {
  private static final MappingDefaults<?> EMPTY_ARRAY_DEFAULTS = defaultTo(new Object[0]);

  /**
   * Transforms a collection into an array, returns an empty array when the specified collection is {@code null} or
   * empty.
   * <p>
   * If the collection makes any guarantees as to what order its elements are returned by its iterator, the elements in
   * the returned array will respect the same order.
   * </p>
   *
   * @param collection a collection of type {@code T} or {@code null}
   * @param <T>        a type
   *
   * @return a array of type {@code T}
   */
  @Nonnull
  public static <T> T[] toArray(@Nullable Collection<T> collection) {
    if (collection == null || collection.isEmpty()) {
      return emptyArray();
    }

    return toArrayImpl(collection);
  }

  /**
   * Private method isolating SuppressWarnings annotation to the smallest piece of code possible.
   */
  @SuppressWarnings("unchecked")
  private static <T> T[] emptyArray() {
    return (T[]) EMPTY_ARRAY_DEFAULTS.whenNull();
  }

  /**
   * Transforms a collection into an array, returns the specified defaultValue when the collection is empty or
   * {@code null}.
   * <p>
   * If the collection makes any guarantees as to what order its elements are returned by its iterator, the elements in
   * the returned array will respect the same order.
   * </p>
   *
   * @param collection   a collection of type {@code T} or {@code null}
   * @param defaultValue a array of {@code T} or {@code null}
   * @param <T>          a type
   *
   * @return a array of type {@code T}
   *
   * @throws NullPointerException if the {@code defaultValue} is {@code null}
   */
  @Nonnull
  public static <T> T[] toArray(@Nullable Collection<T> collection, @Nullable T[] defaultValue) {
    return toArray(collection, defaultTo(defaultValue));
  }

  /**
   * Transforms a collection into an array, returns the default returned by the specified defaultValue methods
   * {@link MappingDefaults#whenNull()} and {@link MappingDefaults#whenEmpty()} when the collection
   * is {@code null} or empty.
   * <p>
   * If the collection makes any guarantees as to what order its elements are returned by its iterator, the elements in
   * the returned array will respect the same order.
   * </p>
   * <p>
   * For convenience, methods {@link #defaultToEmpty()} and {@link #defaultToNull()} can be used as second
   * argument of this method to, respectively, returns an empty array when collection is {@code null} or empty
   * (in which case calling this method is totally equivalent to calling method {@link #toArray(java.util.Collection)}
   * or return a {@code null} value when collection is {@code null} or empty.
   * </p>
   *
   * @param collection   a collection of type {@code T} or {@code null}
   * @param defaultValue a {@link MappingDefaults} of type {@code T[]}
   * @param <T>          a type
   *
   * @return a array of type {@code T}
   *
   * @throws NullPointerException if the {@code defaultValue} is {@code null}
   * @see #defaultToEmpty()
   * @see #defaultToNull()
   */
  public static <T> T[] toArray(@Nullable Collection<T> collection,
                                @Nonnull MappingDefaults<T[]> defaultValue) {
    Objects.requireNonNull(defaultValue);
    if (collection == null) {
      return defaultValue.whenNull();
    }

    if (collection.isEmpty()) {
      return defaultValue.whenEmpty();
    }

    return toArrayImpl(collection);
  }

  /**
   * Private method isolating SuppressWarnings annotation to the smallest piece of code possible.
   */
  @SuppressWarnings("unchecked")
  private static <T> T[] toArrayImpl(Collection<T> collection) {
    return collection.toArray((T[]) new Object[collection.size()]);
  }

  /**
   * Creates a MappingDefaults that returns a {@code null} array of type {@code T} in every case.
   * <p>
   * This method can also be used to create a MappingDefaults that returns an {@code null} array in only one case. eg.:
   * <pre>
   * MappingDefaults<String[]> defaults = ToArrayMapper.<String>defaultToNull().withEmptyDefault(new String[]{"ERROR"});
   * </pre>
   * </p>
   *
   * @param <T> any type
   *
   * @return a {@link MappingDefaults}
   */
  public static <T> MappingDefaults<T[]> defaultToNull() {
    return nullArrayDefaultsImpl();
  }

  /**
   * Private method hiding SuppressWarnings annotation from public API of the class.
   */
  @SuppressWarnings("unchecked")
  private static <T> MappingDefaults<T> nullArrayDefaultsImpl() {
    return (MappingDefaults<T>) MappingDefaults.defaultToNull();
  }

  /**
   * Creates a MappingDefaults that returns an empty array of type {@code T} in every case.
   * <p>
   * This method can also be used to create a MappingDefaults that returns an empty array in only one case. eg.:
   * <pre>
   * MappingDefaults<String[]> defaults = ToArrayMapper.<String>defaultToEmpty().withNullDefault(null);
   * </pre>
   * </p>
   *
   * @param <T> any type
   *
   * @return a {@link MappingDefaults}
   */
  public static <T> MappingDefaults<T[]> defaultToEmpty() {
    return emptyArrayDefaultsImpl();
  }

  /**
   * Private method hiding SuppressWarnings annotation from public API of the class.
   */
  @SuppressWarnings("unchecked")
  private static <T> MappingDefaults<T[]> emptyArrayDefaultsImpl() {
    return (MappingDefaults<T[]>) EMPTY_ARRAY_DEFAULTS;
  }
}
