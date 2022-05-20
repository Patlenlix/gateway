FROM eclipse-temurin:17-jre
COPY /target/gateway.jar /usr/src/gateway/
WORKDIR usr/src/gateway
ENTRYPOINT ["java","-jar","gateway.jar"]