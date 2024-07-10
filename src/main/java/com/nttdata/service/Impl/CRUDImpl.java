package com.nttdata.service.Impl;

import com.nttdata.exception.ModelNotFoundException;
import com.nttdata.repository.IGenericRepository;
import com.nttdata.service.ICRUD;

import java.util.List;

public abstract class CRUDImpl<T, ID> implements ICRUD<T, ID> {

    protected abstract IGenericRepository<T, ID> getRepository();

    @Override
    public T save(T t) {
        return getRepository().save(t);
    }

    @Override
    public T update(ID id, T t) {
        //getRepository().findById(id).orElseThrow(() -> new ModelNotFoundException("ID not found: " + id));
        return getRepository().save(t);
    }

    @Override
    public List<T> findAll() {
        return getRepository().findAll();
    }

    @Override
    public T findById(ID id, String method ) {
        getRepository().findById(id).orElseThrow(() -> new ModelNotFoundException(method +": ID not found: " + id));
        return getRepository().findById(id).orElse(null);
    }

    @Override
    public void delete(ID id) {
        getRepository().findById(id).orElseThrow(() -> new ModelNotFoundException("ID not found: " + id));
        getRepository().deleteById(id);
    }
}
