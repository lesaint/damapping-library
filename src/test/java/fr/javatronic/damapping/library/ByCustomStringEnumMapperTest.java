package fr.javatronic.damapping.library;

import javax.annotation.Nonnull;

import org.testng.annotations.Test;

import static fr.javatronic.damapping.library.StringEnumMapper.map;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * ByCustomStringEnumMapperTest - Unit tests for class {@link StringEnumMapper}.
 *
 * @author Sébastien Lesaint
 */
public class ByCustomStringEnumMapperTest {

  private static final String DUMMY_STRING_VALUE = "fooBarAcme";

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

  /************
   * utilities
   ***********/

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
