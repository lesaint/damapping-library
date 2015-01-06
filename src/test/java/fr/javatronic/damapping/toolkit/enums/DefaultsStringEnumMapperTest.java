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

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static fr.javatronic.damapping.toolkit.enums.StringEnumMappers.map;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * DefaultsStringEnumMapperTest -
 *
 * @author Sébastien Lesaint
 */
public class DefaultsStringEnumMapperTest<T extends Enum<T>> {
  private static final String DEFAULT_STRING_VALUE = "default";
  private static final String UNKNOWN_ENUM_NAME = "acme";

  @Test(dataProvider = "stringEnumMappers_withDefault_string")
  public void withDefault_returns_default_value_when_enum_is_null(StringEnumMapper<T> mapper) throws Exception {
    assertThat(mapper.toString(null)).isEqualTo(DEFAULT_STRING_VALUE);
  }

  /**
   * Creates the combination of calls to withDefault(Enum) for every StringEnumMapper.
   */
  @DataProvider
  public StringEnumMapper[][] stringEnumMappers_withDefault_string() {
    return new StringEnumMapper[][]{
        {map(EnumA.class).byName().withDefault(DEFAULT_STRING_VALUE)},
        {map(EnumA.class).byName().ignoreCase().withDefault(DEFAULT_STRING_VALUE)},
    };
  }

  @Test(dataProvider = "stringEnumMappers_withDefault_enum")
  public void withDefault_returns_default_value_when_enum_is_null_empty_or_unknown(StringEnumMapper<T> mapper)
      throws Exception {
    assertThat(mapper.toEnum(null)).isEqualTo(EnumA.VALUE_1);
    assertThat(mapper.toEnum("")).isEqualTo(EnumA.VALUE_1);
    assertThat(mapper.toEnum(UNKNOWN_ENUM_NAME)).isEqualTo(EnumA.VALUE_1);
  }

  /**
   * Creates the combination of calls to withDefault(Enum) for every StringEnumMapper and the combination of calls to
   * withNullDefault, withEmptyDefault and withUnknownDefault for every StringEnumMapper
   */
  @DataProvider
  public StringEnumMapper[][] stringEnumMappers_withDefault_enum() {
    return new StringEnumMapper[][]{
        {map(EnumA.class).byName().withDefault(EnumA.VALUE_1)},
        {map(EnumA.class).byName().ignoreCase().withDefault(EnumA.VALUE_1)},
        {map(EnumA.class).byName().withNullDefault(EnumA.VALUE_1).withEmptyDefault(EnumA.VALUE_1).withUnknownDefault(
            EnumA.VALUE_1
        )},
        {map(EnumA.class).byName().withNullDefault(EnumA.VALUE_1).withUnknownDefault(EnumA.VALUE_1).withEmptyDefault(
            EnumA.VALUE_1
        )},
        {map(EnumA.class).byName().withEmptyDefault(EnumA.VALUE_1).withNullDefault(EnumA.VALUE_1).withUnknownDefault(
            EnumA.VALUE_1
        )},
        {map(EnumA.class).byName().withEmptyDefault(EnumA.VALUE_1).withUnknownDefault(EnumA.VALUE_1).withNullDefault(
            EnumA.VALUE_1
        )},
        {map(EnumA.class).byName().withUnknownDefault(EnumA.VALUE_1).withEmptyDefault(EnumA.VALUE_1).withNullDefault(
            EnumA.VALUE_1
        )},
        {map(EnumA.class).byName().withUnknownDefault(EnumA.VALUE_1).withNullDefault(EnumA.VALUE_1).withEmptyDefault(
            EnumA.VALUE_1
        )},
        {map(EnumA.class).byName()
            .ignoreCase()
            .withNullDefault(EnumA.VALUE_1)
            .withEmptyDefault(EnumA.VALUE_1)
            .withUnknownDefault(
                EnumA.VALUE_1
            )},
        {map(EnumA.class).byName()
            .ignoreCase()
            .withNullDefault(EnumA.VALUE_1)
            .withUnknownDefault(EnumA.VALUE_1)
            .withEmptyDefault(


                EnumA.VALUE_1
            )},
        {map(EnumA.class).byName()
            .ignoreCase()
            .withUnknownDefault(EnumA.VALUE_1)
            .withNullDefault(EnumA.VALUE_1)
            .withEmptyDefault(
                EnumA.VALUE_1
            )},
        {map(EnumA.class).byName()
            .ignoreCase()
            .withEmptyDefault(EnumA.VALUE_1)
            .withNullDefault(EnumA.VALUE_1)
            .withUnknownDefault(
                EnumA.VALUE_1
            )},
        {map(EnumA.class).byName()
            .ignoreCase()
            .withEmptyDefault(EnumA.VALUE_1)
            .withUnknownDefault(EnumA.VALUE_1)
            .withNullDefault(
                EnumA.VALUE_1
            )},
        {map(EnumA.class).byName()
            .ignoreCase()
            .withUnknownDefault(EnumA.VALUE_1)
            .withEmptyDefault(EnumA.VALUE_1)
            .withNullDefault(
                EnumA.VALUE_1
            )},
        {map(EnumA.class).byName()
            .ignoreCase()
            .withUnknownDefault(EnumA.VALUE_1)
            .withNullDefault(EnumA.VALUE_1)
            .withEmptyDefault(
                EnumA.VALUE_1
            )},
    };
  }

  @Test(dataProvider = "stringEnumMappers_withNullDefault_enum")
  public void withDefault_for_null_only_returns_default_value_when_enum_is_null(StringEnumMapper<T> mapper)
      throws Exception {
    assertThat(mapper.toEnum(null)).isEqualTo(EnumA.VALUE_1);
    assertThat(mapper.toEnum("")).isNull();
    assertThat(mapper.toEnum(UNKNOWN_ENUM_NAME)).isNull();
  }

  /**
   * Creates the combination of calls to withNullDefault for every StringEnumMapper
   */
  @DataProvider
  public StringEnumMapper[][] stringEnumMappers_withNullDefault_enum() {
    return new StringEnumMapper[][]{
        {map(EnumA.class).byName().withNullDefault(EnumA.VALUE_1)},
        {map(EnumA.class).byName().ignoreCase().withNullDefault(EnumA.VALUE_1)},
    };
  }

  @Test(dataProvider = "stringEnumMappers_withEmptyDefault_enum")
  public void withDefault_for_empty_only_returns_default_value_when_enum_is_empty(StringEnumMapper<T> mapper)
      throws Exception {
    assertThat(mapper.toEnum("")).isEqualTo(EnumA.VALUE_1);
    assertThat(mapper.toEnum(null)).isNull();
    assertThat(mapper.toEnum(UNKNOWN_ENUM_NAME)).isNull();
  }

  /**
   * Creates the combination of calls to withEmptyDefault for every StringEnumMapper
   */
  @DataProvider
  public StringEnumMapper[][] stringEnumMappers_withEmptyDefault_enum() {
    return new StringEnumMapper[][]{
        {map(EnumA.class).byName().withEmptyDefault(EnumA.VALUE_1)},
        {map(EnumA.class).byName().ignoreCase().withEmptyDefault(EnumA.VALUE_1)},
    };
  }

  @Test(dataProvider = "stringEnumMappers_withUnknownDefault_enum")
  public void withDefault_for_Unknown_only_returns_default_value_when_enum_is_unknown(StringEnumMapper<T> mapper)
      throws Exception {
    assertThat(mapper.toEnum(UNKNOWN_ENUM_NAME)).isEqualTo(EnumA.VALUE_1);
    assertThat(mapper.toEnum(null)).isNull();
    assertThat(mapper.toEnum("")).isNull();
  }

  /**
   * Creates the combination of calls to withUnknownDefault for every StringEnumMapper
   */
  @DataProvider
  public StringEnumMapper[][] stringEnumMappers_withUnknownDefault_enum() {
    return new StringEnumMapper[][]{
        {map(EnumA.class).byName().withUnknownDefault(EnumA.VALUE_1)},
        {map(EnumA.class).byName().ignoreCase().withUnknownDefault(EnumA.VALUE_1)},
    };
  }

  @Test(dataProvider = "stringEnumMappers_withNullDefault_withEmptyDefault_enum")
  public void withDefault_for_empty_and_null_only_returns_default_value_when_enum_is_null_or_empty(
      StringEnumMapper<T> mapper) throws Exception {
    assertThat(mapper.toEnum(null)).isEqualTo(EnumA.VALUE_1);
    assertThat(mapper.toEnum("")).isEqualTo(EnumA.VALUE_1);
    assertThat(mapper.toEnum(UNKNOWN_ENUM_NAME)).isNull();
  }


  /**
   * Creates the combination of calls to withNullDefault and withEmptyDefault for every
   * StringEnumMapper
   */
  @DataProvider
  public StringEnumMapper[][] stringEnumMappers_withNullDefault_withEmptyDefault_enum() {
    return new StringEnumMapper[][]{
        {map(EnumA.class).byName().withNullDefault(EnumA.VALUE_1).withEmptyDefault(EnumA.VALUE_1)},
        {map(EnumA.class).byName().withEmptyDefault(EnumA.VALUE_1).withNullDefault(EnumA.VALUE_1)},
        {map(EnumA.class).byName().ignoreCase().withNullDefault(EnumA.VALUE_1).withEmptyDefault(EnumA.VALUE_1)},
        {map(EnumA.class).byName().ignoreCase().withEmptyDefault(EnumA.VALUE_1).withNullDefault(EnumA.VALUE_1)},
    };
  }

  @Test(dataProvider = "stringEnumMappers_withNullDefault_withUnknownDefault_enum")
  public void withDefault_for_null_and_unknown_only_returns_default_value_when_enum_is_null_or_unknown(
      StringEnumMapper<T> mapper) throws Exception {
    assertThat(mapper.toEnum(null)).isEqualTo(EnumA.VALUE_1);
    assertThat(mapper.toEnum(UNKNOWN_ENUM_NAME)).isEqualTo(EnumA.VALUE_1);
    assertThat(mapper.toEnum("")).isNull();
  }


  /**
   * Creates the combination of calls to withNullDefault and withUnknownDefault for every
   * StringEnumMapper
   */
  @DataProvider
  public StringEnumMapper[][] stringEnumMappers_withNullDefault_withUnknownDefault_enum() {
    return new StringEnumMapper[][]{
        {map(EnumA.class).byName().withNullDefault(EnumA.VALUE_1).withUnknownDefault(EnumA.VALUE_1)},
        {map(EnumA.class).byName().withUnknownDefault(EnumA.VALUE_1).withNullDefault(EnumA.VALUE_1)},
        {map(EnumA.class).byName().ignoreCase().withNullDefault(EnumA.VALUE_1).withUnknownDefault(EnumA.VALUE_1)},
        {map(EnumA.class).byName().ignoreCase().withUnknownDefault(EnumA.VALUE_1).withNullDefault(EnumA.VALUE_1)},
    };
  }

  @Test(dataProvider = "stringEnumMappers_withEmptyDefault_withUnknownDefault_enum")
  public void withDefault_for_empty_and_unknown_only_returns_default_value_when_enum_is_null_or_unknown(
      StringEnumMapper<T> mapper) throws Exception {
    assertThat(mapper.toEnum("")).isEqualTo(EnumA.VALUE_1);
    assertThat(mapper.toEnum(UNKNOWN_ENUM_NAME)).isEqualTo(EnumA.VALUE_1);
    assertThat(mapper.toEnum(null)).isNull();
  }


  /**
   * Creates the combination of calls to withNullDefault and withUnknownDefault for every
   * StringEnumMapper
   */
  @DataProvider
  public StringEnumMapper[][] stringEnumMappers_withEmptyDefault_withUnknownDefault_enum() {
    return new StringEnumMapper[][]{
        {map(EnumA.class).byName().withEmptyDefault(EnumA.VALUE_1).withUnknownDefault(EnumA.VALUE_1)},
        {map(EnumA.class).byName().withUnknownDefault(EnumA.VALUE_1).withEmptyDefault(EnumA.VALUE_1)},
        {map(EnumA.class).byName().ignoreCase().withEmptyDefault(EnumA.VALUE_1).withUnknownDefault(EnumA.VALUE_1)},
        {map(EnumA.class).byName().ignoreCase().withUnknownDefault(EnumA.VALUE_1).withEmptyDefault(EnumA.VALUE_1)},
    };
  }

}
