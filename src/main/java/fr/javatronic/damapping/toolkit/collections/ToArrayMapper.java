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
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.google.common.collect.Lists;

import static fr.javatronic.damapping.toolkit.MappingDefaults.defaultTo;
import static java.util.Objects.requireNonNull;

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
  private static final String[] EMPTY_STRING_ARRAY = new String[0];

  /**
   * Transforms a collection into an array, returns an empty array when the specified collection is {@code null} or
   * empty.
   * <p>
   * If the collection makes any guarantees as to what order its elements are returned by its iterator, the elements in
   * the returned array will respect the same order.
   * </p>
   *
   * @param collection a {@link Collection} of type {@code T} or {@code null}
   * @param <T>        a type
   *
   * @return a array of type {@code T}
   */
  @Nonnull
  public static <T> T[] toArray(@Nullable Collection<T> collection) {
    return toArray(collection, ToArrayMapper.<T>emptyArray());
  }

  /**
   * Transforms a Iterable into an array, returns an empty array when the iterable is {@code null}, has a code null}
   * Iterator or is empty.
   * <p>
   * If the Iterable makes any guarantees as to what order its elements are returned by its iterator, the elements in
   * the returned array will respect the same order.
   * </p>
   *
   * @param iterable a {@link Iterable} of type {@code T} or {@code null}
   * @param <T>      a type
   *
   * @return a array of type {@code T}
   */
  @Nonnull
  public static <T> T[] toArray(@Nullable Iterable<T> iterable) {
    return toArray(iterable, ToArrayMapper.<T>defaultToEmpty());
  }

  /**
   * Transforms a String into a array of String containing the specified String as single value.
   * <p>
   * This method returns an empty array if the String is {@code null} or empty.
   * </p>
   *
   * @param str a {@link String} or {@code null}
   *
   * @return a array of {@link String}
   */
  @Nonnull
  public static String[] toArray(@Nullable String str) {
    return toArray(str, MappingDefaults.defaultTo(EMPTY_STRING_ARRAY));
  }


  /**
   * Transforms an object of any type into an array of that type with a single value, the specified object.
   * <p>
   * This method returns an empty array when the object is {@code null].
   * </p>
   *
   * @param obj an object of type {@code T} or {@code null}
   * @param <T> any type
   *
   * @return a array of type {@code T} or {@code null}
   */
  @Nonnull
  public static <T> T[] toArray(@Nullable T obj) {
    if (obj == null) {
      return emptyArray();
    }

    return toArrayImpl(obj);
  }

  /**
   * Private method isolating SuppressWarnings annotation to the smallest piece of code possible.
   */
  @SuppressWarnings("unchecked")
  private static <T> T[] toArrayImpl(T object) {
    return (T[]) new Object[]{object};
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
   * @param collection   a {@link Collection} of type {@code T} or {@code null}
   * @param defaultValue a array of {@code T} or {@code null}
   * @param <T>          a type
   *
   * @return a array of type {@code T} or {@code null}
   *
   * @throws NullPointerException if the {@code defaultValue} is {@code null}
   */
  @Nullable
  public static <T> T[] toArray(@Nullable Collection<T> collection, @Nullable T[] defaultValue) {
    return toArray(collection, defaultTo(defaultValue));
  }

  /**
   * Transforms a Iterable into an array, returns the specified defaultValue when the iterable is {@code null}, has a
   * {@code null} Iterator or is empty.
   * <p>
   * If the Iterable makes any guarantees as to what order its elements are returned by its iterator, the elements in
   * the returned array will respect the same order.
   * </p>
   *
   * @param iterable     a {@link Iterable} of type {@code T} or {@code null}
   * @param defaultValue a array of {@code T} or {@code null}
   * @param <T>          a type
   *
   * @return a array of type {@code T} or {@code null}
   */
  @Nullable
  public static <T> T[] toArray(@Nullable Iterable<T> iterable, @Nullable T[] defaultValue) {
    return toArray(iterable, defaultTo(defaultValue));
  }

  /**
   * Transforms a String into a array of String containing the specified String as single value.
   * <p>
   * This method returns the specified defaultValue if the String is {@code null} or empty.
   * </p>
   *
   * @param str          a {@link String} or {@code null}
   * @param defaultValue a array if {@link String} or {@code null}
   *
   * @return a array of {@link String} or {@code null}
   */
  @Nullable
  public static String[] toArray(@Nullable String str, @Nullable String[] defaultValue) {
    return toArray(str, defaultTo(defaultValue));
  }

  /**
   * Transforms an object of any type into an array of that type with a single value, the specified object.
   * <p>
   * This method returns the specified default value when the object is {@code null].
   * </p>
   *
   * @param obj          an object of type {@code T} or {@code null}
   * @param defaultValue an array of type {@code T} or {@code null}
   * @param <T>          any type
   *
   * @return a array of type {@code T} or {@code null}
   */
  @Nullable
  public static <T> T[] toArray(@Nullable T obj, @Nullable T[] defaultValue) {
    if (obj == null) {
      return defaultValue;
    }

    return toArrayImpl(obj);
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
   * @param collection   a {@link Collection} of type {@code T} or {@code null}
   * @param defaultValue a {@link MappingDefaults} of type {@code T[]}
   * @param <T>          a type
   *
   * @return a array of type {@code T}
   *
   * @throws NullPointerException if the {@code defaultValue} is {@code null}
   * @see #defaultToEmpty()
   * @see #defaultToNull()
   */
  @Nullable
  public static <T> T[] toArray(@Nullable Collection<T> collection,
                                @Nonnull MappingDefaults<T[]> defaultValue) {
    requireNonNull(defaultValue);
    if (collection == null) {
      return defaultValue.whenNull();
    }

    if (collection.isEmpty()) {
      return defaultValue.whenEmpty();
    }

    return toArrayImpl(collection);
  }

  /**
   * Transforms a Iterable into an array, returns the default returned by the method
   * {@link MappingDefaults#whenNull()} of the specified defaultValue when the iterable is {@code null} or returns a
   * {@code null} Iterator and the one of {@link MappingDefaults#whenEmpty()} when the Iterable is empty.
   * <p>
   * If the Iterable makes any guarantees as to what order its elements are returned by its iterator, the elements in
   * the returned array will respect the same order.
   * </p>
   *
   * @param iterable     a {@link Iterable} of type {@code T} or {@code null}
   * @param defaultValue a array of {@code T} or {@code null}
   * @param <T>          a type
   *
   * @return a array of type {@code T} or {@code null}
   *
   * @throws NullPointerException if the {@code defaultValue} is {@code null}
   */
  @Nullable
  public static <T> T[] toArray(@Nullable Iterable<T> iterable,
                                @Nonnull MappingDefaults<T[]> defaultValue) {
    requireNonNull(defaultValue);
    if (iterable == null) {
      return defaultValue.whenNull();
    }
    Iterator<T> iterator = iterable.iterator();
    if (iterator == null) {
      return defaultValue.whenNull();
    }
    if (!iterator.hasNext()) {
      return defaultValue.whenEmpty();
    }

    List<T> list = Lists.newArrayList();
    while (iterator.hasNext()) {
      list.add(iterator.next());
    }

    return toArrayImpl(list);
  }

  /**
   * Private method isolating SuppressWarnings annotation to the smallest piece of code possible.
   */
  @SuppressWarnings("unchecked")
  private static <T> T[] toArrayImpl(Collection<T> collection) {
    return collection.toArray((T[]) new Object[collection.size()]);
  }


  /**
   * Transforms a String into a array of String containing the specified String as single value.
   * <p>
   * This method returns the result of method {@link MappingDefaults#whenNull()} when the specified String is {@code
   * null} and the result of  {@link MappingDefaults#whenEmpty()} when the String is empty.
   * </p>
   *
   * @param str          a {@link String} or {@code null}
   * @param defaultValue a {@link MappingDefaults}
   *
   * @return a array of {@link String} or {@code null}
   *
   * @throws java.lang.NullPointerException if defaultValue is {@code null}
   */
  @Nullable
  public static String[] toArray(@Nullable String str, @Nonnull MappingDefaults<String[]> defaultValue) {
    requireNonNull(defaultValue);
    if (str == null) {
      return defaultValue.whenNull();
    }
    if (str.isEmpty()) {
      return defaultValue.whenEmpty();
    }

    return new String[]{str};
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
  @Nonnull
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
  @Nonnull
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
