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
* WeirdEnumATransformer -
*
* @author Sébastien Lesaint
*/
enum WeirdEnumATransformer implements EnumToString<EnumA> {
  INSTANCE;

  @Nonnull
  @Override
  public String transform(@Nonnull EnumA enumValue) {
    switch (enumValue) {
      case VALUE_1:
        return "Foo";
      case VALUE_2:
        return "Bar";
      case VALUE_3:
        return "Acme";
      default:
        throw new IllegalArgumentException("value is not supported in toString(), fix toString(): " + enumValue);
    }
  }
}
