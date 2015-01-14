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
package fr.javatronic.damapping.toolkit;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Mappings - Factory class for {@link UniMapping} and {@link BiMapping}.
 *
 * @author Sébastien Lesaint
 */
public final class Mappings {
  private Mappings() {
    // prevents instantiation
  }

  @Nonnull
  public static <E, T> UniMapping<E, T> mapping(@Nonnull final E e, @Nullable final T t) {
    return new UniMapping<E, T>() {
      @Nonnull
      @Override
      public E from() {
        return e;
      }

      @Nullable
      @Override
      public T to() {
        return t;
      }
    };
  }

  @Nonnull
  public static <E, T> BiMapping<E, T> bimapping(@Nonnull final E e, @Nonnull final T t) {
    return new BiMapping<E, T>() {
      @Nonnull
      @Override
      public E from() {
        return e;
      }

      @Nonnull
      @Override
      public T to() {
        return t;
      }
    };
  }
}
