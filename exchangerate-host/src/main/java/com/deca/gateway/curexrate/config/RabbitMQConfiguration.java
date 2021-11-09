package com.deca.gateway.curexrate.config;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.PostConstruct;

import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import com.deca.gateway.curexrate.queue.ExchangeRateRequestMessageListener;

import static java.lang.System.getenv;

@Configuration
public class RabbitMQConfiguration {
    @Value("${queue.name}")
    protected String queueName;

    @Autowired
	private AmqpAdmin amqpAdmin;

    @Autowired
	private ConnectionFactory connectionFactory;

    @Autowired 
    ExchangeRateRequestMessageListener exchangeRateRequestMessageListener;

    private SimpleMessageListenerContainer listenerContainer;

    private final Executor executors;

    public RabbitMQConfiguration() {
        ThreadFactory factory = new ThreadFactory() {
			private volatile long count = 1;
			@Override
			public Thread newThread(Runnable r) {
				return new Thread(r, "DLR : " + count++);
			}
		};
		executors = Executors.newCachedThreadPool(factory);
    }

    @PostConstruct
	public void setup() {
        listenerContainer = new SimpleMessageListenerContainer();
        listenerContainer.setConnectionFactory(connectionFactory);
        listenerContainer.setQueueNames(this.queueName);
        listenerContainer.setAcknowledgeMode(AcknowledgeMode.AUTO);
        //listenerContainer.setConcurrentConsumers(threadsPerQueue);
        //listenerContainer.setMaxConcurrentConsumers(threadsPerQueue*2);
        listenerContainer.setMessageListener(exchangeRateRequestMessageListener);
        listenerContainer.setTaskExecutor(executors);
        listenerContainer.setDefaultRequeueRejected(false);
        listenerContainer.start();
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
