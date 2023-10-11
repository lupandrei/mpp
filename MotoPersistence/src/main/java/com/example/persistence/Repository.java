package com.example.persistence;


public interface Repository<E, ID> {
    void add(E e);
    void delete(E e);
    void update(E e, ID id);
    E findByID(ID id);
    Iterable<E> getAll();
}
