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

package org.apache.metron.stellar.dsl.validators.network;

import org.apache.commons.validator.routines.InetAddressValidator;
import org.apache.metron.stellar.dsl.validators.SimpleValidation;
import org.apache.metron.stellar.dsl.Stellar;
import org.apache.metron.stellar.dsl.StellarFunctionFromListPredicate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class IPValidation extends SimpleValidation<InetAddressValidator> {

  @Stellar(name="IS_IP"
          , description = "Determine if an string is an IP or not. Excess arguments after the first two are ignored."
          , params = {
              "ip - An object which we wish to test is an ip"
             ,"type (optional) - Object of string or collection type (e.g. list) one of IPV4 or IPV6 or both.  The default is IPV4."
                     }
          , returns = "True if the string is an IP and false otherwise.")
  public static class IS_IP extends StellarFunctionFromListPredicate {

    public IS_IP() {
      super(new IPValidation());
    }
  }

  protected enum IPType {
     IPV4(ip -> InetAddressValidator.getInstance().isValidInet4Address(ip))
    ,IPV6(ip -> InetAddressValidator.getInstance().isValidInet6Address(ip))
    , DEFAULT(ip -> InetAddressValidator.getInstance().isValid(ip));
    Predicate<String> validationPredicate;
    IPType(Predicate<String> validationPredicate) {
      this.validationPredicate = validationPredicate;
    }
    public boolean isValid(String ip) {
      return validationPredicate.test(ip);
    }
    public static IPType get(String type) {
      if(type == null) {
        return DEFAULT;
      }
      else {
        try {
          return IPType.valueOf(type);
        }
        catch(Exception e) {
          return DEFAULT;
        }
      }
    }
  }

  /**
   * Evaluates this predicate on the given argument.
   *
   * @param strings the input argument
   * @return {@code true} if the input argument matches the predicate,
   * otherwise {@code false}
   */
  @Override
  public boolean test(List<Object> strings) {
    IPType type = IPType.DEFAULT;
    if(strings.isEmpty()) {
      return false;
    }
    Object ip =  strings.get(0);
    if(ip == null) {
      return false;
    }
    if(strings.size() >= 2) {
      Object ipType = strings.get(1);
      if(ipType != null )
      {
        try {
          type = IPType.get(ipType.toString());
        } catch (Exception e) {
          type = IPType.DEFAULT;
        }
      }
    }
    return type.isValid(ip.toString());
  }

  public boolean test( Map<String, Object> input) {
    IPType type = IPType.DEFAULT;
    for (Object o : input.values()) {
      if(o == null || type.isValid(o.toString())) {
        return true;
      }
    }
    return false;
  }

  private Predicate<Object> pred = ip -> IPType.DEFAULT.isValid(ip.toString());

  private BiPredicate<Object, String> pred2 = (ip, type) -> test(Arrays.asList(ip, type));

  public BiPredicate<Object, String> getPredicate2() {return pred2;}

  public InetAddressValidator getValidatorInstance() {
    return InetAddressValidator.getInstance();
  }
}
