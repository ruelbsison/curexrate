package com.deca.gateway.curexrate.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
//import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.boot.web.client.RootUriTemplateHandler;

@Configuration
public class ExchangeRateHostConfiguration {
    @Autowired
    CloseableHttpClient httpClient;

    @Value("${api.exchangerate.host.baseurl}")
    private String apiHost;
    
    public String getHost() {
        return apiHost;
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory());
        restTemplate.setUriTemplateHandler(new RootUriTemplateHandler(apiHost));
        return restTemplate;
    }
    
    @Bean
    @ConditionalOnMissingBean
    public HttpComponentsClientHttpRequestFactory clientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory 
                            = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setHttpClient(httpClient);
        return clientHttpRequestFactory;
    }
}
