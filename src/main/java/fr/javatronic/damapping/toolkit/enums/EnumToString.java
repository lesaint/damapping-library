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
 * Defines a rule to be used by a {@link StringEnumMapper} to transform an enum value into a String.
 * <p>
 * This rule can not have any exception, ie. any enum value must be transformed into a non null String.
 * </p>
 * <p>
 * If the transformation returns the same String for more than one enum value, the behavior of the
 * {@link StringEnumMapper} that uses it is undefined.
 * </p>
 *
 * @param <T> an enum type
 */
public interface EnumToString<T extends Enum<T>> {
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
