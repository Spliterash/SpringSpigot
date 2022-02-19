package ru.spliterash.springspigot.repo.realizations.mapper;

import java.util.List;
import java.util.stream.Collectors;

public interface MongoEntityMapper<E, D> {
    D entityToDoc(E entity);

    E docToEntity(D doc);


    default List<D> entityToDocList(List<E> list) {
        return list
                .stream()
                .map(this::entityToDoc)
                .collect(Collectors.toList());
    }

    default List<E> docToEntityList(List<D> list) {
        return list
                .stream()
                .map(this::docToEntity)
                .collect(Collectors.toList());
    }
}
