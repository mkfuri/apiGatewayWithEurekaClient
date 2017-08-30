package com.navent.realestate.examples.connectors;

@FunctionalInterface
public interface Resteable<T> {

    T doRest() throws RestException;
}
