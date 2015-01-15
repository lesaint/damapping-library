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

import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;
import com.google.common.base.Function;

import static com.google.common.collect.FluentIterable.from;
import static java.util.Objects.requireNonNull;

/**
 * EnumA -
 *
 * @author Sébastien Lesaint
 */
enum EnumA {
  VALUE_1, VALUE_2, VALUE_3;

  /**
   * Overrides method toString() so that its behavior is different from name(): it returns the name in lowercase.
   *
   * @return a {@link String}
   */
  @Override
  public String toString() {
    return name().toLowerCase();
  }

  public static Iterator<Object[]> createCombination(List<StringEnumMapper<EnumA>> baseEnumMappers,
                                                     Function<StringEnumMapper<EnumA>, List<StringEnumMapper<EnumA>>>
                                                         fct) {
    return from(baseEnumMappers)
        .transformAndConcat(fct)
        .transform(WrapIntoObjectArray.INSTANCE)
        .toList()
        .iterator();
  }

  private static enum WrapIntoObjectArray implements Function<Object, Object[]> {
    INSTANCE;

    @Nullable
    @Override
    public Object[] apply(@Nullable Object input) {
      requireNonNull(input);
      return new Object[]{input};
    }
  }
}
