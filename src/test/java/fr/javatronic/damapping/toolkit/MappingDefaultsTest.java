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
package fr.javatronic.damapping.toolkit;

import org.testng.annotations.Test;

import static fr.javatronic.damapping.toolkit.MappingDefaults.defaultTo;
import static fr.javatronic.damapping.toolkit.MappingDefaults.defaultToNull;
import static org.assertj.core.api.Assertions.assertThat;

public class MappingDefaultsTest {

  private static final String GENERIC_DEFAULT = "default";
  private static final String NULL_DEFAULT = "Null input";
  private static final String EMPTY_DEFAULT = "Empty input";
  private static final String UNKNOWN_DEFAULT = "Unknown input";

  @Test
  public void defaultToNull_all_when_methods_return_null() throws Exception {
    MappingDefaults<Object> mappingDefaults = defaultToNull();
    assertThat(mappingDefaults.whenNull()).isNull();
    assertThat(mappingDefaults.whenEmpty()).isNull();
    assertThat(mappingDefaults.whenUnknown()).isNull();
  }

  @Test
  public void defaultTo_all_when_methods_return_the_same_value() throws Exception {
    MappingDefaults<String> mappingDefaults = defaultTo(NULL_DEFAULT);
    assertThat(mappingDefaults.whenNull()).isEqualTo(NULL_DEFAULT);
    assertThat(mappingDefaults.whenEmpty()).isEqualTo(NULL_DEFAULT);
    assertThat(mappingDefaults.whenUnknown()).isEqualTo(NULL_DEFAULT);
  }

  @Test
  public void defaultToNull_withNullDefault_set_value_returned_by_whenNull() throws Exception {
    assertThat(defaultToNull().withNullDefault(NULL_DEFAULT).whenNull()).isEqualTo(NULL_DEFAULT);
  }

  @Test
  public void defaultToNull_withEmptyDefault_set_value_returned_by_whenEmpty() throws Exception {
    assertThat(defaultToNull().withEmptyDefault(EMPTY_DEFAULT).whenEmpty()).isEqualTo(EMPTY_DEFAULT);
  }

  @Test
  public void defaultToNull_withUnknownDefault_set_value_returned_by_whenUnknown() throws Exception {
    assertThat(defaultToNull().withUnknownDefault(UNKNOWN_DEFAULT).whenUnknown()).isEqualTo(UNKNOWN_DEFAULT);
  }

  @Test
  public void defaultTo_withNullDefault_set_value_returned_by_whenNull() throws Exception {
    assertThat(defaultTo(GENERIC_DEFAULT).withNullDefault(NULL_DEFAULT).whenNull()).isEqualTo(NULL_DEFAULT);
  }

  @Test
  public void defaultTo_withEmptyDefault_set_value_returned_by_whenEmpty() throws Exception {
    assertThat(defaultTo(GENERIC_DEFAULT).withEmptyDefault(EMPTY_DEFAULT).whenEmpty()).isEqualTo(EMPTY_DEFAULT);
  }

  @Test
  public void defaultTo_withUnknownDefault_set_value_returned_by_whenUnknown() throws Exception {
    assertThat(defaultTo(GENERIC_DEFAULT).withUnknownDefault(UNKNOWN_DEFAULT).whenUnknown()).isEqualTo(UNKNOWN_DEFAULT);
  }

  @Test
  public void defaultToNull_returns_a_singleton() throws Exception {
    assertThat(defaultToNull()).isSameAs(defaultToNull());
  }

  @Test
  public void same_defaultValues_are_equals() throws Exception {
    MappingDefaults<String> mappingDefaults1 = defaultTo(GENERIC_DEFAULT);
    MappingDefaults<String> mappingDefaults2 = defaultTo(GENERIC_DEFAULT);

    assertThat(mappingDefaults1).isNotSameAs(mappingDefaults2);
    assertThat(mappingDefaults2).isEqualTo(mappingDefaults2);
  }
}