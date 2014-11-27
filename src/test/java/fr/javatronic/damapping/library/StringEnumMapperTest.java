package fr.javatronic.damapping.library;

import javax.annotation.Nonnull;

import org.testng.annotations.Test;

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
    assertThat(StringEnumMapper.mapByName(EnumA.class).toString(null)).isNull();
  }

  @Test
  public void map_byName_toString_returns_name_value() throws Exception {
    StringEnumMapper.ByNameEnumMapper<EnumA> mapper = StringEnumMapper.mapByName(EnumA.class);

    for (EnumA enumA : EnumA.values()) {
      assertThat(mapper.toString(enumA)).isEqualTo(enumA.name());
    }
  }

  @Test
  public void map_byName_from_String_returns_null_for_null_value() throws Exception {
    assertThat(StringEnumMapper.mapByName(EnumA.class).from(null)).isNull();
  }

  @Test
  public void map_byName_from_String_returns_value_for_exact_name() throws Exception {
    StringEnumMapper.ByNameEnumMapper<EnumA> mapper = StringEnumMapper.mapByName(EnumA.class);

    for (EnumA enumA : EnumA.values()) {
      assertThat(mapper.from(enumA.name())).isEqualTo(enumA);
    }
  }

  @Test
  public void map_byName_from_String_returns_null_for_null() throws Exception {
    assertThat(StringEnumMapper.mapByName(EnumA.class).from(null)).isNull();
  }

  @Test
  public void map_byName_from_String_returns_null_for_empty_String() throws Exception {
    assertThat(StringEnumMapper.mapByName(EnumA.class).from("")).isNull();
  }

  @Test
  public void map_byName_from_String_returns_null_for_case_does_not_match() throws Exception {
    StringEnumMapper.ByNameEnumMapper<EnumA> mapper = StringEnumMapper.mapByName(EnumA.class);

    for (EnumA enumA : EnumA.values()) {
      assertThat(mapper.from(enumA.name().toLowerCase())).isNull();
    }
    assertThat(mapper.from(DUMMY_STRING_VALUE)).isNull();
  }

  /*********************
   * byName ignoreCase *
   *********************/
  @Test
  public void map_byNam_ignoreCase_toString_returns_null_for_null_value() throws Exception {
    assertThat(StringEnumMapper.mapByName(EnumA.class).ignoreCase().toString(null)).isNull();
  }

  @Test
  public void map_byName_ignoreCase_toString_returns_name_value() throws Exception {
    StringEnumMapper.ByNameEnumMapper<EnumA> mapper = StringEnumMapper.mapByName(EnumA.class).ignoreCase();

    for (EnumA enumA : EnumA.values()) {
      assertThat(mapper.toString(enumA)).isEqualTo(enumA.name());
    }
  }
  @Test
  public void map_byName_ignoreCase_from_String_returns_null_for_null_value() throws Exception {
    assertThat(StringEnumMapper.mapByName(EnumA.class).ignoreCase().from(null)).isNull();
  }

  @Test
  public void map_byName_ignoreCase_from_String_returns_null_for_case_does_not_match() throws Exception {
    StringEnumMapper.ByNameEnumMapper<EnumA> mapper = StringEnumMapper.mapByName(EnumA.class).ignoreCase();

    for (EnumA enumA : EnumA.values()) {
      assertThat(mapper.from(enumA.name().toLowerCase())).isEqualTo(enumA);
    }
  }

  @Test
  public void map_byName_ignoreCase_returns_itself_as_ignoreCase_returned_value() throws Exception {
    StringEnumMapper.ByNameEnumMapper<EnumA> mapper = StringEnumMapper.mapByName(EnumA.class).ignoreCase();

    assertThat(mapper.ignoreCase()).isSameAs(mapper);
  }

  /********
   * byToString
   ********/
  @Test
  public void map_byToString_toString_returns_null_for_null_value() throws Exception {
    assertThat(StringEnumMapper.mapByToString(EnumA.class).toString(null)).isNull();
  }

  @Test
  public void map_byToString_fromString_returns_value_for_exact_String_equality() throws Exception {
    StringEnumMapper.ByToStringEnumMapper<EnumA> mapper = StringEnumMapper.mapByToString(EnumA.class);

    for (EnumA enumA : EnumA.values()) {
      assertThat(mapper.from(enumA.toString())).isEqualTo(enumA);
    }
  }

  @Test
  public void map_byToString_from_String_returns_null_for_null() throws Exception {
    assertThat(StringEnumMapper.mapByToString(EnumA.class).from(null)).isNull();
  }

  @Test
  public void map_byToString_from_String_returns_null_for_empty_String() throws Exception {
    assertThat(StringEnumMapper.mapByToString(EnumA.class).from("")).isNull();
  }

  @Test
  public void map_byToString_from_String_returns_null_when_case_does_not_match() throws Exception {
    StringEnumMapper.ByToStringEnumMapper<EnumA> mapper = StringEnumMapper.mapByToString(EnumA.class);

    for (EnumA enumA : EnumA.values()) {
      assertThat(mapper.from(enumA.toString().toUpperCase())).isNull();
    }
    assertThat(mapper.from(DUMMY_STRING_VALUE)).isNull();
  }


  /********
   * custom
   ********/
  @Test
  public void map_byCustom_toString_returns_null_for_null_value() throws Exception {
    assertThat(StringEnumMapper.map(EnumA.class).by(CustomEnumAToString.INSTANCE).toString(null)).isNull();
  }

  @Test
  public void map_byCustom_toString_returns_name_value() throws Exception {
    StringEnumMapper.CustomStringEnumMapper<EnumA> mapper = StringEnumMapper.map(EnumA.class).by(
        CustomEnumAToString.INSTANCE
    );

    for (EnumA enumA : EnumA.values()) {
      assertThat(mapper.toString(enumA)).isEqualTo(CustomEnumAToString.INSTANCE.transform(enumA));
    }
  }

  @Test
  public void map_byCustom_from_String_returns_null_for_null_value() throws Exception {
    assertThat(StringEnumMapper.map(EnumA.class).by(CustomEnumAToString.INSTANCE).from(null)).isNull();
  }

  @Test
  public void map_byCustom_from_String_returns_value_for_exact_name() throws Exception {
    StringEnumMapper.CustomStringEnumMapper<EnumA> mapper = StringEnumMapper.map(EnumA.class).by(
        CustomEnumAToString.INSTANCE
    );

    for (EnumA enumA : EnumA.values()) {
      assertThat(mapper.from(CustomEnumAToString.INSTANCE.transform(enumA))).isEqualTo(enumA);
    }
  }

  @Test
  public void map_byCustom_from_String_returns_null_for_null() throws Exception {
    assertThat(StringEnumMapper.map(EnumA.class).by(CustomEnumAToString.INSTANCE).from(null)).isNull();
  }

  @Test
  public void map_byCustom_from_String_returns_null_for_empty_String() throws Exception {
    assertThat(StringEnumMapper.map(EnumA.class).by(CustomEnumAToString.INSTANCE).from("")).isNull();
  }

  @Test
  public void map_byCustom_from_String_returns_null_for_case_does_not_match() throws Exception {
    StringEnumMapper.CustomStringEnumMapper<EnumA> mapper = StringEnumMapper.map(EnumA.class).by(
        CustomEnumAToString.INSTANCE
    );

    for (EnumA enumA : EnumA.values()) {
      assertThat(mapper.from(CustomEnumAToString.INSTANCE.transform(enumA).toUpperCase())).isNull();
    }
    assertThat(mapper.from(DUMMY_STRING_VALUE)).isNull();
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
      }
      throw new NullPointerException("provided enum Value is not supposed to be null");
    }
  }
}
