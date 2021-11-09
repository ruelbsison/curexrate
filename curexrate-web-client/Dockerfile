FROM openjdk:8

RUN apt-get update && apt-get install -y maven
COPY ./curexrate-web-client /curexrate-web-client
RUN  cd /curexrate-web-client && mvn package

EXPOSE 8080
#run the spring boot application
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-Dblabla", "-jar","/curexrate-web-client/target/curexrate-web-client-1.0.jar"]


