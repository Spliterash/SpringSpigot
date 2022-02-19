package ru.spliterash.springspigot.repo.realizations;

import lombok.experimental.UtilityClass;
import org.springframework.data.mongodb.core.query.Criteria;

@UtilityClass
public class Queries {
    public Criteria findById(String id) {
        return Criteria.where("_id").is(id);
    }
}
