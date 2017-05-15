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

package org.apache.metron.stellar.dsl.validators.primitive;

import org.apache.metron.stellar.dsl.validators.SimpleValidation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class DateValidation extends SimpleValidation<SimpleDateFormat> {

  /**
   * Evaluates this predicate on the given argument.
   *
   * @param strings the input argument
   * @return {@code true} if the input argument matches the predicate,
   * otherwise {@code false}
   */
  @Override
  public boolean test(List<Object> strings) {
    if(strings.isEmpty()) {
      return false;
    }
    if(strings.size() >= 2) {
      Object date = strings.get(0);
      Object format = strings.get(1);
      if(date == null || format == null) {
        return false;
      }
      try {
        SimpleDateFormat sdf = new SimpleDateFormat(format.toString());
        sdf.parse(date.toString());
        return true;
      }
      catch(ParseException pe) {
        return false;
      }
    }
    else {
      return false;
    }
  }

  public boolean test( Map<String, Object> input)
  {
    SimpleDateFormat sdf = new SimpleDateFormat();
    for(Object o : input.values()) {
      if(o == null) {
        return true;
      }
      try {
        Date d = sdf.parse(o.toString());
      } catch (ParseException e) {
        return false;
      }
    }
    return true;
  }

  private Predicate<Object> pred = date -> {
    SimpleDateFormat sdf = getValidatorInstance();
    try{
      sdf.parse(date.toString()); return true;
    }
    catch(ParseException pe) {
      return false;
    }
  };

  private BiPredicate<Object, String> pred2 = (date, format) -> test(Arrays.asList(date, format));

  public BiPredicate<Object, String> getPredicate2() {return pred2;}

  /**
   * Warning: this "validator" does not have an {@code isValid} method.
   */
  public SimpleDateFormat getValidatorInstance() {
    return new SimpleDateFormat();
  }

}
