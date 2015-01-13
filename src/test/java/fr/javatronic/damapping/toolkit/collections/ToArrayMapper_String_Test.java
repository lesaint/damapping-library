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