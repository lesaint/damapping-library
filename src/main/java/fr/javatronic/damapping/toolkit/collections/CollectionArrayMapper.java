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

import java.util.Collection;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * CollectionArrayMapper - This class provides the developer with a convenient way to map a Collection from and to
 * an array.
 *
 * TODO: add ToArray returning null for null and/or empty collection
 * TODO: java8 add ToArrayOptional
 *
 * @author Sébastien Lesaint
 */
public class CollectionArrayMapper {
  private static final Object[] EMPTY_ARRAY = new Object[0];

  /**
   * Transforms a collection into an array, returns an empty array when the specified collection is {@code null} or
   * empty.
   * <p>
   * If the collection makes any guarantees as to what order its elements are returned by its iterator, the elements in
   * the returned array will respect the same order.
   * </p>
   *
   * @param collection a collection of type {@code T} or {@code null}
   * @param <T> a type
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
   * Transforms a collection into an array, returns an empty array when the specified collection is empty and the
   * specified {@code defaultValue} if the collection is empty.
   * <p>
   * If the collection makes any guarantees as to what order its elements are returned by its iterator, the elements in
   * the returned array will respect the same order.
   * </p>
   *
   * @param collection a collection of type {@code T} or {@code null}
   * @param defaultValue a array of {@code T}
   * @param <T> a type
   *
   * @return a array of type {@code T}
   *
   * @throws NullPointerException if the {@¢ode defaultValue} is {@code null}
   */
  @Nonnull
  public static <T> T[] toArray(@Nullable Collection<T> collection, @Nonnull T[] defaultValue) {
    Objects.requireNonNull(defaultValue);
    if (collection == null || collection.isEmpty()) {
      return defaultValue;
    }

    return toArrayImpl(collection);
  }

  @SuppressWarnings("unchecked")
  private static <T> T[] toArrayImpl(Collection<T> collection) {
    return (T[]) collection.toArray(new Object[collection.size()]);
  }

  @SuppressWarnings("unchecked")
  private static <T> T[] emptyArray() {
    return (T[]) EMPTY_ARRAY;
  }
}
