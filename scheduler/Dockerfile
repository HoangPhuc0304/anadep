FROM ubuntu:latest

USER root

#Update packages
RUN apt update -y && apt upgrade -y

#Install essential tools
RUN apt install wget curl -y

#Install Java 17
RUN apt install openjdk-17-jdk -y

LABEL version=1.0.0

#Setup Workplace
WORKDIR /app
COPY target/*.jar app.jar
COPY script ./script
COPY .mvn ./.mvn
COPY mvnw mvnw.cmd ./
ENTRYPOINT ["java","-jar","app.jar"]