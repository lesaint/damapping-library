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

import org.testng.annotations.Test;

import static fr.javatronic.damapping.toolkit.collections.ToArrayMapper.defaultToEmpty;
import static fr.javatronic.damapping.toolkit.collections.ToArrayMapper.defaultToNull;
import static org.assertj.core.api.Assertions.assertThat;

public class ToArrayMapper_defaults_Test {

  @Test
  public void defaultToNull_returns_MappingDefaults_with_null_defaults() throws Exception {
    assertThat(defaultToNull().whenNull()).isNull();
    assertThat(defaultToNull().whenEmpty()).isNull();
  }

  @Test
  public void defaultToEmpty_returns_MappingDefaults_with_empty_array_defaults() throws Exception {
    assertThat(defaultToEmpty().whenNull()).isEmpty();
    assertThat(defaultToEmpty().whenEmpty()).isEmpty();
  }
}