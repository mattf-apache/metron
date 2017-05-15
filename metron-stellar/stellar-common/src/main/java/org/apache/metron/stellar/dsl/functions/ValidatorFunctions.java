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

import org.apache.metron.stellar.dsl.Stellar;
import org.apache.metron.stellar.dsl.StellarFunctionFromListPredicate;
import org.apache.metron.stellar.dsl.validators.network.*;
import org.apache.metron.stellar.dsl.validators.primitive.*;
/**
 * These functions expose the independent validators from
 * org.apache.commons.validator.routines.*
 * for the primitives Integer and Date, and the network-related types
 * Domain, IP address, Email address, and URL.
 */
public class ValidatorFunctions {

  @Stellar(name="IS_DATE"
          ,description = "Determines if the date contained in the string conforms to the specified format."
          ,params = {
          "date - The date in string form"
          , "format - The format of the date"
  }
          ,returns = "True if the date is in the specified format and false if otherwise."
  )
  public static class IS_DATE extends StellarFunctionFromListPredicate {

    public IS_DATE() {
      super(new DateValidation());
    }
  }

  @Stellar(name="IS_INTEGER"
          , description = "Determines whether or not an object is an integer."
          , params = {
          "x - The object to test"
  }
          , returns = "True if the object can be converted to an integer and false if otherwise."
  )
  public static class IS_INTEGER extends StellarFunctionFromListPredicate {

    public IS_INTEGER() {
      super(new IntegerValidation());
    }
  }

  @Stellar(name="IS_DOMAIN"
          ,description = "Tests if a string refers to a valid domain name.  Domain names are evaluated according" +
          " to the standards RFC1034 section 3, and RFC1123 section 2.1."
          ,params = {
          "address - The string to test"
  }
          , returns = "True if the string refers to a valid domain name and false if otherwise")
  public static class IS_DOMAIN extends StellarFunctionFromListPredicate {

    public IS_DOMAIN() {
      super(new DomainValidation());
    }
  }

  @Stellar(name="IS_EMAIL"
          ,description = "Tests if a string is a valid email address"
          ,params = {
          "address - The string to test"
  }
          , returns = "True if the string is a valid email address and false if otherwise.")
  public static class IS_EMAIL extends StellarFunctionFromListPredicate {

    public IS_EMAIL() {
      super(new EmailValidation());
    }
  }

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

  @Stellar(name="IS_URL"
          ,description = "Tests if a string is a valid URL"
          ,params = {
          "url - The string to test"
  }
          , returns = "True if the string is a valid URL and false if otherwise."
  )
  public static class IS_URL extends StellarFunctionFromListPredicate {

    public IS_URL() {
      super(new URLValidation());
    }
  }

}
