package com.deca.gateway.curexrate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import com.deca.gateway.curexrate.repository.JsonResourceRepository;
import com.deca.gateway.curexrate.model.JsonResource;

@Component
public class MongoDBJsonResourceService implements JsonResourceService {
    @Autowired
	private JsonResourceRepository jsonResourceRepository;

    @Override
    public JsonResource getJson(String resourceId) {
        return jsonResourceRepository.findByResourceId(resourceId);
    }

    @Retryable(value={OptimisticLockingFailureException.class}, maxAttempts=3)
	@Override
    public JsonResource save(JsonResource jsonResource) {
        return jsonResourceRepository.save(jsonResource);
    }
}