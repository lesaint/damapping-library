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
 * ByNameStringEnumMapperTest - Unit tests for class mapping an enum to and from a String by the enum name method.
 *
 * @author Sébastien Lesaint
 */
public class ByNameStringEnumMapperTest {

  private static final String DUMMY_STRING_VALUE = "fooBarAcme";
  private static final String DEFAULT_STRING = "DEFAULT_STRING";

  /*========*
   * byName
   *========*/
  @Test
  public void map_byName_toString_returns_null_for_null_value() throws Exception {
    assertThat(map(EnumA.class).byName().toString(null)).isNull();
  }

  @Test
  public void map_byName_toString_returns_name_value() throws Exception {
    mapper_toString_returns_name_value(map(EnumA.class).byName());
  }

  @Test
  public void map_byName_toEnum_returns_null_for_null_value() throws Exception {
    assertThat(map(EnumA.class).byName().toEnum(null)).isNull();
  }

  @Test
  public void map_byName_toEnum_returns_null_for_empty_String() throws Exception {
    assertThat(map(EnumA.class).byName().toEnum("")).isNull();
  }

  @Test
  public void map_byName_toEnum_returns_value_for_exact_name() throws Exception {
    StringEnumMapper<EnumA> mapper = map(EnumA.class).byName();

    for (EnumA enumA : EnumA.values()) {
      assertThat(mapper.toEnum(enumA.name())).isEqualTo(enumA);
    }
  }

  @Test
  public void map_byName_toEnum_returns_null_for_case_does_not_match() throws Exception {
    StringEnumMapper<EnumA> mapper = map(EnumA.class).byName();

    for (EnumA enumA : EnumA.values()) {
      assertThat(mapper.toEnum(enumA.name().toLowerCase())).isNull();
    }
    assertThat(mapper.toEnum(DUMMY_STRING_VALUE)).isNull();
  }

  /*===================*
   * byName ignoreCase *
   *===================*/
  @Test
  public void map_byName_ignoreCase_toString_returns_null_for_null_value() throws Exception {
    assertThat(map(EnumA.class).byName().ignoreCase().toString(null)).isNull();
  }

  @Test
  public void map_byName_ignoreCase_toString_returns_name_value() throws Exception {
    mapper_toString_returns_name_value(map(EnumA.class).byName().ignoreCase());
  }

  @Test
  public void map_byName_ignoreCase_toEnum_returns_null_for_null_value() throws Exception {
    assertThat(map(EnumA.class).byName().ignoreCase().toEnum(null)).isNull();
  }

  @Test
  public void map_byName_ignoreCase_toEnum_returns_name_when_case_does_not_match() throws Exception {
    StringEnumMapper<EnumA> mapper = map(EnumA.class).byName().ignoreCase();

    for (EnumA enumA : EnumA.values()) {
      assertThat(mapper.toEnum(enumA.name().toLowerCase())).isEqualTo(enumA);
    }
  }

  @Test
  public void map_byName_ignoreCase_returns_itself_as_ignoreCase_returned_value() throws Exception {
    StringEnumMapper<EnumA> mapper = map(EnumA.class).byName().ignoreCase();

    assertThat(mapper.ignoreCase()).isSameAs(mapper);
  }

  /*=====================*
   * byName with default *
   *=====================*/
  @Test
  public void map_byName_withDefault_String_toString_returns_default_string_for_null_value() throws Exception {
    assertThat(map(EnumA.class).byName().withDefault(DEFAULT_STRING).toString(null)).isEqualTo(DEFAULT_STRING);
  }

  @Test
  public void map_byName_withDefault_String_toString_returns_default_string_returns_name_value() throws Exception {
    mapper_toString_returns_name_value(map(EnumA.class).byName().withDefault(DEFAULT_STRING));
  }

  /*==========*
  * utilities *
   *==========*/
  private void mapper_toString_returns_name_value(StringEnumMapper<EnumA> mapper) {
    for (EnumA enumA : EnumA.values()) {
      assertThat(mapper.toString(enumA)).isEqualTo(enumA.name());
    }
  }

}
