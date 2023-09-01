package com.example.daegurobus.network.resultInterface;

public interface BaseSingleInterface<T> {
    void success(T item);

    void error(String message);
}