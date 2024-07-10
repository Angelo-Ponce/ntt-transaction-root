package com.nttdata.service;

import java.util.List;

public interface ICRUD<T, ID> {

    T save(T t);
    T update(ID id, T t);
    List<T> findAll();
    T findById(ID id, String method);
    void delete(ID id);
}
