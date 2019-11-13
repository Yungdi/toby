package com.yun27jin.toby.test;

import com.yun27jin.toby.user.template.Calculator;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class CalcSumTest {
    private Calculator calculator;
    private String filePath;

    @Before
    public void setUp() {
        this.calculator = new Calculator();
        this.filePath = "/Users/we/workspace/toy/toby/numbers.txt";
    }

    @Test
    public void add() throws IOException {
        int calcSum = this.calculator.add(filePath);
        Assert.assertThat(10, CoreMatchers.is(calcSum));
    }

    @Test
    public void multiply() throws IOException {
        int calcSum = this.calculator.multiply(filePath);
        Assert.assertThat(24, CoreMatchers.is(calcSum));
    }

}
