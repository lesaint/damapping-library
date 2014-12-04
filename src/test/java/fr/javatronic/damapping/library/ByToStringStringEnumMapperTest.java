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
package fr.javatronic.damapping.library;

import org.testng.annotations.Test;

import static fr.javatronic.damapping.library.StringEnumMapper.map;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * ByCustomStringEnumMapperTest - Unit tests for class {@link fr.javatronic.damapping.library.StringEnumMapper}.
 *
 * @author Sébastien Lesaint
 */
public class ByToStringStringEnumMapperTest {

  private static final String DUMMY_STRING_VALUE = "fooBarAcme";


  /********
   * byToString
   ********/
  @Test
  public void map_byToString_toString_returns_null_for_null_value() throws Exception {
    assertThat(map(EnumA.class).byToString().toString(null)).isNull();
  }

  @Test
  public void map_byToString_toString_returns_name_value() throws Exception {
    StringEnumMapper.ByToStringEnumMapper<EnumA> mapper = map(EnumA.class).byToString();

    for (EnumA enumA : EnumA.values()) {
      assertThat(mapper.toString(enumA)).isEqualTo(enumA.toString());
    }
  }

  @Test
  public void map_byToString_toEnum_returns_null_for_null_value() throws Exception {
    assertThat(map(EnumA.class).byToString().toEnum(null)).isNull();
  }

  @Test
  public void map_byToString_toEnum_returns_null_for_empty_String() throws Exception {
    assertThat(map(EnumA.class).byToString().toEnum("")).isNull();
  }

  @Test
  public void map_byToString_toEnum_returns_value_for_exact_String_equality() throws Exception {
    StringEnumMapper.ByToStringEnumMapper<EnumA> mapper = map(EnumA.class).byToString();

    for (EnumA enumA : EnumA.values()) {
      assertThat(mapper.toEnum(enumA.toString())).isEqualTo(enumA);
    }
  }

  @Test
  public void map_byToString_toEnum_returns_null_when_case_does_not_match() throws Exception {
    StringEnumMapper.ByToStringEnumMapper<EnumA> mapper = map(EnumA.class).byToString();

    for (EnumA enumA : EnumA.values()) {
      assertThat(mapper.toEnum(enumA.toString().toUpperCase())).isNull();
    }
    assertThat(mapper.toEnum(DUMMY_STRING_VALUE)).isNull();
  }

  /*************************
   * byToString ignoreCase *
   *************************/
  @Test
  public void map_byToString_ignoreCase_toString_returns_null_for_null_value() throws Exception {
    assertThat(map(EnumA.class).byToString().ignoreCase().toString(null)).isNull();
  }

  @Test
  public void map_byToString_ignoreCase_toString_returns_toString_value() throws Exception {
    StringEnumMapper.ByToStringEnumMapper<EnumA> mapper = map(EnumA.class).byToString().ignoreCase();

    for (EnumA enumA : EnumA.values()) {
      assertThat(mapper.toString(enumA)).isEqualTo(enumA.toString());
    }
  }

  @Test
  public void map_byToString_ignoreCase_toEnum_returns_null_for_null_value() throws Exception {
    assertThat(map(EnumA.class).byToString().ignoreCase().toEnum(null)).isNull();
  }

  @Test
  public void map_byToString_ignoreCase_toEnum_returns_value_when_case_matches() throws Exception {
    StringEnumMapper.ByToStringEnumMapper<EnumA> mapper = map(EnumA.class).byToString().ignoreCase();

    for (EnumA enumA : EnumA.values()) {
      assertThat(mapper.toEnum(enumA.name())).isEqualTo(enumA);
    }
  }

  @Test
  public void map_byToString_ignoreCase_toEnum_returns_value_when_case_does_not_match() throws Exception {
    StringEnumMapper.ByToStringEnumMapper<EnumA> mapper = map(EnumA.class).byToString().ignoreCase();

    for (EnumA enumA : EnumA.values()) {
      assertThat(mapper.toEnum(enumA.name().toLowerCase())).isEqualTo(enumA);
    }
  }

  @Test
  public void map_byToString_ignoreCase_returns_itself_as_ignoreCase_returned_value() throws Exception {
    StringEnumMapper.ByToStringEnumMapper<EnumA> mapper = map(EnumA.class).byToString().ignoreCase();

    assertThat(mapper.ignoreCase()).isSameAs(mapper);
  }


}
