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
import com.google.common.base.Function;

/**
 * Defines a bijective transformation from an enum value to a String.
 * <p>
 * To be a bijective transformation, the implementation must comply with <strong>all</strong> the specified constraints:
 * <ol>
 *   <li>the transformer can never return {@code null}</li>
 *   <li>the transformer must return a value for every value of the enum type</li>
 *   <li>the transformer can not return the same String value for more than one enum value</li>
 * </ol>
 * </p>
 * <p>
 * Typical implementations of a BijectiveTransformer are the one based on the enum's name() or toString() methods.
 * </p>
 * <p>
 * Other implementations may rely on a switch statement and throw an {@link java.lang.IllegalArgumentException} for the
 * default label to easily comply with the bijective transformation constraints.
 * </p>
 *
 * @param <T> an enum type
 */
public interface BijectiveTransformer<T extends Enum<T>> extends Function<T, String> {
  /**
   * Transform the specified enum value into a String which must <strong>not be</strong> {@code null}.
   * <p>
   * The specified enum value is guaranteed to be non null.
   * </p>
   *
   * @param enumValue a enum value of type T
   *
   * @return a {@link String}
   */
  @Override
  @Nonnull
  String apply(@Nonnull T enumValue);
}
