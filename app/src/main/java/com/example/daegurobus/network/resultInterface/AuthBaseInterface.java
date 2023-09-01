package com.example.daegurobus.network.resultInterface;

public interface AuthBaseInterface<T> {
    void success(T item);

    void error(String message);

    void errorAuth();
}
