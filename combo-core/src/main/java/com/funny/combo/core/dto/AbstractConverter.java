package com.funny.combo.core.dto;

public abstract class AbstractConverter<A, B>  {
    protected abstract B doForward(A a);
    protected abstract A doBackward(B b);
}