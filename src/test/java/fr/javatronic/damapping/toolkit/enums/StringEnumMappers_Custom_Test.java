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

import org.testng.annotations.Test;

import static fr.javatronic.damapping.toolkit.enums.StringEnumMappers.map;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * ByStringEnumMapperTest - Unit tests for class mapping an enum to and from a String by the some custom
 * &transformation.
 *
 * @author Sébastien Lesaint
 */
public class StringEnumMappers_Custom_Test {

  private static final String DUMMY_STRING_VALUE = "fooBarAcme";

  /*========*
   * custom *
   *========*/
  @Test
  public void toString_returns_null_for_null_value() throws Exception {
    assertThat(map(EnumA.class).by(WeirdEnumATransformer.INSTANCE).toString(null)).isNull();
  }

  @Test
  public void toString_returns_transform_value() throws Exception {
    StringEnumMapper<EnumA> mapper = map(EnumA.class).by(WeirdEnumATransformer.INSTANCE);

    for (EnumA enumA : EnumA.values()) {
      assertThat(mapper.toString(enumA)).isEqualTo(WeirdEnumATransformer.INSTANCE.apply(enumA));
    }
  }

  @Test
  public void toEnum_returns_null_for_null_value() throws Exception {
    assertThat(map(EnumA.class).by(WeirdEnumATransformer.INSTANCE).toEnum(null)).isNull();
  }

  @Test
  public void toEnum_returns_enum_value_for_exact_string_value() throws Exception {
    StringEnumMapper<EnumA> mapper = map(EnumA.class).by(WeirdEnumATransformer.INSTANCE);

    for (EnumA enumA : EnumA.values()) {
      assertThat(mapper.toEnum(WeirdEnumATransformer.INSTANCE.apply(enumA))).isEqualTo(enumA);
    }
  }

  @Test
  public void toEnum_returns_null_for_null_string() throws Exception {
    assertThat(map(EnumA.class).by(WeirdEnumATransformer.INSTANCE).toEnum(null)).isNull();
  }

  @Test
  public void toEnum_returns_null_for_empty_String() throws Exception {
    assertThat(map(EnumA.class).by(WeirdEnumATransformer.INSTANCE).toEnum("")).isNull();
  }

  @Test
  public void toEnum_returns_null_for_case_does_not_match() throws Exception {
    StringEnumMapper<EnumA> mapper = map(EnumA.class).by(WeirdEnumATransformer.INSTANCE);

    for (EnumA enumA : EnumA.values()) {
      assertThat(mapper.toEnum(WeirdEnumATransformer.INSTANCE.apply(enumA).toUpperCase())).isNull();
    }
    assertThat(mapper.toEnum(DUMMY_STRING_VALUE)).isNull();
  }

  /*=====================*
   * byCustom ignoreCase *
   *=====================*/
  @Test
  public void map_byCustom_ignoreCase_toString_returns_null_for_null_value() throws Exception {
    assertThat(map(EnumA.class).by(WeirdEnumATransformer.INSTANCE).ignoreCase().toString(null)).isNull();
  }

  @Test
  public void map_byCustom_ignoreCase_toString_returns_name_value() throws Exception {
    for (EnumA enumA : EnumA.values()) {
      assertThat(
          map(EnumA.class).by(WeirdEnumATransformer.INSTANCE).ignoreCase().toString(enumA)
      ).isEqualTo(WeirdEnumATransformer.INSTANCE.apply(enumA));
    }
  }

  @Test
  public void map_byCustom_ignoreCase_toEnum_returns_null_for_null_value() throws Exception {
    assertThat(map(EnumA.class).by(WeirdEnumATransformer.INSTANCE).ignoreCase().toEnum(null)).isNull();
  }

  @Test
  public void map_byCustom_ignoreCase_toEnum_returns_enum_when_case_does_not_match() throws Exception {
    StringEnumMapper<EnumA> mapper = map(EnumA.class).by(WeirdEnumATransformer.INSTANCE).ignoreCase();

    for (EnumA enumA : EnumA.values()) {
      assertThat(mapper.toEnum(WeirdEnumATransformer.INSTANCE.apply(enumA).toLowerCase())).isEqualTo(enumA);
    }
  }

  @Test
  public void map_byCustom_ignoreCase_returns_itself_as_ignoreCase_returned_value() throws Exception {
    StringEnumMapper<EnumA> mapper = map(EnumA.class).by(WeirdEnumATransformer.INSTANCE).ignoreCase();

    assertThat(mapper.ignoreCase()).isSameAs(mapper);
  }

}
