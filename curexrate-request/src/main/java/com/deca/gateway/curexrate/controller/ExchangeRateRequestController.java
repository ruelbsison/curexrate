package com.deca.gateway.curexrate.controller;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;
import java.util.Collections;

//import com.deca.gateway.curexrate.queue.ExchangeRateRequestSender;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class ExchangeRateRequestController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Queue queue;
    
    //@Autowired
    //private ExchangeRateRequestSender exchangeRateRequestSender;

    public ExchangeRateRequestController() {}

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/exchangeRate", method = RequestMethod.GET)
    @ResponseBody
    //public ResponseEntity<String> call(@RequestParam Map<String, Optional<String>> params) {
        public ResponseEntity<String> call(@RequestParam(name = "base", required = false) String base,
                                        @RequestParam(name = "symbols", required = false) String symbols,
                                        @RequestParam(name = "date", required = false) String date,
                                        @RequestParam(name = "amount", required = false) String amount,
                                        @RequestParam(name = "places", required = false) Integer places) {
            log.info("Exchange Rate Request");
    
        String resourceId = UUID.randomUUID().toString();
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("resourceId", resourceId);
            responseHeaders.set("processingTime", "1000");
            
            log.info("Exchange Rate Request resourceId={}", resourceId);

             StringBuilder builder = new StringBuilder();
             builder.append("resourceId="+resourceId);

             if (base != null && base.length() > 0) {
                if (builder.length() > 0)
                    builder.append("~");
                builder.append("base="+base);
             }

             if (symbols != null && symbols.length() > 0) {
                if (builder.length() > 0)
                    builder.append(",");
                builder.append("symbols="+symbols);
             }

             if (date != null && date.length() > 0) {
                if (builder.length() > 0)
                    builder.append(",");
                builder.append("date="+date);
             }

             if (amount != null && amount.length() > 0) {
                if (builder.length() > 0)
                    builder.append(",");
                builder.append("amount="+amount);
             }

             if (places != null) {
                if (builder.length() > 0)
                    builder.append(",");
                builder.append("places="+places.toString());
             }
            
        
        //exchangeRateRequestSender.send(builder.toString());
        rabbitTemplate.convertAndSend(this.queue.getName(), builder.toString());
        log.info("Exchange Rate Request queued={}", builder.toString());

            return new ResponseEntity<String>("", responseHeaders, HttpStatus.ACCEPTED);
        }
}
