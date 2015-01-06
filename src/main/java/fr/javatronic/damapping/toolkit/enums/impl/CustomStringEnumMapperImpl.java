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
package fr.javatronic.damapping.toolkit.enums.impl;

import fr.javatronic.damapping.toolkit.enums.CustomStringEnumMapper;
import fr.javatronic.damapping.toolkit.enums.EnumToString;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static java.util.Objects.requireNonNull;

/**
* CustomStringEnumMapperImpl -
*
* @author Sébastien Lesaint
*/
public class CustomStringEnumMapperImpl<E extends Enum<E>> implements CustomStringEnumMapper<E> {
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
  public E toEnum(@Nullable String str) {
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
