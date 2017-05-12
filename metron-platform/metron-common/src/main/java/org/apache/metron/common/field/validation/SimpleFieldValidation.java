package org.apache.metron.common.field.validation;

import org.apache.metron.stellar.dsl.Context;

import java.util.Map;

/**
 *
 */
public interface SimpleFieldValidation extends FieldValidation {
  @Override
  default public boolean isValid( Map<String, Object> input
          , Map<String, Object> validationConfig
          , Map<String, Object> globalConfig
          , Context context
  )
  {
    return test(input);
  }

  public boolean test( Map<String, Object> input);

  @Override
  default public void initialize(Map<String, Object> validationConfig, Map<String, Object> globalConfig) {

  }

}
