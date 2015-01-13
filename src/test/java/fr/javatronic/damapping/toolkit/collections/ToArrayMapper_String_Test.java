package fr.javatronic.damapping.toolkit.collections;

import fr.javatronic.damapping.toolkit.MappingDefaults;

import org.testng.annotations.Test;

import static fr.javatronic.damapping.toolkit.collections.ToArrayMapper.toArray;
import static org.assertj.core.api.Assertions.assertThat;

public class ToArrayMapper_String_Test {

  private static final String SOME_STRING = "some string";
  private static final String NULL_STRING = (String) null;
  private static final String EMPTY_STRING = "";

  @Test
  public void toArray_from_string_returns_empty_array_from_null_string() throws Exception {
    assertThat(toArray(NULL_STRING)).isEmpty();
  }

  @Test
  public void toArray_from_string_returns_empty_array_from_empty_string() throws Exception {
    assertThat(toArray(EMPTY_STRING)).isEmpty();
  }

  @Test
  public void toArray_from_string_returns_array_with_a_single_string() throws Exception {
    assertThat(toArray(SOME_STRING)).containsExactly(SOME_STRING);
  }

  @Test
  public void toArray_with_default_returns_null_when_string_is_null_or_empty() throws Exception {
    assertThat(toArray(NULL_STRING, (String[]) null)).isNull();
    assertThat(toArray(EMPTY_STRING, (String[]) null)).isNull();
  }

  /*=================*
   * MappingDefaults *
   *=================*/
  @Test(expectedExceptions = NullPointerException.class)
  public void toArray_with_MappingDefaults_throws_NPE_if_MappingDefaults_is_null() throws Exception {
    toArray(SOME_STRING, (MappingDefaults<String[]>) null);
  }

  @Test
  public void toArray_with_MappingDefaults_discriminate_null_or_empty_string() throws Exception {
    String[] nullDefault = {"NULL_COLLECTION"};
    String[] emptyDefault = {"EMPTY_COLLECTION"};
    MappingDefaults<String[]> mappingDefaults = MappingDefaults.defaultTo(nullDefault)
                                                               .withEmptyDefault(emptyDefault);

    assertThat(toArray(NULL_STRING, mappingDefaults)).isEqualTo(nullDefault);
    assertThat(toArray(EMPTY_STRING, mappingDefaults)).isEqualTo(emptyDefault);
  }

}