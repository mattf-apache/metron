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

package org.apache.metron.common.field.validation;

import org.apache.metron.common.field.validation.network.DomainFieldValidation;
import org.apache.metron.common.field.validation.network.EmailFieldValidation;
import org.apache.metron.common.field.validation.network.IPFieldValidation;
import org.apache.metron.common.field.validation.network.URLFieldValidation;
import org.apache.metron.common.field.validation.primitive.DateFieldValidation;
import org.apache.metron.common.field.validation.primitive.IntegerFieldValidation;
import org.apache.metron.common.field.validation.primitive.NotEmptyFieldValidation;
import org.apache.metron.common.field.validation.primitive.RegexFieldValidation;
import org.apache.metron.common.utils.ReflectionUtils;

public enum FieldValidations {
  STELLAR(new QueryValidation())
  ,IP(new IPFieldValidation())
  ,DOMAIN(new DomainFieldValidation())
  ,EMAIL(new EmailFieldValidation())
  ,URL(new URLFieldValidation())
  ,DATE(new DateFieldValidation())
  ,INTEGER(new IntegerFieldValidation())
  ,REGEX_MATCH(new RegexFieldValidation())
  ,NOT_EMPTY(new NotEmptyFieldValidation())
  ;
  private FieldValidation validation;
  FieldValidations(FieldValidation validation) {
    this.validation = validation;
  }
  public static FieldValidation get(String validation) {
    try {
      return FieldValidations.valueOf(validation).validation;
    }
    catch(Exception ex) {
      return ReflectionUtils.createInstance(validation);
    }
  }
}
