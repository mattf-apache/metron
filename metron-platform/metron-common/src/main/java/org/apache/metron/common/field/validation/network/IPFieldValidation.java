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

package org.apache.metron.common.field.validation.network;

import org.apache.metron.common.field.validation.FieldValidation;
import org.apache.metron.stellar.dsl.Context;
import org.apache.metron.stellar.dsl.validators.network.IPValidation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class IPFieldValidation extends IPValidation implements FieldValidation {

  private enum Config {
    TYPE("type")
    ;
    String key;
    Config(String key) {
      this.key = key;
    }
    public List get(Map<String, Object> config ) {
      Object o = config.get(key);
      if(o == null) {
        return Collections.singletonList("DEFAULT");
      }
      if( o instanceof ArrayList){
        return (ArrayList)o;
      }
      return Collections.singletonList(o);
    }
  }

  @Override
  public boolean isValid( Map<String, Object> input
          , Map<String, Object> validationConfig
          , Map<String, Object> globalConfig
          , Context context
  ) {
    List types = Config.TYPE.get(validationConfig);

    for(Object typeObject : types) {
      IPType type = IPType.get(typeObject.toString());
      for (Object o : input.values()) {
        if(o == null || type.isValid(o.toString())) {
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public void initialize(Map<String, Object> validationConfig, Map<String, Object> globalConfig) {
  }

}
