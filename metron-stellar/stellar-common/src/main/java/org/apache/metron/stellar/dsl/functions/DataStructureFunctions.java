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
package org.apache.metron.stellar.dsl.functions;

import org.apache.metron.stellar.dsl.BaseStellarFunction;
import org.apache.metron.stellar.dsl.Stellar;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class DataStructureFunctions {

  @Stellar(name="IS_EMPTY"
          , description="Returns true if string or collection is empty or null and false if otherwise."
          , params = { "input - Object of string or collection type (for example, list)"}
          , returns = "True if the string or collection is empty or null and false if otherwise."
          )
  public static class IsEmpty extends BaseStellarFunction {

    @Override
    public Object apply(List<Object> list) {
      if(null == list || list.size() == 0) {
        return true;
      }
      Object o = list.get(0);
      if(o instanceof Collection) {
        return ((Collection)o).isEmpty();
      }
      else if(o instanceof String) {
        String val = (String) list.get(0);
        return val == null || val.isEmpty() ? true : false;
      }
      else {
        return o == null;
      }
    }
  }
  @Stellar(name="ADD"
          ,namespace="LIST"
          , description="Adds an element to a list."
          , params = { "list - List to add element to."
                     , "element - Element to add to list"
                     }
          , returns = "Resulting list with the item added at the end."
  )

  public static class ListAdd extends BaseStellarFunction {
    @Override
    public Object apply(List<Object> list) {
      if (list.size() == 0) {
        return null;
      }
      Object o = list.get(0);
      if(list.size() == 1) {
        return o;
      }
      if(o instanceof List) {
        List l = (List)o;
        Object arg = list.get(1);
        l.add(arg);
        return l;
      }
      else {
        return o;
      }
    }
  }

  @Stellar(name="LENGTH"
          , description="Returns the length of a string or size of a collection. Returns 0 for empty or null Strings"
          , params = { "input - Object of string or collection type (e.g. list)"}
          , returns = "Integer"
  )
  public static class Length extends BaseStellarFunction {
    @Override
    public Object apply(List<Object> list) {
      if(list.size() == 0) {
        return 0;
      }
      Object o = list.get(0);
      if(o instanceof Collection) {
        return ((Collection)o).size();
      }
      else if(o instanceof String) {
        String val = (String) list.get(0);
        return val == null || val.isEmpty() ? 0 : val.length();
      }
      else {
        return 0;
      }
    }
  }

  @Stellar(name="EXISTS"
          ,namespace="MAP"
          , description="Checks for existence of a key in a map."
          , params = {
          "key - The key to check for existence"
          ,"map - The map to check for existence of the key"
  }
          , returns = "True if the key is found in the map and false if otherwise."
  )
  public static class MapExists extends BaseStellarFunction {

    @Override
    public Object apply(List<Object> list) {
      if(list.size() < 2) {
        return false;
      }
      Object key = list.get(0);
      Object mapObj = list.get(1);
      if(key != null && mapObj != null && mapObj instanceof Map) {
        return ((Map)mapObj).containsKey(key);
      }
      return false;
    }
  }

  @Stellar(name="GET"
          ,namespace="MAP"
          , description="Gets the value associated with a key from a map"
          , params = {
          "key - The key"
          ,"map - The map"
          ,"default - Optionally the default value to return if the key is not in the map."
  }
          , returns = "The object associated with the key in the map.  If no value is associated with the key and default is specified, then default is returned. If no value is associated with the key or default, then null is returned."
  )
  public static class MapGet extends BaseStellarFunction {
    @Override
    public Object apply(List<Object> objects) {
      Object keyObj = objects.get(0);
      Object mapObj = objects.get(1);
      Object defaultObj = null;
      if(objects.size() >= 3) {
        defaultObj = objects.get(2);
      }
      if(keyObj == null || mapObj == null) {
        return defaultObj;
      }
      Map<Object, Object> map = (Map)mapObj;
      Object ret = map.get(keyObj);
      if(ret == null && defaultObj != null) {
        return defaultObj;
      }
      return ret;
    }
  }

}
