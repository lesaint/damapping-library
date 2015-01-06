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

import fr.javatronic.damapping.toolkit.enums.ByToStringEnumMapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
* IgnoreCaseByToStringEnumMapperImpl -
*
* @author Sébastien Lesaint
*/
public class IgnoreCaseByToStringEnumMapperImpl<E extends Enum<E>> extends BaseByToStringEnumMapperImpl<E> {

  public IgnoreCaseByToStringEnumMapperImpl(Class<E> clazz) {
    super(clazz);
  }

  @Override
  @Nullable
  public E toEnum(@Nullable String str) {
    if (str == null || str.isEmpty()) {
      return null;
    }

    for (E e : clazz.getEnumConstants()) {
      if (e.toString().compareToIgnoreCase(str) == 0) {
        return e;
      }
    }
    return null;
  }

  @Override
  @Nonnull
  public ByToStringEnumMapper<E> ignoreCase() {
    return this;
  }
}
