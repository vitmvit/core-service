FROM openjdk:17-alpine
COPY ./build/libs/core-service-1.0.jar core-service-1.0.jar
EXPOSE 8082
ENTRYPOINT ["java","-jar","core-service-1.0.jar"]