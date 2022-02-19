package ru.spliterash.springspigot.repo;

import java.util.List;
import java.util.Optional;

public interface CRUDRepo<E> {
    E save(E save);

    Optional<E> findById(String id);

    boolean exist(String id);

    List<E> all();

    void delete(String id);
}