package com.navent.realestate.examples.connectors;

public class RestConnector<T> {

    public T doRestCall(Resteable<T> callable){
        T response = callable.doRest();
        return response;
    }
}
