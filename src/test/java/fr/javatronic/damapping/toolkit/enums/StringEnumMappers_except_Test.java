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

import fr.javatronic.damapping.toolkit.BiMapping;
import fr.javatronic.damapping.toolkit.Mappings;

import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static fr.javatronic.damapping.toolkit.enums.EnumA.VALUE_1;
import static org.assertj.core.api.Assertions.assertThat;

public class StringEnumMappers_except_Test {

  private static final ImmutableList<StringEnumMapper<EnumA>> BASE_MAPPERS = ImmutableList.of(
      StringEnumMappers.map(EnumA.class).byName(),
      StringEnumMappers.map(EnumA.class).byName().ignoreCase(),
      StringEnumMappers.map(EnumA.class).byToString(),
      StringEnumMappers.map(EnumA.class).byToString().ignoreCase(),
      StringEnumMappers.map(EnumA.class).by(WeirdEnumATransformer.INSTANCE),
      StringEnumMappers.map(EnumA.class).by(WeirdEnumATransformer.INSTANCE).ignoreCase()
  );

  private static final EnumA NULL_ENUM = (EnumA) null;
  private static final String NULL_STRING = (String) null;
  private static final String SOME_STRING = "some string";
  private static final BiMapping<EnumA, String> NULL_BIMAPPING = (BiMapping<EnumA, String>) null;

  @Test(dataProvider = "base_mappers", expectedExceptions = NullPointerException.class)
  public void except_enum_to_string_throws_NPE_if_enum_is_null(StringEnumMapper<EnumA> mapper) throws Exception {
    mapper.except(NULL_ENUM, SOME_STRING);
  }

  @Test(dataProvider = "base_mappers", expectedExceptions = NullPointerException.class)
  public void except_enum_to_string_throws_NPE_if_string_is_null(StringEnumMapper<EnumA> mapper) throws Exception {
    mapper.except(VALUE_1, NULL_STRING);
  }

  @Test(dataProvider = "base_mappers", expectedExceptions = NullPointerException.class)
  public void except_string_to_enum_throws_NPE_if_string_is_null(StringEnumMapper<EnumA> mapper) throws Exception {
    mapper.except(NULL_STRING, VALUE_1);
  }

  @Test(dataProvider = "base_mappers", expectedExceptions = NullPointerException.class)
  public void except_string_to_enum_throws_NPE_if_enum_is_null(StringEnumMapper<EnumA> mapper) throws Exception {
    mapper.except(SOME_STRING, NULL_ENUM);
  }

  @Test(dataProvider = "base_mappers", expectedExceptions = NullPointerException.class)
  public void except_bimapping_throws_NPE_if_bimapping_is_null(StringEnumMapper<EnumA> mapper) throws Exception {
    mapper.except(NULL_BIMAPPING);
  }

  @Test(dataProvider = "base_mappers", expectedExceptions = NullPointerException.class)
  public void except_enum_throws_NPE_if_enum_is_null(StringEnumMapper<EnumA> mapper) throws Exception {
    mapper.except(NULL_ENUM);
  }

  @Test(dataProvider = "base_mappers", expectedExceptions = NullPointerException.class)
  public void except_string_throws_NPE_if_string_is_null(StringEnumMapper<EnumA> mapper) throws Exception {
    mapper.except(NULL_STRING);
  }

  @Test(dataProvider = "base_mappers")
  public void except_enum_to_string_affects_mapping_only_from_the_specified_enum_value(StringEnumMapper<EnumA> mapper)
      throws Exception {
    StringEnumMapper<EnumA> exceptMapper = mapper.except(VALUE_1, SOME_STRING);

    for (EnumA enumA : EnumA.values()) {
      if (enumA == VALUE_1) {
        assertThat(exceptMapper.toString(enumA)).isEqualTo(SOME_STRING);
      }
      else {
        assertThat(exceptMapper.toString(enumA)).isEqualTo(mapper.toString(enumA));
      }
    }
  }

  @Test(dataProvider = "base_mappers")
  public void except_enum_affects_mapping_only_from_the_specified_enum_value(StringEnumMapper<EnumA> mapper)
      throws Exception {
    StringEnumMapper<EnumA> exceptMapper = mapper.except(VALUE_1);

    for (EnumA enumA : EnumA.values()) {
      if (enumA == VALUE_1) {
        assertThat(exceptMapper.toString(enumA)).isNull();
      }
      else {
        assertThat(exceptMapper.toString(enumA)).isEqualTo(mapper.toString(enumA));
      }
    }
  }

  @Test(dataProvider = "base_mappers")
  public void except_enum_to_string_does_not_change_mapping_from_string(StringEnumMapper<EnumA> mapper)
      throws Exception {
    StringEnumMapper<EnumA> exceptMapper = mapper.except(VALUE_1, SOME_STRING);

    for (EnumA enumA : EnumA.values()) {
      String str = mapper.toString(enumA);
      assertThat(exceptMapper.toEnum(str)).isEqualTo(enumA);
    }
  }

  @Test(dataProvider = "base_mappers")
  public void except_string_to_enum_adds_an_extra_mapping_to_an_enum(StringEnumMapper<EnumA> mapper) throws Exception {
    StringEnumMapper<EnumA> exceptMapper = mapper.except(SOME_STRING, VALUE_1);

    assertThat(mapper.toEnum(SOME_STRING)).isNull();
    assertThat(exceptMapper.toEnum(SOME_STRING)).isEqualTo(VALUE_1);
  }

  @Test(dataProvider = "base_mappers")
  public void except_string_to_enum_affects_mapping_only_from_the_specified_string_value(StringEnumMapper<EnumA> mapper)
      throws Exception {
    StringEnumMapper<EnumA> exceptMapper = mapper.except(SOME_STRING, VALUE_1);

    for (EnumA enumA : EnumA.values()) {
      String str = mapper.toString(enumA);
      assertThat(exceptMapper.toEnum(str)).isEqualTo(mapper.toEnum(str));
    }
  }

  @Test(dataProvider = "base_mappers")
  public void except_string_affects_mapping_only_from_the_specified_string_value(StringEnumMapper<EnumA> mapper)
      throws Exception {
    StringEnumMapper<EnumA> exceptMapper = mapper.except(mapper.toString(VALUE_1));

    for (EnumA enumA : EnumA.values()) {
      String str = mapper.toString(enumA);
      if (enumA == VALUE_1) {
        assertThat(exceptMapper.toEnum(str)).isNull();
      }
      else {
        assertThat(exceptMapper.toEnum(str)).isEqualTo(mapper.toEnum(str));
      }
    }
  }

  @Test(dataProvider = "base_mappers")
  public void except_bimapping_overwrites_mapping_from_enum_and_adds_one_from_string(StringEnumMapper<EnumA> mapper)
      throws Exception {
    StringEnumMapper<EnumA> exceptMapper = mapper.except(Mappings.bimapping(VALUE_1, SOME_STRING));

    assertThat(mapper.toEnum(SOME_STRING)).isNull();
    assertThat(exceptMapper.toEnum(SOME_STRING)).isEqualTo(VALUE_1);
    assertThat(exceptMapper.toString(VALUE_1)).isEqualTo(SOME_STRING);
  }

  @DataProvider
  public Iterator<Object[]> base_mappers() {
    return EnumA.createCombination(
        BASE_MAPPERS,
        new Function<StringEnumMapper<EnumA>, List<StringEnumMapper<EnumA>>>() {
          @Nullable
          @Override
          public List<StringEnumMapper<EnumA>> apply(StringEnumMapper<EnumA> input) {
            return ImmutableList.of(input);
          }
        }
    );
  }
}