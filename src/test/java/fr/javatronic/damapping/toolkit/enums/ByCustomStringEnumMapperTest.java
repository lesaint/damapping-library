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

import org.testng.annotations.Test;

import static fr.javatronic.damapping.toolkit.enums.StringEnumMapper.map;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * ByCustomStringEnumMapperTest - Unit tests for class {@link fr.javatronic.damapping.toolkit.enums.StringEnumMapper}.
 *
 * @author Sébastien Lesaint
 */
public class ByCustomStringEnumMapperTest {

  private static final String DUMMY_STRING_VALUE = "fooBarAcme";

  /**
   * *****
   * custom
   * ******
   */
  @Test
  public void toString_returns_null_for_null_value() throws Exception {
    assertThat(map(EnumA.class).by(CustomEnumAToString.INSTANCE).toString(null)).isNull();
  }

  @Test
  public void toString_returns_transform_value() throws Exception {
    StringEnumMapper.CustomStringEnumMapper<EnumA> mapper = map(EnumA.class).by(CustomEnumAToString.INSTANCE);

    for (EnumA enumA : EnumA.values()) {
      assertThat(mapper.toString(enumA)).isEqualTo(CustomEnumAToString.INSTANCE.transform(enumA));
    }
  }

  @Test
  public void toEnum_returns_null_for_null_value() throws Exception {
    assertThat(map(EnumA.class).by(CustomEnumAToString.INSTANCE).toEnum(null)).isNull();
  }

  @Test
  public void toEnum_returns_enum_value_for_exact_string_value() throws Exception {
    StringEnumMapper.CustomStringEnumMapper<EnumA> mapper = map(EnumA.class).by(CustomEnumAToString.INSTANCE);

    for (EnumA enumA : EnumA.values()) {
      assertThat(mapper.toEnum(CustomEnumAToString.INSTANCE.transform(enumA))).isEqualTo(enumA);
    }
  }

  @Test
  public void toEnum_returns_null_for_null_string() throws Exception {
    assertThat(map(EnumA.class).by(CustomEnumAToString.INSTANCE).toEnum(null)).isNull();
  }

  @Test
  public void toEnum_returns_null_for_empty_String() throws Exception {
    assertThat(map(EnumA.class).by(CustomEnumAToString.INSTANCE).toEnum("")).isNull();
  }

  @Test
  public void toEnum_returns_null_for_case_does_not_match() throws Exception {
    StringEnumMapper.CustomStringEnumMapper<EnumA> mapper = map(EnumA.class).by(CustomEnumAToString.INSTANCE);

    for (EnumA enumA : EnumA.values()) {
      assertThat(mapper.toEnum(CustomEnumAToString.INSTANCE.transform(enumA).toUpperCase())).isNull();
    }
    assertThat(mapper.toEnum(DUMMY_STRING_VALUE)).isNull();
  }

  /**
   * *********
   * utilities
   * *********
   */

  private static enum CustomEnumAToString implements StringEnumMapper.EnumToString<EnumA> {
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
}
