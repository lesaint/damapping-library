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
 * A mapper that uses the {@link Enum#toString()} method to match an enum to a String (and the other way around).
 *
 * @param <T> an enum type
 */
public interface ByToStringEnumMapper<T extends Enum<T>> extends StringEnumMapper<T> {
  @Nonnull
  ByToStringEnumMapper<T> ignoreCase();
}
