package ru.spliterash.springspigot.repo.realizations.repos;

import net.jodah.typetools.TypeResolver;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.spliterash.springspigot.repo.CRUDRepo;
import ru.spliterash.springspigot.repo.realizations.Queries;

import java.util.List;
import java.util.Optional;

public abstract class AbstractSimpleMongoCRUDRepo<E> extends AbstractMongoCRUDRepo<E> implements CRUDRepo<E> {
    private final Class<E> documentClass;

    public AbstractSimpleMongoCRUDRepo(MongoTemplate mongoTemplate) {
        super(mongoTemplate);
        //noinspection unchecked
        documentClass = (Class<E>) TypeResolver.resolveRawArgument(AbstractSimpleMongoCRUDRepo.class, getClass());

    }

    @Override
    public E save(E save) {
        return mongoTemplate.save(save);
    }

    @Override
    public Optional<E> findById(String id) {
        return Optional.ofNullable(mongoTemplate.findById(id, documentClass));
    }

    @Override
    public boolean exist(String id) {
        return mongoTemplate.exists(wrap(Queries.findById(id)), documentClass);
    }

    @Override
    public List<E> all() {
        return mongoTemplate.findAll(documentClass);
    }

    @Override
    public void delete(String id) {
        mongoTemplate.remove(wrap(Queries.findById(id)), documentClass);
    }
}
