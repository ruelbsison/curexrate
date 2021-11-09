package com.deca.gateway.curexrate.config;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.PostConstruct;

import static java.lang.System.getenv;

@Configuration
public class RabbitMQConfiguration {
    @Value("${queue.name}")
    protected String queueName;

    @Autowired
	private AmqpAdmin amqpAdmin;

    @Autowired
	private ConnectionFactory connectionFactory;

    @PostConstruct
	public void setup() {
        Queue exRateRequestQueue = new Queue(this.queueName, true);
        amqpAdmin.declareQueue(exRateRequestQueue);
	}

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setRoutingKey(this.queueName);
        //template.setQueue(this.queueName);
        return template;
    }

    @Bean
    public Queue queue() {
        return new Queue(this.queueName);
    }

    private static String getEnvOrThrow(String name) {
        final String env = getenv(name);
        if (env == null) {
            throw new IllegalStateException("Environment variable [" + name + "] is not set.");
        }
        return env;
    }
}
