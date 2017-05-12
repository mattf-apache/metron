package org.apache.metron.common.field.validation.primitive;

import org.apache.metron.common.field.validation.FieldValidation;
import org.apache.metron.stellar.dsl.Context;
import org.apache.metron.stellar.dsl.validators.primitive.DateValidation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;


public class DateFieldValidation extends DateValidation implements FieldValidation {

  private enum Config {
    FORMAT("format")
    ;
    String key;
    Config(String key) {
      this.key = key;
    }
    public <T> T get(Map<String, Object> config, Class<T> clazz) {
      Object o = config.get(key);
      if(o == null) {
        return null;
      }
      return clazz.cast(o);
    }
  }
  @Override
  public boolean isValid( Map<String, Object> input
          , Map<String, Object> validationConfig
          , Map<String, Object> globalConfig
          , Context context
  )
  {
    String format = Config.FORMAT.get(validationConfig, String.class);
    if(format == null) {
      return false;
    }
    SimpleDateFormat sdf = new SimpleDateFormat(format);
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

  @Override
  public void initialize(Map<String, Object> validationConfig, Map<String, Object> globalConfig) {
    String format = Config.FORMAT.get(validationConfig, String.class);
    if(format == null) {
      throw new IllegalStateException("You must specify '" + Config.FORMAT.key + "' in the config");
    }
    SimpleDateFormat sdf = new SimpleDateFormat(format);
    try {
      sdf.format(new Date());
    }
    catch(Exception e) {
      throw new IllegalStateException("Invalid date format: " + format, e);
    }
  }

}
