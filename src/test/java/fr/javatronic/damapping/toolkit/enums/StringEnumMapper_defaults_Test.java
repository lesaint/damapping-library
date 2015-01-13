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

import fr.javatronic.damapping.toolkit.MappingDefaults;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static fr.javatronic.damapping.toolkit.enums.EnumA.VALUE_1;
import static fr.javatronic.damapping.toolkit.enums.EnumA.VALUE_2;
import static fr.javatronic.damapping.toolkit.enums.EnumA.VALUE_3;
import static fr.javatronic.damapping.toolkit.enums.StringEnumMappers.map;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * StringEnumMapper_defaults_Test -
 *
 * @author Sébastien Lesaint
 */
public class StringEnumMapper_defaults_Test<T extends Enum<T>> {
  private static final String DEFAULT_STRING_VALUE = "default";
  private static final String UNKNOWN_ENUM_NAME = "unknown_enum_name";

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
        {byName().withDefault(DEFAULT_STRING_VALUE)},
        {byNameIgnoreCase().withDefault(DEFAULT_STRING_VALUE)},
    };
  }

  @Test(dataProvider = "stringEnumMappers_withSameDefault_for_all_cases")
  public void withDefault_returns_default_value_when_enum_is_null_empty_or_unknown(StringEnumMapper<T> mapper)
      throws Exception {
    assertThat(mapper.toEnum(null)).isEqualTo(VALUE_1);
    assertThat(mapper.toEnum("")).isEqualTo(VALUE_1);
    assertThat(mapper.toEnum(UNKNOWN_ENUM_NAME)).isEqualTo(VALUE_1);
  }

  /**
   * Creates the combination of calls to withDefault(Enum) for every StringEnumMapper and the combination of calls to
   * withNullDefault, withEmptyDefault and withUnknownDefault for every StringEnumMapper which will return
   * {@link EnumA#VALUE_1} for every case where source enum value can not be mapped.
   */
  @DataProvider
  public StringEnumMapper[][] stringEnumMappers_withSameDefault_for_all_cases() {
    MappingDefaults<EnumA> alldefaultsToVALUE_1 = MappingDefaults.defaultTo(VALUE_1);

    return new StringEnumMapper[][]{
        {byName().withDefault(VALUE_1)},
        {byNameIgnoreCase().withDefault(VALUE_1)},
        {byToString().withDefault(VALUE_1)},
        {byToStringIgnoreCase().withDefault(VALUE_1)},
        {byCustom().withDefault(VALUE_1)},
        {byCustomIgnoreCase().withDefault(VALUE_1)},
        {byName().withNullDefault(VALUE_1).withEmptyDefault(VALUE_1).withUnknownDefault(VALUE_1)},
        {byName().withNullDefault(VALUE_1).withUnknownDefault(VALUE_1).withEmptyDefault(VALUE_1)},
        {byName().withEmptyDefault(VALUE_1).withNullDefault(VALUE_1).withUnknownDefault(VALUE_1)},
        {byName().withEmptyDefault(VALUE_1).withUnknownDefault(VALUE_1).withNullDefault(VALUE_1)},
        {byName().withUnknownDefault(VALUE_1).withEmptyDefault(VALUE_1).withNullDefault(VALUE_1)},
        {byName().withUnknownDefault(VALUE_1).withNullDefault(VALUE_1).withEmptyDefault(VALUE_1)},
        {byToString().withNullDefault(VALUE_1).withEmptyDefault(VALUE_1).withUnknownDefault(VALUE_1)},
        {byToString().withNullDefault(VALUE_1).withUnknownDefault(VALUE_1).withEmptyDefault(VALUE_1)},
        {byToString().withEmptyDefault(VALUE_1).withNullDefault(VALUE_1).withUnknownDefault(VALUE_1)},
        {byToString().withEmptyDefault(VALUE_1).withUnknownDefault(VALUE_1).withNullDefault(VALUE_1)},
        {byToString().withUnknownDefault(VALUE_1).withEmptyDefault(VALUE_1).withNullDefault(VALUE_1)},
        {byToString().withUnknownDefault(VALUE_1).withNullDefault(VALUE_1).withEmptyDefault(VALUE_1)},
        {byCustom().withNullDefault(VALUE_1).withEmptyDefault(VALUE_1).withUnknownDefault(VALUE_1)},
        {byCustom().withNullDefault(VALUE_1).withUnknownDefault(VALUE_1).withEmptyDefault(VALUE_1)},
        {byCustom().withEmptyDefault(VALUE_1).withNullDefault(VALUE_1).withUnknownDefault(VALUE_1)},
        {byCustom().withEmptyDefault(VALUE_1).withUnknownDefault(VALUE_1).withNullDefault(VALUE_1)},
        {byCustom().withUnknownDefault(VALUE_1).withEmptyDefault(VALUE_1).withNullDefault(VALUE_1)},
        {byCustom().withUnknownDefault(VALUE_1).withNullDefault(VALUE_1).withEmptyDefault(VALUE_1)},
        {byNameIgnoreCase().withNullDefault(VALUE_1).withEmptyDefault(VALUE_1).withUnknownDefault(VALUE_1)},
        {byNameIgnoreCase().withNullDefault(VALUE_1).withUnknownDefault(VALUE_1).withEmptyDefault(VALUE_1)},
        {byNameIgnoreCase().withUnknownDefault(VALUE_1).withNullDefault(VALUE_1).withEmptyDefault(VALUE_1)},
        {byNameIgnoreCase().withEmptyDefault(VALUE_1).withNullDefault(VALUE_1).withUnknownDefault(VALUE_1)},
        {byNameIgnoreCase().withEmptyDefault(VALUE_1).withUnknownDefault(VALUE_1).withNullDefault(VALUE_1)},
        {byNameIgnoreCase().withUnknownDefault(VALUE_1).withEmptyDefault(VALUE_1).withNullDefault(VALUE_1)},
        {byNameIgnoreCase().withUnknownDefault(VALUE_1).withNullDefault(VALUE_1).withEmptyDefault(VALUE_1)},
        {byToStringIgnoreCase().withNullDefault(VALUE_1).withEmptyDefault(VALUE_1).withUnknownDefault(VALUE_1)},
        {byToStringIgnoreCase().withNullDefault(VALUE_1).withUnknownDefault(VALUE_1).withEmptyDefault(VALUE_1)},
        {byToStringIgnoreCase().withUnknownDefault(VALUE_1).withNullDefault(VALUE_1).withEmptyDefault(VALUE_1)},
        {byToStringIgnoreCase().withEmptyDefault(VALUE_1).withNullDefault(VALUE_1).withUnknownDefault(VALUE_1)},
        {byToStringIgnoreCase().withEmptyDefault(VALUE_1).withUnknownDefault(VALUE_1).withNullDefault(VALUE_1)},
        {byToStringIgnoreCase().withUnknownDefault(VALUE_1).withEmptyDefault(VALUE_1).withNullDefault(VALUE_1)},
        {byToStringIgnoreCase().withUnknownDefault(VALUE_1).withNullDefault(VALUE_1).withEmptyDefault(VALUE_1)},
        {byCustomIgnoreCase().withNullDefault(VALUE_1).withEmptyDefault(VALUE_1).withUnknownDefault(VALUE_1)},
        {byCustomIgnoreCase().withNullDefault(VALUE_1).withUnknownDefault(VALUE_1).withEmptyDefault(VALUE_1)},
        {byCustomIgnoreCase().withUnknownDefault(VALUE_1).withNullDefault(VALUE_1).withEmptyDefault(VALUE_1)},
        {byCustomIgnoreCase().withEmptyDefault(VALUE_1).withNullDefault(VALUE_1).withUnknownDefault(VALUE_1)},
        {byCustomIgnoreCase().withEmptyDefault(VALUE_1).withUnknownDefault(VALUE_1).withNullDefault(VALUE_1)},
        {byCustomIgnoreCase().withUnknownDefault(VALUE_1).withEmptyDefault(VALUE_1).withNullDefault(VALUE_1)},
        {byCustomIgnoreCase().withUnknownDefault(VALUE_1).withNullDefault(VALUE_1).withEmptyDefault(VALUE_1)},
        {byName().withEnumDefaults(alldefaultsToVALUE_1)},
        {byNameIgnoreCase().withEnumDefaults(alldefaultsToVALUE_1)},
        {byToString().withEnumDefaults(alldefaultsToVALUE_1)},
        {byToStringIgnoreCase().withEnumDefaults(alldefaultsToVALUE_1)},
        {byCustom().withEnumDefaults(alldefaultsToVALUE_1)},
        {byCustomIgnoreCase().withEnumDefaults(alldefaultsToVALUE_1)}
    };
  }

  @Test(dataProvider = "stringEnumMappers_withNullDefault_enum")
  public void withDefault_for_null_only_returns_default_value_when_enum_is_null(StringEnumMapper<T> mapper)
      throws Exception {
    assertThat(mapper.toEnum(null)).isEqualTo(VALUE_1);
    assertThat(mapper.toEnum("")).isNull();
    assertThat(mapper.toEnum(UNKNOWN_ENUM_NAME)).isNull();
  }

  /**
   * Creates the combination of calls to withNullDefault for every StringEnumMapper
   */
  @DataProvider
  public StringEnumMapper[][] stringEnumMappers_withNullDefault_enum() {
    MappingDefaults<EnumA> nulldefaultsToVALUE_1 = MappingDefaults.<EnumA>defaultToNull().withNullDefault(VALUE_1);
    return new StringEnumMapper[][]{
        {byName().withNullDefault(VALUE_1)},
        {byNameIgnoreCase().withNullDefault(VALUE_1)},
        {byToString().withNullDefault(VALUE_1)},
        {byToStringIgnoreCase().withNullDefault(VALUE_1)},
        {byCustom().withNullDefault(VALUE_1)},
        {byToStringIgnoreCase().withNullDefault(VALUE_1)},
        {byName().withEnumDefaults(nulldefaultsToVALUE_1)},
        {byNameIgnoreCase().withEnumDefaults(nulldefaultsToVALUE_1)},
        {byToString().withEnumDefaults(nulldefaultsToVALUE_1)},
        {byToStringIgnoreCase().withEnumDefaults(nulldefaultsToVALUE_1)},
        {byCustom().withEnumDefaults(nulldefaultsToVALUE_1)},
        {byCustomIgnoreCase().withEnumDefaults(nulldefaultsToVALUE_1)}
    };
  }

  @Test(dataProvider = "stringEnumMappers_withEmptyDefault_enum")
  public void withDefault_for_empty_only_returns_default_value_when_enum_is_empty(StringEnumMapper<T> mapper)
      throws Exception {
    assertThat(mapper.toEnum("")).isEqualTo(VALUE_1);
    assertThat(mapper.toEnum(null)).isNull();
    assertThat(mapper.toEnum(UNKNOWN_ENUM_NAME)).isNull();
  }

  /**
   * Creates the combination of calls to withEmptyDefault for every StringEnumMapper
   */
  @DataProvider
  public StringEnumMapper[][] stringEnumMappers_withEmptyDefault_enum() {
    MappingDefaults<EnumA> emptydefaultsToVALUE_1 = MappingDefaults.<EnumA>defaultToNull().withEmptyDefault(VALUE_1);
    return new StringEnumMapper[][]{
        {byName().withEmptyDefault(VALUE_1)},
        {byNameIgnoreCase().withEmptyDefault(VALUE_1)},
        {byToString().withEmptyDefault(VALUE_1)},
        {byToStringIgnoreCase().withEmptyDefault(VALUE_1)},
        {byCustom().withEmptyDefault(VALUE_1)},
        {byCustomIgnoreCase().withEmptyDefault(VALUE_1)},
        {byName().withEnumDefaults(emptydefaultsToVALUE_1)},
        {byNameIgnoreCase().withEnumDefaults(emptydefaultsToVALUE_1)},
        {byToString().withEnumDefaults(emptydefaultsToVALUE_1)},
        {byToStringIgnoreCase().withEnumDefaults(emptydefaultsToVALUE_1)},
        {byCustom().withEnumDefaults(emptydefaultsToVALUE_1)},
        {byCustomIgnoreCase().withEnumDefaults(emptydefaultsToVALUE_1)}
    };
  }

  @Test(dataProvider = "stringEnumMappers_withUnknownDefault_enum")
  public void withDefault_for_Unknown_only_returns_default_value_when_enum_is_unknown(StringEnumMapper<T> mapper)
      throws Exception {
    assertThat(mapper.toEnum(UNKNOWN_ENUM_NAME)).isEqualTo(VALUE_1);
    assertThat(mapper.toEnum(null)).isNull();
    assertThat(mapper.toEnum("")).isNull();
  }

  /**
   * Creates the combination of calls to withUnknownDefault for every StringEnumMapper
   */
  @DataProvider
  public StringEnumMapper[][] stringEnumMappers_withUnknownDefault_enum() {
    MappingDefaults<EnumA> unknwondefaultsToVALUE_1 = MappingDefaults.<EnumA>defaultToNull().withUnknownDefault(VALUE_1);
    return new StringEnumMapper[][]{
        {byName().withUnknownDefault(VALUE_1)},
        {byNameIgnoreCase().withUnknownDefault(VALUE_1)},
        {byToString().withUnknownDefault(VALUE_1)},
        {byToStringIgnoreCase().withUnknownDefault(VALUE_1)},
        {byCustom().withUnknownDefault(VALUE_1)},
        {byCustomIgnoreCase().withUnknownDefault(VALUE_1)},
        {byName().withEnumDefaults(unknwondefaultsToVALUE_1)},
        {byNameIgnoreCase().withEnumDefaults(unknwondefaultsToVALUE_1)},
        {byToString().withEnumDefaults(unknwondefaultsToVALUE_1)},
        {byToStringIgnoreCase().withEnumDefaults(unknwondefaultsToVALUE_1)},
        {byCustom().withEnumDefaults(unknwondefaultsToVALUE_1)},
        {byCustomIgnoreCase().withEnumDefaults(unknwondefaultsToVALUE_1)}
    };
  }

  @Test(dataProvider = "stringEnumMappers_withNullDefault_withEmptyDefault_enum")
  public void withDefault_for_empty_and_null_only_returns_default_value_when_enum_is_null_or_empty(
      StringEnumMapper<T> mapper) throws Exception {
    assertThat(mapper.toEnum(null)).isEqualTo(VALUE_1);
    assertThat(mapper.toEnum("")).isEqualTo(VALUE_1);
    assertThat(mapper.toEnum(UNKNOWN_ENUM_NAME)).isNull();
  }


  /**
   * Creates the combination of calls to withNullDefault and withEmptyDefault for every
   * StringEnumMapper
   */
  @DataProvider
  public StringEnumMapper[][] stringEnumMappers_withNullDefault_withEmptyDefault_enum() {
    MappingDefaults<EnumA> defaults = MappingDefaults.<EnumA>defaultToNull().withNullDefault(VALUE_1).withEmptyDefault(VALUE_1);
    return new StringEnumMapper[][]{
        {byName().withNullDefault(VALUE_1).withEmptyDefault(VALUE_1)},
        {byName().withEmptyDefault(VALUE_1).withNullDefault(VALUE_1)},
        {byNameIgnoreCase().withNullDefault(VALUE_1).withEmptyDefault(VALUE_1)},
        {byNameIgnoreCase().withEmptyDefault(VALUE_1).withNullDefault(VALUE_1)},
        {byToString().withNullDefault(VALUE_1).withEmptyDefault(VALUE_1)},
        {byToString().withEmptyDefault(VALUE_1).withNullDefault(VALUE_1)},
        {byToStringIgnoreCase().withNullDefault(VALUE_1).withEmptyDefault(VALUE_1)},
        {byToStringIgnoreCase().withEmptyDefault(VALUE_1).withNullDefault(VALUE_1)},
        {byCustom().withNullDefault(VALUE_1).withEmptyDefault(VALUE_1)},
        {byCustom().withEmptyDefault(VALUE_1).withNullDefault(VALUE_1)},
        {byCustomIgnoreCase().withNullDefault(VALUE_1).withEmptyDefault(VALUE_1)},
        {byCustomIgnoreCase().withEmptyDefault(VALUE_1).withNullDefault(VALUE_1)},
        {byName().withEnumDefaults(defaults)},
        {byNameIgnoreCase().withEnumDefaults(defaults)},
        {byToString().withEnumDefaults(defaults)},
        {byToStringIgnoreCase().withEnumDefaults(defaults)},
        {byCustom().withEmptyDefault(VALUE_1).withNullDefault(VALUE_1)},
        {byCustomIgnoreCase().withEnumDefaults(defaults)},
    };
  }

  @Test(dataProvider = "stringEnumMappers_withNullDefault_withUnknownDefault_enum")
  public void withDefault_for_null_and_unknown_only_returns_default_value_when_enum_is_null_or_unknown(
      StringEnumMapper<T> mapper) throws Exception {
    assertThat(mapper.toEnum(null)).isEqualTo(VALUE_1);
    assertThat(mapper.toEnum(UNKNOWN_ENUM_NAME)).isEqualTo(VALUE_1);
    assertThat(mapper.toEnum("")).isNull();
  }


  /**
   * Creates the combination of calls to withNullDefault and withUnknownDefault for every
   * StringEnumMapper
   */
  @DataProvider
  public StringEnumMapper[][] stringEnumMappers_withNullDefault_withUnknownDefault_enum() {
    MappingDefaults<EnumA> defaults = MappingDefaults.<EnumA>defaultToNull().withNullDefault(VALUE_1).withUnknownDefault(
        VALUE_1
    );
    return new StringEnumMapper[][]{
        {byName().withNullDefault(VALUE_1).withUnknownDefault(VALUE_1)},
        {byName().withUnknownDefault(VALUE_1).withNullDefault(VALUE_1)},
        {byNameIgnoreCase().withNullDefault(VALUE_1).withUnknownDefault(VALUE_1)},
        {byNameIgnoreCase().withUnknownDefault(VALUE_1).withNullDefault(VALUE_1)},
        {byToString().withNullDefault(VALUE_1).withUnknownDefault(VALUE_1)},
        {byToString().withUnknownDefault(VALUE_1).withNullDefault(VALUE_1)},
        {byToStringIgnoreCase().withNullDefault(VALUE_1).withUnknownDefault(VALUE_1)},
        {byToStringIgnoreCase().withUnknownDefault(VALUE_1).withNullDefault(VALUE_1)},
        {byCustom().withNullDefault(VALUE_1).withUnknownDefault(VALUE_1)},
        {byCustom().withUnknownDefault(VALUE_1).withNullDefault(VALUE_1)},
        {byCustomIgnoreCase().withNullDefault(VALUE_1).withUnknownDefault(VALUE_1)},
        {byCustomIgnoreCase().withUnknownDefault(VALUE_1).withNullDefault(VALUE_1)},
        {byName().withEnumDefaults(defaults)},
        {byNameIgnoreCase().withEnumDefaults(defaults)},
        {byToString().withEnumDefaults(defaults)},
        {byToStringIgnoreCase().withEnumDefaults(defaults)},
        {byCustom().withEnumDefaults(defaults)},
        {byCustomIgnoreCase().withEnumDefaults(defaults)},
    };
  }

  @Test(dataProvider = "stringEnumMappers_withEmptyDefault_withUnknownDefault_enum")
  public void withDefault_for_empty_and_unknown_only_returns_default_value_when_enum_is_null_or_unknown(
      StringEnumMapper<T> mapper) throws Exception {
    assertThat(mapper.toEnum(null)).isNull();
    assertThat(mapper.toEnum("")).isEqualTo(VALUE_1);
    assertThat(mapper.toEnum(UNKNOWN_ENUM_NAME)).isEqualTo(VALUE_1);
  }


  /**
   * Creates the combination of calls to withNullDefault and withUnknownDefault for every
   * StringEnumMapper
   */
  @DataProvider
  public StringEnumMapper<?>[][] stringEnumMappers_withEmptyDefault_withUnknownDefault_enum() {
    MappingDefaults<EnumA> defaults = MappingDefaults.<EnumA>defaultToNull().withEmptyDefault(VALUE_1).withUnknownDefault(VALUE_1);
    return new StringEnumMapper[][]{
        {byName().withEmptyDefault(VALUE_1).withUnknownDefault(VALUE_1)},
        {byName().withUnknownDefault(VALUE_1).withEmptyDefault(VALUE_1)},
        {byNameIgnoreCase().withEmptyDefault(VALUE_1).withUnknownDefault(VALUE_1)},
        {byNameIgnoreCase().withUnknownDefault(VALUE_1).withEmptyDefault(VALUE_1)},
        {byToString().withEmptyDefault(VALUE_1).withUnknownDefault(VALUE_1)},
        {byToString().withUnknownDefault(VALUE_1).withEmptyDefault(VALUE_1)},
        {byToStringIgnoreCase().withEmptyDefault(VALUE_1).withUnknownDefault(VALUE_1)},
        {byToStringIgnoreCase().withUnknownDefault(VALUE_1).withEmptyDefault(VALUE_1)},
        {byCustom().withEmptyDefault(VALUE_1).withUnknownDefault(VALUE_1)},
        {byCustom().withUnknownDefault(VALUE_1).withEmptyDefault(VALUE_1)},
        {byCustomIgnoreCase().withEmptyDefault(VALUE_1).withUnknownDefault(VALUE_1)},
        {byCustomIgnoreCase().withUnknownDefault(VALUE_1).withEmptyDefault(VALUE_1)},
        {byName().withEnumDefaults(defaults)},
        {byNameIgnoreCase().withEnumDefaults(defaults)},
        {byToString().withEnumDefaults(defaults)},
        {byToStringIgnoreCase().withEnumDefaults(defaults)},
        {byCustom().withEnumDefaults(defaults)},
        {byCustomIgnoreCase().withEnumDefaults(defaults)},
    };
  }


  @Test(dataProvider = "stringEnumMappers_with_different_default_for_each_case_enum")
  public void can_return_a_specific_default_for_each_case(
      StringEnumMapper<T> mapper) throws Exception {
    assertThat(mapper.toEnum(null)).isEqualTo(VALUE_1);
    assertThat(mapper.toEnum("")).isEqualTo(VALUE_2);
    assertThat(mapper.toEnum(UNKNOWN_ENUM_NAME)).isEqualTo(VALUE_3);
  }


  @DataProvider
  public StringEnumMapper<?>[][] stringEnumMappers_with_different_default_for_each_case_enum() {
    MappingDefaults<EnumA> defaults = MappingDefaults.defaultTo(VALUE_3)
                                                     .withNullDefault(VALUE_1)
                                                     .withEmptyDefault(VALUE_2);
    return new StringEnumMapper[][] {
        {byName().withNullDefault(VALUE_1).withEmptyDefault(VALUE_2).withUnknownDefault(VALUE_3)},
        {byName().withNullDefault(VALUE_1).withUnknownDefault(VALUE_3).withEmptyDefault(VALUE_2)},
        {byName().withEmptyDefault(VALUE_2).withNullDefault(VALUE_1).withUnknownDefault(VALUE_3)},
        {byName().withEmptyDefault(VALUE_2).withUnknownDefault(VALUE_3).withNullDefault(VALUE_1)},
        {byName().withUnknownDefault(VALUE_3).withEmptyDefault(VALUE_2).withNullDefault(VALUE_1)},
        {byName().withUnknownDefault(VALUE_3).withNullDefault(VALUE_1).withEmptyDefault(VALUE_2)},

        {byNameIgnoreCase().withNullDefault(VALUE_1).withEmptyDefault(VALUE_2).withUnknownDefault(VALUE_3)},
        {byNameIgnoreCase().withNullDefault(VALUE_1).withUnknownDefault(VALUE_3).withEmptyDefault(VALUE_2)},
        {byNameIgnoreCase().withEmptyDefault(VALUE_2).withNullDefault(VALUE_1).withUnknownDefault(VALUE_3)},
        {byNameIgnoreCase().withEmptyDefault(VALUE_2).withUnknownDefault(VALUE_3).withNullDefault(VALUE_1)},
        {byNameIgnoreCase().withUnknownDefault(VALUE_3).withEmptyDefault(VALUE_2).withNullDefault(VALUE_1)},
        {byNameIgnoreCase().withUnknownDefault(VALUE_3).withNullDefault(VALUE_1).withEmptyDefault(VALUE_2)},

        {byToString().withNullDefault(VALUE_1).withEmptyDefault(VALUE_2).withUnknownDefault(VALUE_3)},
        {byToString().withNullDefault(VALUE_1).withUnknownDefault(VALUE_3).withEmptyDefault(VALUE_2)},
        {byToString().withEmptyDefault(VALUE_2).withNullDefault(VALUE_1).withUnknownDefault(VALUE_3)},
        {byToString().withEmptyDefault(VALUE_2).withUnknownDefault(VALUE_3).withNullDefault(VALUE_1)},
        {byToString().withUnknownDefault(VALUE_3).withEmptyDefault(VALUE_2).withNullDefault(VALUE_1)},
        {byToString().withUnknownDefault(VALUE_3).withNullDefault(VALUE_1).withEmptyDefault(VALUE_2)},

        {byToStringIgnoreCase().withNullDefault(VALUE_1).withEmptyDefault(VALUE_2).withUnknownDefault(VALUE_3)},
        {byToStringIgnoreCase().withNullDefault(VALUE_1).withUnknownDefault(VALUE_3).withEmptyDefault(VALUE_2)},
        {byToStringIgnoreCase().withEmptyDefault(VALUE_2).withNullDefault(VALUE_1).withUnknownDefault(VALUE_3)},
        {byToStringIgnoreCase().withEmptyDefault(VALUE_2).withUnknownDefault(VALUE_3).withNullDefault(VALUE_1)},
        {byToStringIgnoreCase().withUnknownDefault(VALUE_3).withEmptyDefault(VALUE_2).withNullDefault(VALUE_1)},
        {byToStringIgnoreCase().withUnknownDefault(VALUE_3).withNullDefault(VALUE_1).withEmptyDefault(VALUE_2)},

        {byCustom().withNullDefault(VALUE_1).withEmptyDefault(VALUE_2).withUnknownDefault(VALUE_3)},
        {byCustom().withNullDefault(VALUE_1).withUnknownDefault(VALUE_3).withEmptyDefault(VALUE_2)},
        {byCustom().withEmptyDefault(VALUE_2).withNullDefault(VALUE_1).withUnknownDefault(VALUE_3)},
        {byCustom().withEmptyDefault(VALUE_2).withUnknownDefault(VALUE_3).withNullDefault(VALUE_1)},
        {byCustom().withUnknownDefault(VALUE_3).withEmptyDefault(VALUE_2).withNullDefault(VALUE_1)},
        {byCustom().withUnknownDefault(VALUE_3).withNullDefault(VALUE_1).withEmptyDefault(VALUE_2)},

        {byNameIgnoreCase().withNullDefault(VALUE_1).withEmptyDefault(VALUE_2).withUnknownDefault(VALUE_3)},
        {byNameIgnoreCase().withNullDefault(VALUE_1).withUnknownDefault(VALUE_3).withEmptyDefault(VALUE_2)},
        {byNameIgnoreCase().withEmptyDefault(VALUE_2).withNullDefault(VALUE_1).withUnknownDefault(VALUE_3)},
        {byNameIgnoreCase().withEmptyDefault(VALUE_2).withUnknownDefault(VALUE_3).withNullDefault(VALUE_1)},
        {byNameIgnoreCase().withUnknownDefault(VALUE_3).withEmptyDefault(VALUE_2).withNullDefault(VALUE_1)},
        {byNameIgnoreCase().withUnknownDefault(VALUE_3).withNullDefault(VALUE_1).withEmptyDefault(VALUE_2)},

        {byName().withEnumDefaults(defaults)},
        {byNameIgnoreCase().withEnumDefaults(defaults)},
        {byToString().withEnumDefaults(defaults)},
        {byToStringIgnoreCase().withEnumDefaults(defaults)},
        {byCustom().withEnumDefaults(defaults)},
        {byCustomIgnoreCase().withEnumDefaults(defaults)},
    };
  }

  /*==========*
  * utilities *
   *==========*/
  private static StringEnumMapper<EnumA> byName() {
    return map(EnumA.class).byName();
  }

  private static StringEnumMapper<EnumA> byNameIgnoreCase() {
    return byName().ignoreCase();
  }

  private static StringEnumMapper<EnumA> byToString() {
    return map(EnumA.class).byToString();
  }

  private static StringEnumMapper<EnumA> byToStringIgnoreCase() {
    return map(EnumA.class).byToString().ignoreCase();
  }

  private static StringEnumMapper<EnumA> byCustom() {
    return map(EnumA.class).by(WeirdEnumATransformer.INSTANCE);
  }

  private static StringEnumMapper<EnumA> byCustomIgnoreCase() {
    return map(EnumA.class).by(WeirdEnumATransformer.INSTANCE).ignoreCase();
  }
}
