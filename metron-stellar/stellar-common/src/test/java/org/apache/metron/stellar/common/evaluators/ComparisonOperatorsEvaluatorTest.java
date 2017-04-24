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

import org.antlr.v4.runtime.tree.TerminalNode;
import org.apache.metron.stellar.common.generated.StellarParser;
import org.apache.metron.stellar.dsl.ParseException;
import org.apache.metron.stellar.dsl.Token;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import java.io.Serializable;

import static org.junit.Assert.*;

@SuppressWarnings("ALL")
public class ComparisonOperatorsEvaluatorTest {
  @Rule
  public final ExpectedException exception = ExpectedException.none();

  ComparisonExpressionEvaluator evaluator;

  @Before
  public void setUp() throws Exception {
    evaluator = new ComparisonOperatorsEvaluator();
  }

  @Test
  public void nonSupportedOperatorThrowsExceptionNonNumbericComparable() throws Exception {
    Token<String> left = Mockito.mock(Token.class);
    Mockito.when(left.getValue()).thenReturn("b");

    Token<String> right = Mockito.mock(Token.class);
    Mockito.when(right.getValue()).thenReturn("a");

    StellarParser.ComparisonOpContext op = Mockito.mock(StellarParser.ComparisonOpContext.class);

    exception.expect(ParseException.class);
    exception.expectMessage("Unsupported operator: " + op);

    evaluator.evaluate(left, right, op);
  }

  @Test
  public void nonSupportedOperatorThrowsExceptionNumbericComparison() throws Exception {
    Token<Long> left = Mockito.mock(Token.class);
    Mockito.when(left.getValue()).thenReturn(1L);

    Token<Long> right = Mockito.mock(Token.class);
    Mockito.when(right.getValue()).thenReturn(0L);

    StellarParser.ComparisonOpContext op = Mockito.mock(StellarParser.ComparisonOpContext.class);

    exception.expect(ParseException.class);
    exception.expectMessage("Unsupported operator: " + op);

    evaluator.evaluate(left, right, op);
  }

  @Test
  public void leftIsNullThenThrowException() throws Exception {
    Token<Long> left = Mockito.mock(Token.class);
    Token<Long> right = Mockito.mock(Token.class);
    Mockito.when(right.getValue()).thenReturn(1L);

    StellarParser.ComparisonOpContext op = Mockito.mock(StellarParser.ComparisonOpContext.class);
    Mockito.when(op.LT()).thenReturn(Mockito.mock(TerminalNode.class));

    assertFalse(evaluator.evaluate(left, right, op));
  }

  @Test
  public void rightIsNullThenReturnFalse() throws Exception {
    Token<Long> left = Mockito.mock(Token.class);
    Mockito.when(left.getValue()).thenReturn(1L);
    Token<Long> right = Mockito.mock(Token.class);

    StellarParser.ComparisonOpContext op = Mockito.mock(StellarParser.ComparisonOpContext.class);
    Mockito.when(op.LT()).thenReturn(Mockito.mock(TerminalNode.class));

    assertFalse(evaluator.evaluate(left, right, op));
  }

  @Test
  public void rightAndLeftIsNullThenReturnFalse() throws Exception {
    Token<Long> left = Mockito.mock(Token.class);
    Token<Long> right = Mockito.mock(Token.class);

    StellarParser.ComparisonOpContext op = Mockito.mock(StellarParser.ComparisonOpContext.class);
    Mockito.when(op.LT()).thenReturn(Mockito.mock(TerminalNode.class));

    assertFalse(evaluator.evaluate(left, right, op));
  }

  @Test
  public void throwParseExceptionWhenTryingToCompareNonComparable() throws Exception {
    exception.expect(ParseException.class);
    exception.expectMessage("Unsupported operations. The following expression is invalid: ");

    Token<Serializable> left = Mockito.mock(Token.class);
    Mockito.when(left.getValue()).thenReturn(Mockito.mock(Serializable.class));

    Token<Serializable> right = Mockito.mock(Token.class);
    Mockito.when(right.getValue()).thenReturn(Mockito.mock(Serializable.class));

    StellarParser.ComparisonOpContext op = Mockito.mock(StellarParser.ComparisonOpContext.class);
    Mockito.when(op.LT()).thenReturn(Mockito.mock(TerminalNode.class));

    evaluator.evaluate(left, right, op);
  }

  @Test
  public void makeSureAllOperatorsProperlyWorkForLongs() throws Exception {
    Token<Long> left = Mockito.mock(Token.class);
    Mockito.when(left.getValue()).thenReturn(0L);

    Token<Long> right = Mockito.mock(Token.class);
    Mockito.when(right.getValue()).thenReturn(1L);

    {
      StellarParser.ComparisonOpContext op = Mockito.mock(StellarParser.ComparisonOpContext.class);
      Mockito.when(op.LT()).thenReturn(Mockito.mock(TerminalNode.class));
      assertTrue(evaluator.evaluate(left, right, op));
    }
    {
      StellarParser.ComparisonOpContext op = Mockito.mock(StellarParser.ComparisonOpContext.class);
      Mockito.when(op.LTE()).thenReturn(Mockito.mock(TerminalNode.class));
      assertTrue(evaluator.evaluate(left, right, op));
    }
    {
      StellarParser.ComparisonOpContext op = Mockito.mock(StellarParser.ComparisonOpContext.class);
      Mockito.when(op.GT()).thenReturn(Mockito.mock(TerminalNode.class));
      assertFalse(evaluator.evaluate(left, right, op));
    }
    {
      StellarParser.ComparisonOpContext op = Mockito.mock(StellarParser.ComparisonOpContext.class);
      Mockito.when(op.GTE()).thenReturn(Mockito.mock(TerminalNode.class));
      assertFalse(evaluator.evaluate(left, right, op));
    }
  }

  @Test
  public void makeSureAllOperatorsProperlyWorkForDoubles() throws Exception {
    Token<Double> left = Mockito.mock(Token.class);
    Mockito.when(left.getValue()).thenReturn(0D);

    Token<Double> right = Mockito.mock(Token.class);
    Mockito.when(right.getValue()).thenReturn(1D);

    {
      StellarParser.ComparisonOpContext op = Mockito.mock(StellarParser.ComparisonOpContext.class);
      Mockito.when(op.LT()).thenReturn(Mockito.mock(TerminalNode.class));
      assertTrue(evaluator.evaluate(left, right, op));
    }
    {
      StellarParser.ComparisonOpContext op = Mockito.mock(StellarParser.ComparisonOpContext.class);
      Mockito.when(op.LTE()).thenReturn(Mockito.mock(TerminalNode.class));
      assertTrue(evaluator.evaluate(left, right, op));
    }
    {
      StellarParser.ComparisonOpContext op = Mockito.mock(StellarParser.ComparisonOpContext.class);
      Mockito.when(op.GT()).thenReturn(Mockito.mock(TerminalNode.class));
      assertFalse(evaluator.evaluate(left, right, op));
    }
    {
      StellarParser.ComparisonOpContext op = Mockito.mock(StellarParser.ComparisonOpContext.class);
      Mockito.when(op.GTE()).thenReturn(Mockito.mock(TerminalNode.class));
      assertFalse(evaluator.evaluate(left, right, op));
    }
  }

  @Test
  public void makeSureAllOperatorsProperlyWorkForFloats() throws Exception {
    Token<Float> left = Mockito.mock(Token.class);
    Mockito.when(left.getValue()).thenReturn(0F);

    Token<Float> right = Mockito.mock(Token.class);
    Mockito.when(right.getValue()).thenReturn(1F);

    {
      StellarParser.ComparisonOpContext op = Mockito.mock(StellarParser.ComparisonOpContext.class);
      Mockito.when(op.LT()).thenReturn(Mockito.mock(TerminalNode.class));
      assertTrue(evaluator.evaluate(left, right, op));
    }
    {
      StellarParser.ComparisonOpContext op = Mockito.mock(StellarParser.ComparisonOpContext.class);
      Mockito.when(op.LTE()).thenReturn(Mockito.mock(TerminalNode.class));
      assertTrue(evaluator.evaluate(left, right, op));
    }
    {
      StellarParser.ComparisonOpContext op = Mockito.mock(StellarParser.ComparisonOpContext.class);
      Mockito.when(op.GT()).thenReturn(Mockito.mock(TerminalNode.class));
      assertFalse(evaluator.evaluate(left, right, op));
    }
    {
      StellarParser.ComparisonOpContext op = Mockito.mock(StellarParser.ComparisonOpContext.class);
      Mockito.when(op.GTE()).thenReturn(Mockito.mock(TerminalNode.class));
      assertFalse(evaluator.evaluate(left, right, op));
    }
  }

  @Test
  public void makeSureAllOperatorsProperlyWorkForInts() throws Exception {
    Token<Integer> left = Mockito.mock(Token.class);
    Mockito.when(left.getValue()).thenReturn(0);

    Token<Integer> right = Mockito.mock(Token.class);
    Mockito.when(right.getValue()).thenReturn(1);

    {
      StellarParser.ComparisonOpContext op = Mockito.mock(StellarParser.ComparisonOpContext.class);
      Mockito.when(op.LT()).thenReturn(Mockito.mock(TerminalNode.class));
      assertTrue(evaluator.evaluate(left, right, op));
    }
    {
      StellarParser.ComparisonOpContext op = Mockito.mock(StellarParser.ComparisonOpContext.class);
      Mockito.when(op.LTE()).thenReturn(Mockito.mock(TerminalNode.class));
      assertTrue(evaluator.evaluate(left, right, op));
    }
    {
      StellarParser.ComparisonOpContext op = Mockito.mock(StellarParser.ComparisonOpContext.class);
      Mockito.when(op.GT()).thenReturn(Mockito.mock(TerminalNode.class));
      assertFalse(evaluator.evaluate(left, right, op));
    }
    {
      StellarParser.ComparisonOpContext op = Mockito.mock(StellarParser.ComparisonOpContext.class);
      Mockito.when(op.GTE()).thenReturn(Mockito.mock(TerminalNode.class));
      assertFalse(evaluator.evaluate(left, right, op));
    }
  }

  @Test
  public void makeSureAllOperatorsWorkForMixedTypesDoublesLong() throws Exception {
    Token<Long> left = Mockito.mock(Token.class);
    Mockito.when(left.getValue()).thenReturn(1L);

    Token<Double> right = Mockito.mock(Token.class);
    Mockito.when(right.getValue()).thenReturn(1.0000001D);

    {
      StellarParser.ComparisonOpContext op = Mockito.mock(StellarParser.ComparisonOpContext.class);
      Mockito.when(op.LT()).thenReturn(Mockito.mock(TerminalNode.class));
      assertTrue(evaluator.evaluate(left, right, op));
    }
    {
      StellarParser.ComparisonOpContext op = Mockito.mock(StellarParser.ComparisonOpContext.class);
      Mockito.when(op.LTE()).thenReturn(Mockito.mock(TerminalNode.class));
      assertTrue(evaluator.evaluate(left, right, op));
    }
    {
      StellarParser.ComparisonOpContext op = Mockito.mock(StellarParser.ComparisonOpContext.class);
      Mockito.when(op.GT()).thenReturn(Mockito.mock(TerminalNode.class));
      assertFalse(evaluator.evaluate(left, right, op));
    }
    {
      StellarParser.ComparisonOpContext op = Mockito.mock(StellarParser.ComparisonOpContext.class);
      Mockito.when(op.GTE()).thenReturn(Mockito.mock(TerminalNode.class));
      assertFalse(evaluator.evaluate(left, right, op));
    }
  }

  @Test
  public void makeSureAllOperatorsWorkForMixedTypesDoublesFloat() throws Exception {
    final double leftValue = 1.0000001D;
    final float rightValue = 1.0000001F;

    Token<Double> left = Mockito.mock(Token.class);
    Mockito.when(left.getValue()).thenReturn(leftValue);

    Token<Float> right = Mockito.mock(Token.class);
    Mockito.when(right.getValue()).thenReturn(rightValue);

    {
      StellarParser.ComparisonOpContext op = Mockito.mock(StellarParser.ComparisonOpContext.class);
      Mockito.when(op.LT()).thenReturn(Mockito.mock(TerminalNode.class));
      assertEquals(leftValue < rightValue, evaluator.evaluate(left, right, op));
    }
    {
      StellarParser.ComparisonOpContext op = Mockito.mock(StellarParser.ComparisonOpContext.class);
      Mockito.when(op.LTE()).thenReturn(Mockito.mock(TerminalNode.class));
      assertEquals(leftValue <= rightValue, evaluator.evaluate(left, right, op));
    }
    {
      StellarParser.ComparisonOpContext op = Mockito.mock(StellarParser.ComparisonOpContext.class);
      Mockito.when(op.GT()).thenReturn(Mockito.mock(TerminalNode.class));
      assertEquals(leftValue > rightValue, evaluator.evaluate(left, right, op));
    }
    {
      StellarParser.ComparisonOpContext op = Mockito.mock(StellarParser.ComparisonOpContext.class);
      Mockito.when(op.GTE()).thenReturn(Mockito.mock(TerminalNode.class));
      assertEquals(leftValue >= rightValue, evaluator.evaluate(left, right, op));
    }
  }

  @Test
  public void makeSureAllOperatorsWorkForMixedTypesFloatIntegers() throws Exception {
    final int leftValue = 1;
    final float rightValue = 1.0000001F;

    Token<Integer> left = Mockito.mock(Token.class);
    Mockito.when(left.getValue()).thenReturn(leftValue);

    Token<Float> right = Mockito.mock(Token.class);
    Mockito.when(right.getValue()).thenReturn(rightValue);

    {
      StellarParser.ComparisonOpContext op = Mockito.mock(StellarParser.ComparisonOpContext.class);
      Mockito.when(op.LT()).thenReturn(Mockito.mock(TerminalNode.class));
      assertEquals(leftValue < rightValue, evaluator.evaluate(left, right, op));
    }
    {
      StellarParser.ComparisonOpContext op = Mockito.mock(StellarParser.ComparisonOpContext.class);
      Mockito.when(op.LTE()).thenReturn(Mockito.mock(TerminalNode.class));
      assertEquals(leftValue <= rightValue, evaluator.evaluate(left, right, op));
    }
    {
      StellarParser.ComparisonOpContext op = Mockito.mock(StellarParser.ComparisonOpContext.class);
      Mockito.when(op.GT()).thenReturn(Mockito.mock(TerminalNode.class));
      assertEquals(leftValue > rightValue, evaluator.evaluate(left, right, op));
    }
    {
      StellarParser.ComparisonOpContext op = Mockito.mock(StellarParser.ComparisonOpContext.class);
      Mockito.when(op.GTE()).thenReturn(Mockito.mock(TerminalNode.class));
      assertEquals(leftValue >= rightValue, evaluator.evaluate(left, right, op));
    }
  }

  @Test
  public void makeSureAllOperatorsWorkForMixedTypesFloatIntegers2() throws Exception {
    final int leftValue = 1;
    final float rightValue = 1.00000001F;

    Token<Integer> left = Mockito.mock(Token.class);
    Mockito.when(left.getValue()).thenReturn(leftValue);

    Token<Float> right = Mockito.mock(Token.class);
    Mockito.when(right.getValue()).thenReturn(rightValue);

    {
      StellarParser.ComparisonOpContext op = Mockito.mock(StellarParser.ComparisonOpContext.class);
      Mockito.when(op.LT()).thenReturn(Mockito.mock(TerminalNode.class));
      assertEquals(leftValue < rightValue, evaluator.evaluate(left, right, op));
    }
    {
      StellarParser.ComparisonOpContext op = Mockito.mock(StellarParser.ComparisonOpContext.class);
      Mockito.when(op.LTE()).thenReturn(Mockito.mock(TerminalNode.class));
      assertEquals(leftValue <= rightValue, evaluator.evaluate(left, right, op));
    }
    {
      StellarParser.ComparisonOpContext op = Mockito.mock(StellarParser.ComparisonOpContext.class);
      Mockito.when(op.GT()).thenReturn(Mockito.mock(TerminalNode.class));
      assertEquals(leftValue > rightValue, evaluator.evaluate(left, right, op));
    }
    {
      StellarParser.ComparisonOpContext op = Mockito.mock(StellarParser.ComparisonOpContext.class);
      Mockito.when(op.GTE()).thenReturn(Mockito.mock(TerminalNode.class));
      assertEquals(leftValue >= rightValue, evaluator.evaluate(left, right, op));
    }
  }

  @Test
  public void makeSureAllOperatorsWorkForMixedTypesLongIntegers() throws Exception {
    final int leftValue = 1;
    final long rightValue = 3L;

    Token<Integer> left = Mockito.mock(Token.class);
    Mockito.when(left.getValue()).thenReturn(leftValue);

    Token<Long> right = Mockito.mock(Token.class);
    Mockito.when(right.getValue()).thenReturn(rightValue);

    {
      StellarParser.ComparisonOpContext op = Mockito.mock(StellarParser.ComparisonOpContext.class);
      Mockito.when(op.LT()).thenReturn(Mockito.mock(TerminalNode.class));
      assertEquals(leftValue < rightValue, evaluator.evaluate(left, right, op));
    }
    {
      StellarParser.ComparisonOpContext op = Mockito.mock(StellarParser.ComparisonOpContext.class);
      Mockito.when(op.LTE()).thenReturn(Mockito.mock(TerminalNode.class));
      assertEquals(leftValue <= rightValue, evaluator.evaluate(left, right, op));
    }
    {
      StellarParser.ComparisonOpContext op = Mockito.mock(StellarParser.ComparisonOpContext.class);
      Mockito.when(op.GT()).thenReturn(Mockito.mock(TerminalNode.class));
      assertEquals(leftValue > rightValue, evaluator.evaluate(left, right, op));
    }
    {
      StellarParser.ComparisonOpContext op = Mockito.mock(StellarParser.ComparisonOpContext.class);
      Mockito.when(op.GTE()).thenReturn(Mockito.mock(TerminalNode.class));
      assertEquals(leftValue >= rightValue, evaluator.evaluate(left, right, op));
    }
  }

  @Test
  public void makeSureAllOperatorsWorkForNonIntegerComparableTypes() throws Exception {
    final String leftValue = "a";
    final String rightValue = "b";

    Token<String> left = Mockito.mock(Token.class);
    Mockito.when(left.getValue()).thenReturn(leftValue);

    Token<String> right = Mockito.mock(Token.class);
    Mockito.when(right.getValue()).thenReturn(rightValue);

    {
      StellarParser.ComparisonOpContext op = Mockito.mock(StellarParser.ComparisonOpContext.class);
      Mockito.when(op.LT()).thenReturn(Mockito.mock(TerminalNode.class));
      assertEquals(leftValue.compareTo(rightValue) < 0, evaluator.evaluate(left, right, op));
    }
    {
      StellarParser.ComparisonOpContext op = Mockito.mock(StellarParser.ComparisonOpContext.class);
      Mockito.when(op.LTE()).thenReturn(Mockito.mock(TerminalNode.class));
      assertEquals(leftValue.compareTo(rightValue) <= 0, evaluator.evaluate(left, right, op));
    }
    {
      StellarParser.ComparisonOpContext op = Mockito.mock(StellarParser.ComparisonOpContext.class);
      Mockito.when(op.GT()).thenReturn(Mockito.mock(TerminalNode.class));
      assertEquals(leftValue.compareTo(rightValue) > 0, evaluator.evaluate(left, right, op));
    }
    {
      StellarParser.ComparisonOpContext op = Mockito.mock(StellarParser.ComparisonOpContext.class);
      Mockito.when(op.GTE()).thenReturn(Mockito.mock(TerminalNode.class));
      assertEquals(leftValue.compareTo(rightValue) >= 0, evaluator.evaluate(left, right, op));
    }
  }
}