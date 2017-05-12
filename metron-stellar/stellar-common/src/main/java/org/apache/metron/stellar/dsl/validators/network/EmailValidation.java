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

import org.apache.commons.validator.routines.EmailValidator;
import org.apache.metron.stellar.dsl.validators.SimpleValidation;
import org.apache.metron.stellar.dsl.Stellar;
import org.apache.metron.stellar.dsl.StellarFunctionFromListPredicate;

import java.util.function.Predicate;

public class EmailValidation extends SimpleValidation<EmailValidator> {

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

  private Predicate<Object> pred = email -> EmailValidator.getInstance().isValid(email == null?null:email.toString());

  @Override
  public EmailValidator getValidatorInstance() {
    return EmailValidator.getInstance();
  }
}
