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
//import com.deca.gateway.curexrate.repository.JsonResourceRepository;
import com.deca.gateway.curexrate.service.JsonResourceService;
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
    
    //@Autowired
    //private JsonResourceRepository jsonResourceRepository;

    @Autowired
    private JsonResourceService jsonResourceService;

    // private final MongoOperations mongoOps;

    // public ExchangeRateRequestMessageListener() {
    //     mongoOps = new MongoTemplate(new SimpleMongoClientDbFactory(MongoClients.create(), "exchangeRateStore"));
    // }

    @Override
	public void onMessage(Message message) {
        log.info("onMessage Message = {}", message.getBody());
        
        String resourceId = "";
        String urlPath = "/latest";
        Map<String, String> params = new HashMap<String, String>();

        String request = new String(message.getBody());
        log.info("request = {}", request);
        String[] requestParamList = request.split("~");
        for (String requestParam : requestParamList) {
            log.info("requestParam = {}", requestParam);

            String[] keyValue = requestParam.split("=");
            if (keyValue[0].isEmpty() || keyValue[1].isEmpty()) {
                continue;
            }
            log.info("requestParamList key = {}, value = {}", keyValue[0], keyValue[1]);
            if (keyValue[0].equalsIgnoreCase("date"))
                urlPath = "/" + keyValue[1];
            else if (keyValue[0].equalsIgnoreCase("resourceId"))
                resourceId = keyValue[1];
            else 
                params.put(keyValue[0] , keyValue[1]);
            
        }

        if (!params.isEmpty())
            urlPath = urlPath.concat("?");

        for(String key : params.keySet()) {
            String value = params.get(key);
            log.info("params key = {}, value = {}", key, value);
            if (urlPath.endsWith("?"))
                urlPath = urlPath.concat(key + "=" + value);
            else
                urlPath = urlPath.concat("&" + key + "=" + value);
        }
            
        //String responseStr = restTemplate.getForObject(urlPath, String.class, params);
        String responseStr = restTemplate.getForObject(urlPath, String.class);
        log.info("onMessage resourceId={}, responseStr = {}", resourceId, responseStr);

        JsonResource jsonResource = new JsonResource.Builder(resourceId, responseStr).build();
        jsonResourceService.save(jsonResource);
    }
}
