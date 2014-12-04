package fr.javatronic.damapping.library;

import javax.annotation.Nonnull;

import org.testng.annotations.Test;

import static fr.javatronic.damapping.library.StringEnumMapper.map;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * StringEnumMapperTest - Unit tests for class {@link StringEnumMapper}.
 *
 * @author Sébastien Lesaint
 */
public class StringEnumMapperTest {

  private static final String DUMMY_STRING_VALUE = "fooBarAcme";

  /********
   * byName
   ********/
  @Test
  public void map_byName_toString_returns_null_for_null_value() throws Exception {
    assertThat(map(EnumA.class).byName().toString(null)).isNull();
  }

  @Test
  public void map_byName_toString_returns_name_value() throws Exception {
    StringEnumMapper.ByNameEnumMapper<EnumA> mapper = map(EnumA.class).byName();

    for (EnumA enumA : EnumA.values()) {
      assertThat(mapper.toString(enumA)).isEqualTo(enumA.name());
    }
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
    StringEnumMapper.ByNameEnumMapper<EnumA> mapper = map(EnumA.class).byName();

    for (EnumA enumA : EnumA.values()) {
      assertThat(mapper.toEnum(enumA.name())).isEqualTo(enumA);
    }
  }

  @Test
  public void map_byName_toEnum_returns_null_for_case_does_not_match() throws Exception {
    StringEnumMapper.ByNameEnumMapper<EnumA> mapper = map(EnumA.class).byName();

    for (EnumA enumA : EnumA.values()) {
      assertThat(mapper.toEnum(enumA.name().toLowerCase())).isNull();
    }
    assertThat(mapper.toEnum(DUMMY_STRING_VALUE)).isNull();
  }

  /*********************
   * byName ignoreCase *
   *********************/
  @Test
  public void map_byName_ignoreCase_toString_returns_null_for_null_value() throws Exception {
    assertThat(map(EnumA.class).byName().ignoreCase().toString(null)).isNull();
  }

  @Test
  public void map_byName_ignoreCase_toString_returns_name_value() throws Exception {
    StringEnumMapper.ByNameEnumMapper<EnumA> mapper = map(EnumA.class).byName().ignoreCase();

    for (EnumA enumA : EnumA.values()) {
      assertThat(mapper.toString(enumA)).isEqualTo(enumA.name());
    }
  }
  @Test
  public void map_byName_ignoreCase_toEnum_returns_null_for_null_value() throws Exception {
    assertThat(map(EnumA.class).byName().ignoreCase().toEnum(null)).isNull();
  }

  @Test
  public void map_byName_ignoreCase_toEnum_returns_name_when_case_does_not_match() throws Exception {
    StringEnumMapper.ByNameEnumMapper<EnumA> mapper = map(EnumA.class).byName().ignoreCase();

    for (EnumA enumA : EnumA.values()) {
      assertThat(mapper.toEnum(enumA.name().toLowerCase())).isEqualTo(enumA);
    }
  }

  @Test
  public void map_byName_ignoreCase_returns_itself_as_ignoreCase_returned_value() throws Exception {
    StringEnumMapper.ByNameEnumMapper<EnumA> mapper = map(EnumA.class).byName().ignoreCase();

    assertThat(mapper.ignoreCase()).isSameAs(mapper);
  }

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


  /********
   * custom
   ********/
  @Test
  public void map_byCustom_toString_returns_null_for_null_value() throws Exception {
    assertThat(map(EnumA.class).by(CustomEnumAToString.INSTANCE).toString(null)).isNull();
  }

  @Test
  public void map_byCustom_toString_returns_name_value() throws Exception {
    StringEnumMapper.CustomStringEnumMapper<EnumA> mapper = map(EnumA.class).by(CustomEnumAToString.INSTANCE);

    for (EnumA enumA : EnumA.values()) {
      assertThat(mapper.toString(enumA)).isEqualTo(CustomEnumAToString.INSTANCE.transform(enumA));
    }
  }

  @Test
  public void map_byCustom_toEnum_returns_null_for_null_value() throws Exception {
    assertThat(map(EnumA.class).by(CustomEnumAToString.INSTANCE).toEnum(null)).isNull();
  }

  @Test
  public void map_byCustom_toEnum_returns_value_for_exact_name() throws Exception {
    StringEnumMapper.CustomStringEnumMapper<EnumA> mapper = map(EnumA.class).by(CustomEnumAToString.INSTANCE);

    for (EnumA enumA : EnumA.values()) {
      assertThat(mapper.toEnum(CustomEnumAToString.INSTANCE.transform(enumA))).isEqualTo(enumA);
    }
  }

  @Test
  public void map_byCustom_toEnum_returns_null_for_null() throws Exception {
    assertThat(map(EnumA.class).by(CustomEnumAToString.INSTANCE).toEnum(null)).isNull();
  }

  @Test
  public void map_byCustom_toEnum_returns_null_for_empty_String() throws Exception {
    assertThat(map(EnumA.class).by(CustomEnumAToString.INSTANCE).toEnum("")).isNull();
  }

  @Test
  public void map_byCustom_toEnum_returns_null_for_case_does_not_match() throws Exception {
    StringEnumMapper.CustomStringEnumMapper<EnumA> mapper = map(EnumA.class).by(CustomEnumAToString.INSTANCE);

    for (EnumA enumA : EnumA.values()) {
      assertThat(mapper.toEnum(CustomEnumAToString.INSTANCE.transform(enumA).toUpperCase())).isNull();
    }
    assertThat(mapper.toEnum(DUMMY_STRING_VALUE)).isNull();
  }

  private static enum EnumA {
    VALUE_1, VALUE_2, VALUE_3;

    @Override
    public String toString() {
      return name().toLowerCase();
    }
  }

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
