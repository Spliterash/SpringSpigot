package ru.spliterash.springspigot.repo.realizations.repos;

import net.jodah.typetools.TypeResolver;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.spliterash.springspigot.repo.CRUDRepo;
import ru.spliterash.springspigot.repo.realizations.Queries;
import ru.spliterash.springspigot.repo.realizations.mapper.MongoEntityMapper;

import java.util.List;
import java.util.Optional;

public abstract class AbstractMappingMongoCRUDRepo<E, D> extends AbstractMongoCRUDRepo<E> implements CRUDRepo<E> {
    private final MongoEntityMapper<E, D> mapper;
    private final Class<D> documentClass;

    public AbstractMappingMongoCRUDRepo(MongoTemplate mongoTemplate, MongoEntityMapper<E, D> mapper) {
        super(mongoTemplate);
        this.mapper = mapper;
        //noinspection unchecked
        documentClass = (Class<D>) TypeResolver.resolveRawArguments(AbstractMappingMongoCRUDRepo.class, getClass())[1];

    }

    @Override
    public E save(E save) {
        return mapper.docToEntity(mongoTemplate.save(mapper.entityToDoc(save)));
    }

    @Override
    public Optional<E> findById(String id) {
        return Optional.ofNullable(mongoTemplate.findById(id, documentClass))
                .map(mapper::docToEntity);
    }

    @Override
    public boolean exist(String id) {
        return mongoTemplate.exists(wrap(Queries.findById(id)), documentClass);
    }

    @Override
    public List<E> all() {
        return mapper.docToEntityList(mongoTemplate.findAll(documentClass));
    }

    @Override
    public void delete(String id) {
        mongoTemplate.remove(wrap(Queries.findById(id)), documentClass);
    }
}
