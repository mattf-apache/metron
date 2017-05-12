/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.metron.stellar.analytics.functions;

import org.apache.commons.lang.StringUtils;
import org.apache.metron.stellar.common.utils.ConversionUtils;
import org.apache.metron.stellar.dsl.BaseStellarFunction;
import org.apache.metron.stellar.dsl.Stellar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class ShannonFunctions {

  @Stellar( namespace="SHANNON"
          , name="ENTROPY"
          , description = "Computes the base-2 shannon entropy of a string"
          , params = { "input - String" }
          , returns = "The base-2 shannon entropy of the string (https://en.wikipedia.org/wiki/Entropy_(information_theory)#Definition).  The unit of this is bits."
  )
  public static class Entropy extends BaseStellarFunction {
    @Override
    public Object apply(List<Object> strings) {
      /*
      Shannon entropy is defined as follows:
      \Eta(X) = - \sum(p(x_i)*log_2(p(x_i)), i=0, n-1) where x_i are distinct characters in the string.
       */
      Map<Character, Integer> frequency = new HashMap<>();
      if(strings.size() != 1) {
        throw new IllegalArgumentException("STRING_ENTROPY expects exactly one argument which is a string.");
      }
      String input = ConversionUtils.convert(strings.get(0), String.class);
      if(StringUtils.isEmpty(input)) {
        return 0.0;
      }
      for(int i = 0;i < input.length();++i) {
        char c = input.charAt(i);
        frequency.put(c, frequency.getOrDefault(c, 0) + 1);
      }
      double ret = 0.0;
      double log2 = Math.log(2);
      for(Integer f : frequency.values()) {
        double p = f.doubleValue()/input.length();
        ret -= p * Math.log(p) / log2;
      }
      return ret;
    }
  }

}
