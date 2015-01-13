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

public class ToArrayMapper_Object_Test {

  private static final Object SOME_OBJECT = new Object();
  private static final Object NULL_OBJECT = (Object) null;

  @Test
  public void toArray_from_object_returns_empty_array_from_null_object() throws Exception {
    assertThat(toArray(NULL_OBJECT)).isEmpty();
  }

  @Test
  public void toArray_from_object_returns_array_with_a_single_object() throws Exception {
    assertThat(toArray(SOME_OBJECT)).containsExactly(SOME_OBJECT);
  }

  @Test
  public void toArray_with_default_from_object_returns_null_when_object_is_null() throws Exception {
    assertThat(toArray(NULL_OBJECT, (String[]) null)).isNull();
  }

  @Test(expectedExceptions = NullPointerException.class)
  public void toArray_with_MappingDefaults_throws_NPE_if_MappingDefaults_is_null() throws Exception {
    toArray(SOME_OBJECT, (MappingDefaults<Object[]>) null);
  }

  @Test
  public void toArray_with_MappingDefaults_uses_whenNull_method_result() throws Exception {
    Object[] nullDefault = {"NULL_COLLECTION"};
    Object[] otherDefault = {"EMPTY_COLLECTION"};
    MappingDefaults<Object[]> mappingDefaults = MappingDefaults.defaultTo(nullDefault)
                                                               .withEmptyDefault(otherDefault)
                                                               .withUnknownDefault(otherDefault);

    assertThat(toArray(NULL_OBJECT, mappingDefaults)).isEqualTo(nullDefault);
  }

}