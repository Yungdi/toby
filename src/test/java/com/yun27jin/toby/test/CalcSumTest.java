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
        int res = this.calculator.add(filePath);
        Assert.assertThat(10, CoreMatchers.is(res));
    }

    @Test
    public void multiply() throws IOException {
        int res = this.calculator.multiply(filePath);
        Assert.assertThat(24, CoreMatchers.is(res));
    }

    @Test
    public void concat() throws IOException {
        String res = this.calculator.concat(filePath);
        Assert.assertThat("4321", CoreMatchers.is(res));
    }

}
