package com.yun27jin.toby.user.template;

import java.io.BufferedReader;
import java.io.IOException;

@FunctionalInterface
public interface BufferedReaderCallback {
    int compute(BufferedReader bufferedReader) throws IOException;
}
