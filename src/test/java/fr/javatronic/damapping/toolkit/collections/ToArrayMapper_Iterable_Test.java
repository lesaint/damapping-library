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

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.testng.annotations.Test;

import static fr.javatronic.damapping.toolkit.collections.ToArrayMapper.toArray;
import static org.assertj.core.api.Assertions.assertThat;


public class ToArrayMapper_Iterable_Test {

  private static final Iterable<String> NULL_ITERABLE = (Iterable<String>) null;
  private static final String[] NULL_STRING_ARRAY = (String[]) null;
  private static final List<String> STRING_ITERABLE_AS_LIST = Arrays.asList("A", "B", "C");
  private static final Iterable<String> STRING_ITERABLE = STRING_ITERABLE_AS_LIST;

  @Test
  public void toArray_from_iterable_returns_empty_array_from_null_iterable() throws Exception {
    assertThat(toArray((Iterable<?>) null)).isEmpty();
  }

  @Test
  public void toArray_from_iterable_returns_empty_array_from_iterable_with_null_iterator() throws Exception {
    assertThat(toArray(new NullIteratorIterable<Object>())).isEmpty();
  }

  @Test
  public void toArray_from_iterable_returns_empty_array_from_empty_iterable()
      throws Exception {
    assertThat(toArray(new EmptyIterable<String>())).isEmpty();
  }

  @Test
  @SuppressWarnings("unchecked")
  public <T> void toArray_from_iterable_returns_array_from_Collection_in_order()
      throws Exception {
    assertThat(toArray(STRING_ITERABLE)).containsExactly((String[]) STRING_ITERABLE_AS_LIST.toArray());
  }

  @Test
  public void toArray_with_default_from_iterable_returns_null_when_iterable_is_null_or_empty() throws Exception {
    assertThat(toArray(NULL_ITERABLE, NULL_STRING_ARRAY)).isNull();
    assertThat(toArray(new EmptyIterable<String>(), NULL_STRING_ARRAY)).isNull();
    assertThat(toArray(new NullIteratorIterable<String>(), NULL_STRING_ARRAY)).isNull();
  }

  @Test
  public void toArray_with_default_returns_default_from_null_iterable() throws Exception {
    String[] defaultValue = new String[]{"default"};

    assertThat(toArray((Iterable<String>) null, defaultValue)).isSameAs(defaultValue);
  }

  @Test
  public void toArray_with_default_returns_default_from_empty_iterable()
      throws Exception {
    String[] defaultValue = new String[]{"default"};

    assertThat(toArray(new EmptyIterable<String>(), defaultValue)).isSameAs(defaultValue);
  }

  @Test(expectedExceptions = NullPointerException.class)
  public void toArray_with_MappingDefaults_throws_NPE_if_MappingDefaults_is_null() throws Exception {
    toArray(STRING_ITERABLE, (MappingDefaults<String[]>) null);
  }

  @Test
  public void toArray_with_MappingDefaults_discriminate_null_or_empty_iterable() throws Exception {
    String[] nullDefault = {"NULL_COLLECTION"};
    String[] emptyDefault = {"EMPTY_COLLECTION"};
    MappingDefaults<String[]> mappingDefaults = MappingDefaults.defaultTo(nullDefault)
                                                               .withEmptyDefault(emptyDefault);

    assertThat(toArray(NULL_ITERABLE, mappingDefaults)).isEqualTo(nullDefault);
    assertThat(toArray(new NullIteratorIterable<String>(), mappingDefaults)).isEqualTo(nullDefault);
    assertThat(toArray(new EmptyIterable<String>(), mappingDefaults)).isEqualTo(emptyDefault);
  }

  /*=======*
   * utils *
   *=======*/
  private static class EmptyIterable<T> implements Iterable<T> {
    @Override
    public Iterator<T> iterator() {
      return new Iterator<T>() {
        @Override
        public boolean hasNext() {
          return false;
        }

        @Override
        public T next() {
          return null;
        }

        @Override
        public void remove() {

        }
      };
    }
  }

  private static class NullIteratorIterable<T> implements Iterable<T> {
    @Override
    public Iterator<T> iterator() {
      return null;
    }
  }
}