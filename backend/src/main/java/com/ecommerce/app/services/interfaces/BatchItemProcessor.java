package com.ecommerce.app.services.interfaces;

public interface BatchItemProcessor<T> {
    void process(T item);
}
