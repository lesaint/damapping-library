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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.collections.Sets;

import static fr.javatronic.damapping.toolkit.collections.ToArrayMapper.toArray;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * ToArrayMapper_Collection_Test -
 *
 * @author Sébastien Lesaint
 */
public class ToArrayMapper_Collection_Test {

  private static final Collection<String> NULL_STRING_COLLECTION = (Collection<String>) null;
  private static final List<String> STRING_LIST = Arrays.asList("A", "B", "C");
  private static final List<String> EMPTY_LIST = Collections.emptyList();
  private static final String[] NULL_STRING_ARRAY = (String[]) null;

  @Test
  public void toArray_from_collection_returns_empty_array_from_null_collection() throws Exception {
    assertThat(toArray((Collection<?>) null)).isEmpty();
  }

  @Test(dataProvider = "empty_collections")
  public <T> void toArray_from_collection_returns_empty_array_from_empty_collection(Collection<T> collection)
      throws Exception {
    assertThat(toArray(collection)).isEmpty();
  }

  @DataProvider
  public <T> Object[][] empty_collections() {
    return new Object[][]{
        {new ArrayList<T>()},
        {new LinkedList<T>()},
        {Sets.<T>newHashSet()},
        {new LinkedHashSet<T>()},
    };
  }

  @SuppressWarnings("unchecked")
  @Test(dataProvider = "non_empty_collections")
  public <T> void toArray_from_collection_returns_array_from_Collection_in_order(Collection<T> collection)
      throws Exception {
    assertThat(toArray(collection)).containsExactly((T[]) collection.toArray());
  }

  @DataProvider
  public Object[][] non_empty_collections() {
    return new Object[][]{
        {STRING_LIST},
        {new LinkedList<>(STRING_LIST)},
        {Sets.newHashSet(STRING_LIST)},
        {new LinkedHashSet<>(STRING_LIST)},
    };
  }

  @Test
  public void toArray_with_default_from_collection_returns_null_when_collection_is_null_or_empty() throws Exception {
    assertThat(toArray(NULL_STRING_COLLECTION, NULL_STRING_ARRAY)).isNull();
    assertThat(toArray(EMPTY_LIST, NULL_STRING_ARRAY)).isNull();
  }

  @Test
  public void toArray_with_default_returns_default_from_null_collection() throws Exception {
    String[] defaultValue = new String[]{"default"};

    assertThat(toArray(NULL_STRING_COLLECTION, defaultValue)).isSameAs(defaultValue);
  }

  @Test(dataProvider = "empty_collections")
  public void toArray_with_default_returns_default_from_empty_collections(Collection<String> emptyCollection)
      throws Exception {
    String[] defaultValue = new String[]{"default"};

    assertThat(toArray(emptyCollection, defaultValue)).isSameAs(defaultValue);
  }

  @Test(expectedExceptions = NullPointerException.class)
  public void toArray_with_MappingDefaults_throws_NPE_if_MappingDefaults_is_null() throws Exception {
    toArray(STRING_LIST, (MappingDefaults<String[]>) null);
  }

  @Test
  public void toArray_with_MappingDefaults_discriminate_null_or_empty_collection() throws Exception {
    String[] nullDefault = {"NULL_COLLECTION"};
    String[] emptyDefault = {"EMPTY_COLLECTION"};
    MappingDefaults<String[]> mappingDefaults = MappingDefaults.defaultTo(nullDefault)
                                                               .withEmptyDefault(emptyDefault);

    assertThat(toArray(NULL_STRING_COLLECTION, mappingDefaults)).isEqualTo(nullDefault);
    assertThat(toArray(EMPTY_LIST, mappingDefaults)).isEqualTo(emptyDefault);
  }
}
