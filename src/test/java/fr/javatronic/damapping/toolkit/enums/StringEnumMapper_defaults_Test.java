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

import java.util.Iterator;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.google.common.base.Function;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static com.google.common.collect.ImmutableList.of;
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
  private static final List<StringEnumMapper<EnumA>> BASE_ENUM_MAPPERS = of(
      byName(),
      byNameIgnoreCase(),
      byToString(),
      byToStringIgnoreCase(),
      byCustom(),
      byCustomIgnoreCase()
  );

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
  public Iterator<Object[]> stringEnumMappers_withSameDefault_for_all_cases() {
    final MappingDefaults<EnumA> alldefaultsToVALUE_1 = MappingDefaults.defaultTo(VALUE_1);
    return EnumA.createCombination(
        BASE_ENUM_MAPPERS,
        new Function<StringEnumMapper<EnumA>, List<StringEnumMapper<EnumA>>>() {
          @Nullable
          @Override
          public List<StringEnumMapper<EnumA>> apply(@Nonnull StringEnumMapper<EnumA> input) {
            return of(
                input.withDefault(VALUE_1),
                input.withNullDefault(VALUE_1).withEmptyDefault(VALUE_1).withUnknownDefault(VALUE_1),
                input.withNullDefault(VALUE_1).withUnknownDefault(VALUE_1).withEmptyDefault(VALUE_1),
                input.withEmptyDefault(VALUE_1).withNullDefault(VALUE_1).withUnknownDefault(VALUE_1),
                input.withEmptyDefault(VALUE_1).withUnknownDefault(VALUE_1).withNullDefault(VALUE_1),
                input.withUnknownDefault(VALUE_1).withEmptyDefault(VALUE_1).withNullDefault(VALUE_1),
                input.withUnknownDefault(VALUE_1).withNullDefault(VALUE_1).withEmptyDefault(VALUE_1),
                input.withEnumDefaults(alldefaultsToVALUE_1)
            );
          }
        }
    );
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
  public Iterator<Object[]> stringEnumMappers_withNullDefault_enum() {
    final MappingDefaults<EnumA> nulldefaultsToVALUE_1 = MappingDefaults.<EnumA>defaultToNull()
                                                                        .withNullDefault(VALUE_1);
    return EnumA.createCombination(
        BASE_ENUM_MAPPERS,
        new Function<StringEnumMapper<EnumA>, List<StringEnumMapper<EnumA>>>() {
          @Nullable
          @Override
          public List<StringEnumMapper<EnumA>> apply(@Nonnull StringEnumMapper<EnumA> input) {
            return of(
                input.withNullDefault(VALUE_1),
                input.withEnumDefaults(nulldefaultsToVALUE_1)
            );
          }
        }
    );
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
  public Iterator<Object[]> stringEnumMappers_withEmptyDefault_enum() {
    final MappingDefaults<EnumA> emptydefaultsToVALUE_1 = MappingDefaults.<EnumA>defaultToNull()
                                                                         .withEmptyDefault(VALUE_1);
    return EnumA.createCombination(
        BASE_ENUM_MAPPERS,
        new Function<StringEnumMapper<EnumA>, List<StringEnumMapper<EnumA>>>() {
          @Nullable
          @Override
          public List<StringEnumMapper<EnumA>> apply(@Nonnull StringEnumMapper<EnumA> input) {
            return of(
                input.withEmptyDefault(VALUE_1),
                input.withEnumDefaults(emptydefaultsToVALUE_1)
            );
          }
        }
    );
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
  public Iterator<Object[]> stringEnumMappers_withUnknownDefault_enum() {
    final MappingDefaults<EnumA> unknwonDefaultsToVALUE_1 = MappingDefaults.<EnumA>defaultToNull()
                                                                     .withUnknownDefault(VALUE_1);
    return EnumA.createCombination(
        BASE_ENUM_MAPPERS,
        new Function<StringEnumMapper<EnumA>, List<StringEnumMapper<EnumA>>>() {
          @Nullable
          @Override
          public List<StringEnumMapper<EnumA>> apply(@Nonnull StringEnumMapper<EnumA> input) {
            return of(
                input.withUnknownDefault(VALUE_1),
                input.withEnumDefaults(unknwonDefaultsToVALUE_1)
            );
          }
        }
    );
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
  public Iterator<Object[]> stringEnumMappers_withNullDefault_withEmptyDefault_enum() {
    final MappingDefaults<EnumA> defaults = MappingDefaults.<EnumA>defaultToNull()
                                                     .withNullDefault(VALUE_1)
                                                     .withEmptyDefault(VALUE_1);
    return EnumA.createCombination(
        BASE_ENUM_MAPPERS,
        new Function<StringEnumMapper<EnumA>, List<StringEnumMapper<EnumA>>>() {
          @Nullable
          @Override
          public List<StringEnumMapper<EnumA>> apply(@Nonnull StringEnumMapper<EnumA> input) {
            return of(
                input.withNullDefault(VALUE_1).withEmptyDefault(VALUE_1),
                input.withEmptyDefault(VALUE_1).withNullDefault(VALUE_1),
                input.withEnumDefaults(defaults)
            );
          }
        }
    );
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
  public Iterator<Object[]> stringEnumMappers_withNullDefault_withUnknownDefault_enum() {
    final MappingDefaults<EnumA> defaults = MappingDefaults.<EnumA>defaultToNull()
                                                     .withNullDefault(VALUE_1)
                                                     .withUnknownDefault(VALUE_1);
    return EnumA.createCombination(
        BASE_ENUM_MAPPERS,
        new Function<StringEnumMapper<EnumA>, List<StringEnumMapper<EnumA>>>() {
          @Nullable
          @Override
          public List<StringEnumMapper<EnumA>> apply(@Nonnull StringEnumMapper<EnumA> input) {
            return of(
                input.withNullDefault(VALUE_1).withUnknownDefault(VALUE_1),
                input.withUnknownDefault(VALUE_1).withNullDefault(VALUE_1),
                input.withEnumDefaults(defaults)
            );
          }
        }
    );
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
  public Iterator<Object[]> stringEnumMappers_withEmptyDefault_withUnknownDefault_enum() {
    final MappingDefaults<EnumA> defaults = MappingDefaults.<EnumA>defaultToNull()
                                                     .withEmptyDefault(VALUE_1)
                                                     .withUnknownDefault(VALUE_1);
    return EnumA.createCombination(
        BASE_ENUM_MAPPERS,
        new Function<StringEnumMapper<EnumA>, List<StringEnumMapper<EnumA>>>() {
          @Nullable
          @Override
          public List<StringEnumMapper<EnumA>> apply(@Nonnull StringEnumMapper<EnumA> input) {
            return of(
                input.withEmptyDefault(VALUE_1).withUnknownDefault(VALUE_1),
                input.withUnknownDefault(VALUE_1).withEmptyDefault(VALUE_1),
                input.withEnumDefaults(defaults)
            );
          }
        }
    );
  }


  @Test(dataProvider = "stringEnumMappers_with_different_default_for_each_case_enum")
  public void can_return_a_specific_default_for_each_case(
      StringEnumMapper<T> mapper) throws Exception {
    assertThat(mapper.toEnum(null)).isEqualTo(VALUE_1);
    assertThat(mapper.toEnum("")).isEqualTo(VALUE_2);
    assertThat(mapper.toEnum(UNKNOWN_ENUM_NAME)).isEqualTo(VALUE_3);
  }


  @DataProvider
  public Iterator<Object[]> stringEnumMappers_with_different_default_for_each_case_enum() {
    final MappingDefaults<EnumA> defaults = MappingDefaults.defaultTo(VALUE_3)
                                                     .withNullDefault(VALUE_1)
                                                     .withEmptyDefault(VALUE_2);
    return EnumA.createCombination(
        BASE_ENUM_MAPPERS,
        new Function<StringEnumMapper<EnumA>, List<StringEnumMapper<EnumA>>>() {
          @Nullable
          @Override
          public List<StringEnumMapper<EnumA>> apply(@Nonnull StringEnumMapper<EnumA> input) {
            return of(
                input.withNullDefault(VALUE_1).withEmptyDefault(VALUE_2).withUnknownDefault(VALUE_3),
                input.withNullDefault(VALUE_1).withUnknownDefault(VALUE_3).withEmptyDefault(VALUE_2),
                input.withEmptyDefault(VALUE_2).withNullDefault(VALUE_1).withUnknownDefault(VALUE_3),
                input.withEmptyDefault(VALUE_2).withUnknownDefault(VALUE_3).withNullDefault(VALUE_1),
                input.withUnknownDefault(VALUE_3).withEmptyDefault(VALUE_2).withNullDefault(VALUE_1),
                input.withUnknownDefault(VALUE_3).withNullDefault(VALUE_1).withEmptyDefault(VALUE_2),
                input.withEnumDefaults(defaults)
            );
          }
        }
    );
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
