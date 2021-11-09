package com.deca.gateway.curexrate.controller;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import com.deca.gateway.curexrate.model.JsonResource;
//import org.springframework.data.mongodb.core.MongoTemplate;
import com.deca.gateway.curexrate.repository.JsonResourceRepository;
// import org.springframework.data.mongodb.core.MongoOperations;
// import org.springframework.data.mongodb.core.MongoTemplate;
// import org.springframework.data.mongodb.core.SimpleMongoClientDbFactory;
// import com.mongodb.client.MongoClients;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class ExchangeRateResponseController {
    
    //@Autowired
    //private MongoTemplate mongoTemplate;

    @Autowired
    private JsonResourceRepository jsonResourceRepository;

    // private final MongoOperations mongoOps;

    // public ExchangeRateResponseController() {
    //     mongoOps = new MongoTemplate(new SimpleMongoClientDbFactory(MongoClients.create(), "exchangeRateStore"));
    // }

    @RequestMapping(
        value = "/exchangeRateResponse", 
        method = RequestMethod.GET,
        produces = { "application/json" }
    )
    @ResponseBody
    public ResponseEntity<Object> call(@RequestParam String resourceId) {
            log.info("Exchange Rate Response resourceId={}", resourceId);
    
            JsonResource jsonResource = jsonResourceRepository.findById(resourceId);
            if (jsonResource!=null) {
                return new ResponseEntity<>(jsonResource.getJson(), HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
}
