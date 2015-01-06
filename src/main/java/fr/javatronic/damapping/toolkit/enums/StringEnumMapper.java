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

import javax.annotation.Nullable;

/**
 * Interface implemented by all mapper mapping an enum to and from a String.
 *
 * @param <E> an enum type
 */
public interface StringEnumMapper<E extends Enum<E>> {
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
