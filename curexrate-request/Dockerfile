FROM openjdk:8

RUN apt-get update && apt-get install -y maven
COPY ./curexrate-request /curexrate-request
RUN  cd /curexrate-request && mvn package

EXPOSE 18180
#run the spring boot application
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-Dblabla", "-jar","/curexrate-request/target/curexrate-request-1.0.jar"]


