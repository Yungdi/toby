package com.yun27jin.toby.user.domain;

import org.junit.Test;

import static org.junit.Assert.*;

public class LevelTest {

    @Test(expected = AssertionError.class)
    public void valueOf() {
        Level level = Level.valueOf(4);
    }

}