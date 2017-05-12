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

package org.apache.metron.stellar.common;

import org.apache.metron.stellar.common.evaluators.ArithmeticEvaluator;
import org.apache.metron.stellar.common.evaluators.ComparisonExpressionWithOperatorEvaluator;
import org.apache.metron.stellar.common.evaluators.NumberLiteralEvaluator;
import org.apache.metron.stellar.common.generated.StellarParser;
import org.apache.metron.stellar.dsl.Context;
import org.apache.metron.stellar.dsl.Token;
import org.apache.metron.stellar.dsl.VariableResolver;
import org.apache.metron.stellar.dsl.resolver.FunctionResolver;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayDeque;
import java.util.Deque;


@RunWith(PowerMockRunner.class)
@PrepareForTest({Deque.class, ArithmeticEvaluator.class, NumberLiteralEvaluator.class, ComparisonExpressionWithOperatorEvaluator.class})
public class StellarInterpreterTest {
  VariableResolver variableResolver;
  FunctionResolver functionResolver;
  Context context;
  Deque<Token<?>> tokenStack;
  ArithmeticEvaluator arithmeticEvaluator;
  NumberLiteralEvaluator numberLiteralEvaluator;
  ComparisonExpressionWithOperatorEvaluator comparisonExpressionWithOperatorEvaluator;
  StellarCompiler compiler;
  StellarCompiler.Expression expression;

  @SuppressWarnings("unchecked")
  @Before
  public void setUp() throws Exception {
    variableResolver = PowerMockito.mock(VariableResolver.class);
    functionResolver = PowerMockito.mock(FunctionResolver.class);
    context = PowerMockito.mock(Context.class);
    tokenStack = new ArrayDeque<>();
    arithmeticEvaluator = PowerMockito.mock(ArithmeticEvaluator.class);
    numberLiteralEvaluator = PowerMockito.mock(NumberLiteralEvaluator.class);
    comparisonExpressionWithOperatorEvaluator = PowerMockito.mock(ComparisonExpressionWithOperatorEvaluator.class);
    expression = new StellarCompiler.Expression(tokenStack);
    compiler = new StellarCompiler(expression, arithmeticEvaluator, numberLiteralEvaluator, comparisonExpressionWithOperatorEvaluator);
  }

  @Test
  public void exitIntLiteralShouldProperlyParseStringsAsIntegers() throws Exception {
    StellarParser.IntLiteralContext ctx = PowerMockito.mock(StellarParser.IntLiteralContext.class);
    Token result = PowerMockito.mock(Token.class);
    PowerMockito.when(ctx.getText()).thenReturn("1000");
    PowerMockito.when(numberLiteralEvaluator.evaluate(ctx)).thenReturn(result);
    compiler.exitIntLiteral(ctx);
    Mockito.verify(numberLiteralEvaluator).evaluate(ctx);
    Assert.assertEquals(1, tokenStack.size());
    Assert.assertEquals(tokenStack.getFirst(), result);
    PowerMockito.verifyZeroInteractions(variableResolver);
    PowerMockito.verifyZeroInteractions(functionResolver);
    PowerMockito.verifyZeroInteractions(context);
    PowerMockito.verifyZeroInteractions(arithmeticEvaluator);
    PowerMockito.verifyZeroInteractions(comparisonExpressionWithOperatorEvaluator);
  }

  @Test
  public void exitDoubleLiteralShouldProperlyParseStringsAsDoubles() throws Exception {
    StellarParser.DoubleLiteralContext ctx = PowerMockito.mock(StellarParser.DoubleLiteralContext.class);
    Token result = PowerMockito.mock(Token.class);
    PowerMockito.when(numberLiteralEvaluator.evaluate(ctx)).thenReturn(result);
    PowerMockito.when(ctx.getText()).thenReturn("1000D");

    compiler.exitDoubleLiteral(ctx);

    Mockito.verify(numberLiteralEvaluator).evaluate(ctx);
    Assert.assertEquals(1, tokenStack.size());
    Assert.assertEquals(tokenStack.getFirst(), result);
    PowerMockito.verifyZeroInteractions(variableResolver);
    PowerMockito.verifyZeroInteractions(functionResolver);
    PowerMockito.verifyZeroInteractions(context);
    PowerMockito.verifyZeroInteractions(arithmeticEvaluator);
    PowerMockito.verifyZeroInteractions(comparisonExpressionWithOperatorEvaluator);
  }

  @Test
  public void exitFloatLiteralShouldProperlyParseStringsAsFloats() throws Exception {
    StellarParser.FloatLiteralContext ctx = PowerMockito.mock(StellarParser.FloatLiteralContext.class);
    PowerMockito.when(ctx.getText()).thenReturn("1000f");
    Token result = PowerMockito.mock(Token.class);
    PowerMockito.when(numberLiteralEvaluator.evaluate(ctx)).thenReturn(result);

    compiler.exitFloatLiteral(ctx);

    Mockito.verify(numberLiteralEvaluator).evaluate(ctx);
    Assert.assertEquals(1, tokenStack.size());
    Assert.assertEquals(tokenStack.getFirst(), result);
    PowerMockito.verifyZeroInteractions(variableResolver);
    PowerMockito.verifyZeroInteractions(functionResolver);
    PowerMockito.verifyZeroInteractions(context);
    PowerMockito.verifyZeroInteractions(arithmeticEvaluator);
    PowerMockito.verifyZeroInteractions(comparisonExpressionWithOperatorEvaluator);
  }

  @Test
  public void exitLongLiteralShouldProperlyParseStringsAsLongs() throws Exception {
    StellarParser.LongLiteralContext ctx = PowerMockito.mock(StellarParser.LongLiteralContext.class);
    PowerMockito.when(ctx.getText()).thenReturn("1000l");
    Token result = PowerMockito.mock(Token.class);
    PowerMockito.when(numberLiteralEvaluator.evaluate(ctx)).thenReturn(result);

    compiler.exitLongLiteral(ctx);

    Mockito.verify(numberLiteralEvaluator).evaluate(ctx);
    Assert.assertEquals(1, tokenStack.size());
    Assert.assertEquals(tokenStack.getFirst(), result);
    PowerMockito.verifyZeroInteractions(variableResolver);
    PowerMockito.verifyZeroInteractions(functionResolver);
    PowerMockito.verifyZeroInteractions(context);
    PowerMockito.verifyZeroInteractions(arithmeticEvaluator);
    PowerMockito.verifyZeroInteractions(comparisonExpressionWithOperatorEvaluator);
  }

  @Test
  public void properlyCompareTwoNumbers() throws Exception {
    StellarParser.ComparisonExpressionWithOperatorContext ctx = PowerMockito.mock(StellarParser.ComparisonExpressionWithOperatorContext.class);
    StellarParser.ComparisonOpContext mockOp = PowerMockito.mock(StellarParser.ComparisonOpContext.class);
    PowerMockito.when(ctx.comp_operator()).thenReturn(mockOp);
    Token result = PowerMockito.mock(Token.class);
    PowerMockito.when(comparisonExpressionWithOperatorEvaluator.evaluate(Matchers.any(Token.class), Matchers.any(Token.class), Matchers.any(StellarParser.ComparisonOpContext.class))).thenReturn(result);

    compiler.exitComparisonExpressionWithOperator(ctx);
    Assert.assertEquals(1, tokenStack.size());
    StellarCompiler.DeferredFunction func = (StellarCompiler.DeferredFunction) tokenStack.pop().getValue();
    tokenStack.push(new Token<>(1000, Integer.class));
    tokenStack.push(new Token<>(1500f, Float.class));
    func.apply(tokenStack, new StellarCompiler.ExpressionState(context, functionResolver, variableResolver));
    Assert.assertEquals(1, tokenStack.size());
    Assert.assertEquals(tokenStack.getFirst(), result);
    Mockito.verify(comparisonExpressionWithOperatorEvaluator).evaluate(Matchers.any(Token.class), Matchers.any(Token.class), Matchers.eq(mockOp));
    PowerMockito.verifyZeroInteractions(numberLiteralEvaluator);
    PowerMockito.verifyZeroInteractions(variableResolver);
    PowerMockito.verifyZeroInteractions(functionResolver);
    PowerMockito.verifyZeroInteractions(context);
    PowerMockito.verifyZeroInteractions(arithmeticEvaluator);
  }
}
