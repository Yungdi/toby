package com.yun27jin.toby.user.template;

@FunctionalInterface
public interface LineCallback<T> {
    T compute(String line, T sum);
}
