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

import static java.util.Objects.requireNonNull;

/**
* AbstractStringEnumMapper -
*
* @author Sébastien Lesaint
*/
abstract class AbstractStringEnumMapper<T extends Enum<T>>
    implements StringEnumMapper<T> {
  @Nonnull
  protected final Class<T> clazz;
  @Nonnull
  protected final StringEnumMapperDefaults<T> defaults;

  protected AbstractStringEnumMapper(@Nonnull Class<T> clazz, @Nonnull StringEnumMapperDefaults<T> defaults) {
    this.clazz = requireNonNull(clazz);
    this.defaults = requireNonNull(defaults);
  }
}
