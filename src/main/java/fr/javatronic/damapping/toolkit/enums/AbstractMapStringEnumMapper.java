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
import javax.annotation.Nullable;

import static java.util.Objects.requireNonNull;

/**
 * Abstract implementation of StringEnumMapper that implements holds mapping from an enum to a String and from a
 * String to an enum as a two immutable maps.
 * <p>
 * This class also provide an implementation for the method {@link fr.javatronic.damapping.toolkit.enums.StringEnumMapper#toString(Enum)} as it is the
 * same for both case sensitive and case insensitive mappers but leaves the implementation of method
 * {@link fr.javatronic.damapping.toolkit.enums.StringEnumMapper#toEnum(String)} to subclasses.
 * </p>
 *
 * @param <E> any enum type
 */
abstract class AbstractMapStringEnumMapper<E extends Enum<E>>
    extends AbstractStringEnumMapper<E>
    implements StringEnumMapper<E> {

  @Nonnull
  protected final StringEnumMapperMaps<E> maps;

  protected AbstractMapStringEnumMapper(@Nonnull Class<E> clazz,
                                        @Nonnull StringEnumMapperMaps<E> maps,
                                        @Nonnull StringEnumMapperDefaults<E> defaults) {
    super(clazz, defaults);
    this.maps = requireNonNull(maps);
  }

  protected AbstractMapStringEnumMapper(@Nonnull Class<E> clazz,
                                        @Nonnull BijectiveTransformer<E> transformer,
                                        @Nonnull StringEnumMapperDefaults<E> defaults) {
    super(clazz, defaults);
    this.maps = new StringEnumMapperMaps<>(clazz, transformer);
  }

  /*=================*
   * Mapping methods *
   *=================*/

  @Override
  @Nullable
  public String toString(@Nullable E enumValue) {
    if (enumValue == null) {
      return defaults.getStringDefaults().whenNull();
    }

    String res = maps.getEnumToString().get(enumValue);
    if (res == null) {
      return defaults.getStringDefaults().whenUnknown();
    }
    return res;
  }
}
