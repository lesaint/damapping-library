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
 * UniMapping - Represents a unidirectional mapping.
 *
 * @author Sébastien Lesaint
 */
public interface UniMapping<E, T> {
  @Nonnull
  E from();

  @Nullable
  T to();
}
