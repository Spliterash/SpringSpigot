package ru.spliterash.springspigot.repo.realizations.repos;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import ru.spliterash.springspigot.repo.CRUDRepo;

public abstract class AbstractMongoCRUDRepo<E> implements CRUDRepo<E> {
    protected final MongoTemplate mongoTemplate;

    public AbstractMongoCRUDRepo(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    protected Query wrap(Criteria criteria) {
        return new Query(criteria);
    }
}
