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

package org.apache.metron.stellar.dsl.validators;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * This abstract superclass exposes the independent validators from
 * org.apache.commons.validator.routines.*
 * There is no common superclass for those validators, which limits the usefulness
 * of the VALIDATOR type parameter.  Basically, you need to understand what
 * you're getting before you ask for it.
 *
 * We declare that it implements {@code Predicate<List<Object>>} to support easy production
 * of Stellar functions, but we also support {@code Predicate<Map<String, Object>>}.
 * In the "simple" case, both are tested by AND-ing the underlying predicate test of all their values.
 */
public abstract class SimpleValidation<VALIDATOR> implements Predicate<List<Object>> {

  private Predicate<Object> pred;

  @Override
  public boolean test(List<Object> input) {
    Predicate<Object> predicate = getPredicate();
    for(Object o : input) {
      if(o == null || !predicate.test(o)){
        return false;
      }
    }
    return true;
  }

  public boolean test( Map<String, Object> input) {
    Predicate<Object> predicate = getPredicate();
    if(isNonExistentOk()) {
      for (Object o : input.values()) {
        if (o != null && !predicate.test(o.toString())) {
          return false;
        }
      }
    }
    else {
      for (Object o : input.values()) {
        if (o == null || !predicate.test(o.toString())) {
          return false;
        }
      }
    }
    return true;
  }

  protected boolean isNonExistentOk() {
    return true;
  }

  public Predicate<Object> getPredicate() {return pred;}

  public abstract VALIDATOR getValidatorInstance();

}
