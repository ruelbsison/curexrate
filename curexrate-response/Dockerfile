FROM openjdk:8

RUN apt-get update && apt-get install -y maven
COPY ./curexrate-response /curexrate-response
RUN  cd /curexrate-response && mvn package

EXPOSE 18280
#run the spring boot application
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-Dblabla", "-jar","/curexrate-response/target/curexrate-response-1.0.jar"]


