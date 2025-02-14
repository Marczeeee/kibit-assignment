FROM eclipse-temurin:17-jdk-alpine
COPY target/kibit-assignment.jar instant-payment-service.jar
EXPOSE 8080
ENV SPRINGDOC_API-DOCS_PATH=/api-docs
ENV SPRING_JPA_GENERATE-DDL=true
ENTRYPOINT ["java","-jar","/instant-payment-service.jar"]