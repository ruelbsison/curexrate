package com.deca.gateway.curexrate.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.deca.gateway.curexrate.model.JsonResource;

@EnableMongoRepositories(basePackages = "com.deca.gateway.curexrate.repository")
public interface JsonResourceRepository extends MongoRepository<JsonResource, String> {
    //JsonResource findByResourceId(String resourceId);
}
