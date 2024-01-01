package com.enigma.loan_app.service;

import java.util.List;

public interface BaseService<E, ID> {
    E create (E e);
    List<E> getAll();
    E getById(ID id);
    E update (E e);
    void delete(ID id);
}
