FROM ubuntu:latest

USER root

#Update packages
RUN apt update -y && apt upgrade -y

#Install essential tools
RUN apt install wget curl -y

#Install Java 17
RUN apt install openjdk-17-jdk -y

#Install Maven 3.9.6
RUN wget https://dlcdn.apache.org/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.tar.gz -P /tmp &&\
    tar xf /tmp/apache-maven-*.tar.gz -C /opt &&\
    ln -s /opt/apache-maven-3.9.6/bin/mvn /usr/bin/mvn

#Install NodeJs 21 & NPM
RUN curl -fsSL https://deb.nodesource.com/setup_21.x | bash - && apt-get install -y nodejs

LABEL version=1.0.0

#Setup Workplace
WORKDIR /app
COPY target/*.jar app.jar
COPY script ./script
COPY .mvn ./.mvn
COPY mvnw mvnw.cmd logo.png ./
ENTRYPOINT ["java","-jar","app.jar"]