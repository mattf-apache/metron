/*
 *
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package org.apache.metron.stellar.analytics.utils;

import org.apache.metron.stellar.common.utils.SerDeUtils;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests the Serializer with BloomFilters.
 */
public class BloomFilterSerDeUtilsTest {

  @Test
  public void testSerDeUtils() {
    final BloomFilter<Object> expected = new BloomFilter<>(new BloomFilter.DefaultSerializer<>(), 10000, 0.01);
    expected.add("foo");
    expected.add("bar");
    byte[] raw = SerDeUtils.toBytes(expected);
    BloomFilter<Object> actual = (BloomFilter) SerDeUtils.fromBytes(raw, Object.class);
    Assert.assertTrue(actual.mightContain("foo"));
    Assert.assertFalse(actual.mightContain("timothy"));
    assertEquals(expected, actual);
  }
}
