FROM openjdk:8

RUN apt-get update && apt-get install -y maven
COPY ./exchangerate-host /exchangerate-host
RUN  cd /exchangerate-host && mvn package

#run the spring boot application
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-Dblabla", "-jar","/exchangerate-host/target/exchangerate-host-1.0.jar"]


