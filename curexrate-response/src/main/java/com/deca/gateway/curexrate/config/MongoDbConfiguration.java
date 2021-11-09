package com.deca.gateway.curexrate.config;

import static java.lang.invoke.MethodHandles.lookup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
// import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.beans.factory.annotation.Value;
// import org.springframework.data.mongodb.MongoDatabaseFactory;
// import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
// import org.springframework.data.mongodb.core.convert.MappingMongoConverter;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableMongoRepositories(basePackages= {"ccom.deca.gateway"})
@Configuration
public class MongoDbConfiguration extends AbstractMongoConfiguration {

	@Value("${spring.data.mongodb.database}")
	private String databaseName;

	@Value("${spring.data.mongodb.host}")
	private String host;

	@Value("${spring.data.mongodb.port}")
	private int port;

	@Value("${spring.data.mongodb.username}")
	private String username;

	@Value("${spring.data.mongodb.password}")
	private String password;

	@Override
	protected String getDatabaseName() {
		return this.databaseName;
	}

	//@Override
	protected String getHost() {
		return this.host;
	}

	//@Override
	protected int getPort() {
		return this.port;
	}

	//@Override
	protected String getUsername() {
		return this.username;
	}

	//@Override
	protected String getPassword() {
		return this.password;
	}

	// @Override
	// public MongoDatabaseFactory mongoDbFactory() {
	// 	return new SimpleMongoClientDatabaseFactory(mongoClient(), getDatabaseName());
	// }
	
	@Override
	public Mongo mongo() throws Exception {
		MongoClient mongoClient = null;
		//if(mongoDbProperties.getUri() == null || mongoDbProperties.getUri().isEmpty()) {
			//log.info("Creating mongo client from host, port and credentials");
			//log.info("username: " + mongoDbProperties.getUsername() + " " +
			//		"authDatabase: " + mongoDbProperties.getAuthDatabase() + " " +
			//		"password: " + mongoDbProperties.getPassword());
			MongoCredential credential = MongoCredential.createCredential(getUsername(), 
				getDatabaseName(), getPassword().toCharArray());
			List<ServerAddress> serverAddresses = new ArrayList<>();
			serverAddresses.add(new ServerAddress(getHost(), getPort()));
			// int index = 0;
			// for(String host : mongoDbProperties.getHosts()) {
			// 	int port = Integer.parseInt(mongoDbProperties.getPorts().get(index));
			// 	log.info("host: " + host + " port: " + port);
			// 	serverAddresses.add(new ServerAddress(host, port));
			// 	index++;
			// }
			mongoClient = new MongoClient(serverAddresses, Arrays.asList(credential));
		//}else{
		//	log.info("Creating mongo client from uri: {}", mongoDbProperties.getUri());
		//	MongoClientURI uri = new MongoClientURI(mongoDbProperties.getUri());
		//	mongoClient = new MongoClient(uri);
		//}
		return mongoClient;
	}

	// @Override
	// public MongoTemplate mongoTemplate(MongoDatabaseFactory databaseFactory, MappingMongoConverter converter) {
	// 	return new MongoTemplate(databaseFactory, converter);
	// }

	// @Override
    // public MongoClient mongoClient() {
    //     ConnectionString connectionString = new ConnectionString("mongodb://"+getHost()+":"+getPort()+"/"+getDatabaseName());
    //     MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
    //         .applyConnectionString(connectionString)
    //         .build();
        
    //     return MongoClients.create(mongoClientSettings);
	// 	//return MongoClients.create("mongodb://"+getHost()+":"+getPort()+"/"+getDatabaseName());
    // }
 
    // @Override
    // public Collection getMappingBasePackages() {
    //     return Collections.singleton("com.deca");
    // }
	
}
