/*
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

package org.apache.metron.stellar.common.evaluators;

import org.apache.metron.stellar.dsl.ParseException;
import org.apache.metron.stellar.common.generated.StellarParser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

public class NumberLiteralEvaluatorTest {
  NumberEvaluator<StellarParser.IntLiteralContext> intLiteralContextNumberEvaluator;
  NumberEvaluator<StellarParser.DoubleLiteralContext> doubleLiteralContextNumberEvaluator;
  NumberEvaluator<StellarParser.FloatLiteralContext> floatLiteralContextNumberEvaluator;
  NumberEvaluator<StellarParser.LongLiteralContext> longLiteralContextNumberEvaluator;

  Map<Class<? extends StellarParser.Arithmetic_operandsContext>, NumberEvaluator> instanceMap;

  @Rule
  public final ExpectedException exception = ExpectedException.none();

  @Before
  public void setUp() throws Exception {
    intLiteralContextNumberEvaluator = Mockito.mock(IntLiteralEvaluator.class);
    doubleLiteralContextNumberEvaluator = Mockito.mock(DoubleLiteralEvaluator.class);
    floatLiteralContextNumberEvaluator = Mockito.mock(FloatLiteralEvaluator.class);
    longLiteralContextNumberEvaluator = Mockito.mock(LongLiteralEvaluator.class);
    instanceMap = new HashMap<Class<? extends StellarParser.Arithmetic_operandsContext>, NumberEvaluator>() {{
      put(Mockito.mock(StellarParser.IntLiteralContext.class).getClass(), intLiteralContextNumberEvaluator);
      put(Mockito.mock(StellarParser.DoubleLiteralContext.class).getClass(), doubleLiteralContextNumberEvaluator);
      put(Mockito.mock(StellarParser.FloatLiteralContext.class).getClass(), floatLiteralContextNumberEvaluator);
      put(Mockito.mock(StellarParser.LongLiteralContext.class).getClass(), longLiteralContextNumberEvaluator);
    }};
  }

  @Test
  public void verifyIntLiteralContextIsProperlyEvaluated() throws Exception {
    StellarParser.IntLiteralContext context = Mockito.mock(StellarParser.IntLiteralContext.class);
    NumberLiteralEvaluator.INSTANCE.evaluate(context, instanceMap);

    Mockito.verify(intLiteralContextNumberEvaluator).evaluate(context);
    Mockito.verifyZeroInteractions(doubleLiteralContextNumberEvaluator, floatLiteralContextNumberEvaluator, longLiteralContextNumberEvaluator);
  }

  @Test
  public void verifyDoubleLiteralContextIsProperlyEvaluated() throws Exception {
    StellarParser.DoubleLiteralContext context = Mockito.mock(StellarParser.DoubleLiteralContext.class);
    NumberLiteralEvaluator.INSTANCE.evaluate(context, instanceMap);

    Mockito.verify(doubleLiteralContextNumberEvaluator).evaluate(context);
    Mockito.verifyZeroInteractions(intLiteralContextNumberEvaluator, floatLiteralContextNumberEvaluator, longLiteralContextNumberEvaluator);
  }

  @Test
  public void verifyFloatLiteralContextIsProperlyEvaluated() throws Exception {
    StellarParser.FloatLiteralContext context = Mockito.mock(StellarParser.FloatLiteralContext.class);
    NumberLiteralEvaluator.INSTANCE.evaluate(context, instanceMap);

    Mockito.verify(floatLiteralContextNumberEvaluator).evaluate(context);
    Mockito.verifyZeroInteractions(doubleLiteralContextNumberEvaluator, intLiteralContextNumberEvaluator, longLiteralContextNumberEvaluator);
  }

  @Test
  public void verifyLongLiteralContextIsProperlyEvaluated() throws Exception {
    StellarParser.LongLiteralContext context = Mockito.mock(StellarParser.LongLiteralContext.class);
    NumberLiteralEvaluator.INSTANCE.evaluate(context, instanceMap);

    Mockito.verify(longLiteralContextNumberEvaluator).evaluate(context);
    Mockito.verifyZeroInteractions(doubleLiteralContextNumberEvaluator, floatLiteralContextNumberEvaluator, intLiteralContextNumberEvaluator);
  }

  @Test
  public void verifyExceptionThrownForUnsupportedContextType() throws Exception {
    StellarParser.VariableContext context = Mockito.mock(StellarParser.VariableContext.class);

    exception.expect(ParseException.class);
    exception.expectMessage("Does not support evaluation for type " + context.getClass());

    NumberLiteralEvaluator.INSTANCE.evaluate(context, instanceMap);

    Mockito.verifyZeroInteractions(longLiteralContextNumberEvaluator, doubleLiteralContextNumberEvaluator, floatLiteralContextNumberEvaluator, intLiteralContextNumberEvaluator);
  }
}
