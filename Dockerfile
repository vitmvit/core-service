FROM openjdk:17-alpine
COPY ./build/libs/core-service-1.0.jar core-service-1.0.jar
ENTRYPOINT ["java","-jar","core-service-1.0.jar"]