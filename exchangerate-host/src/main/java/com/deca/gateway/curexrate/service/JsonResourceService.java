package com.deca.gateway.curexrate.service;

import com.deca.gateway.curexrate.model.JsonResource;

public interface JsonResourceService {
    JsonResource getJson(String resourceId);

    JsonResource save(JsonResource jsonResource);
}