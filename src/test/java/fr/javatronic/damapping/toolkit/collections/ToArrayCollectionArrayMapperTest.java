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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.collections.Sets;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * ToArrayCollectionArrayMapperTest -
 *
 * @author Sébastien Lesaint
 */
public class ToArrayCollectionArrayMapperTest {

  private static final List<String> SOME_LIST = Arrays.asList("A", "B", "C");

  @Test
  public void toArray_from_collection_returns_empty_array_from_null_collection() throws Exception {
    assertThat(CollectionArrayMapper.toArray(null)).isEmpty();
  }

  @Test(dataProvider = "empty_collections")
  public <T> void toArray_from_collection_returns_empty_array_from_empty_collection(Collection<T> collection) throws Exception {
    assertThat(CollectionArrayMapper.toArray(collection)).isEmpty();
  }

  @DataProvider
  public <T> Object[][] empty_collections() {
    return new Object[][] {
        {new ArrayList<T>()},
        {new LinkedList<T>()},
        {Sets.<T>newHashSet() },
        {new LinkedHashSet<T>() },
    };
  }

  @SuppressWarnings("unchecked")
  @Test(dataProvider = "non_empty_collections")
  public <T> void toArray_from_collection_returns_array_from_Collection_toArray_method(Collection<T> collection) throws Exception {
    assertThat(CollectionArrayMapper.toArray(collection)).containsExactly((T[]) collection.toArray());
  }

  @DataProvider
  public Object[][] non_empty_collections() {
    return new Object[][] {
        {SOME_LIST},
        {new LinkedList<>(SOME_LIST)},
        {Sets.newHashSet(SOME_LIST) },
        {new LinkedHashSet<>(SOME_LIST) },
    };
  }

  @Test(expectedExceptions = NullPointerException.class)
  public void toArray_with_default_from_collection_throws_NPE_for_null_defaultValue() throws Exception {
    CollectionArrayMapper.toArray(SOME_LIST, null);
  }

  @Test
  public void toArray_with_default_returns_default_from_null_collection() throws Exception {
    String[] defaultValue = new String[] { "default" };

    assertThat(CollectionArrayMapper.toArray(null, defaultValue)).isSameAs(defaultValue);
  }

  @Test(dataProvider = "empty_collections")
  public void toArray_with_default_returns_default_from_empty_collections(Collection<String> emptyCollection) throws Exception {
    String[] defaultValue = new String[] { "default" };

    assertThat(CollectionArrayMapper.toArray(emptyCollection, defaultValue)).isSameAs(defaultValue);
  }

}
