package com.deca.gateway.curexrate.queue;

import java.util.Map;
import java.util.HashMap;
import org.springframework.stereotype.Component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
//import org.springframework.data.mongodb.core.MongoTemplate;
import com.deca.gateway.curexrate.config.ExchangeRateHostConfiguration;
import com.deca.gateway.curexrate.model.JsonResource;
import com.deca.gateway.curexrate.repository.JsonResourceRepository;
//import org.springframework.data.mongodb.core.MongoOperations;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.SimpleMongoClientDbFactory;
//import com.mongodb.client.MongoClients;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ExchangeRateRequestMessageListener implements MessageListener {

    @Autowired
    private ExchangeRateHostConfiguration exchangeRateHostConfiguration;

    @Autowired
    private RestTemplate restTemplate;

    //@Autowired
    //private MongoTemplate mongoTemplate;
    
    @Autowired
    private JsonResourceRepository jsonResourceRepository;

    // private final MongoOperations mongoOps;

    // public ExchangeRateRequestMessageListener() {
    //     mongoOps = new MongoTemplate(new SimpleMongoClientDbFactory(MongoClients.create(), "exchangeRateStore"));
    // }

    @Override
	public void onMessage(Message message) {
        log.info("onMessage Message = {}", message.getBody());
        
        String resourceId = "";
        String urlPath = exchangeRateHostConfiguration.getHost() + "/latest";
        Map<String, String> params = new HashMap<String, String>();

        String request = new String(message.getBody());
        log.info("request = {}", request);
        String[] requestParamList = request.split("~");
        for (String requestParam : requestParamList) {
            log.info("requestParam = {}", requestParam);

            String[] keyValue = requestParam.split("=");
            log.info("key = {}, value = {}", keyValue[0], keyValue[1]);
            if (keyValue[0].isEmpty() || keyValue[1].isEmpty()) {
                continue;
            }
            if (keyValue[0] != "date")
                urlPath = "/" + keyValue[1];
            else if (keyValue[0] == "resourceId")
                resourceId = keyValue[1];
            else 
                params.put(keyValue[0] , keyValue[1]);
            
        }
            
        //Parse the string after getting the response
        //ResponseEntity<String> responseEntity = restTemplate.getForObject(urlPath, String.class, params);
        //responseEntity.
        String responseStr = restTemplate.getForObject(urlPath, String.class, params);
        log.info("onMessage responseStr = {}", responseStr);

        JsonResource jsonResource = new JsonResource(resourceId, responseStr);
        //mongoOps.insert(jsonResource);
        jsonResourceRepository.save(jsonResource);
        //mongoTemplate.insert(jsonResource);
        //mongoTemplate.insert(jsonResource, resourceId);
    }
}
