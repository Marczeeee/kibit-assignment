FROM eclipse-temurin:17-jdk-alpine
COPY target/kibit-assignment.jar instant-payment-service.jar
EXPOSE 8080
ENV SPRINGDOC_API-DOCS_PATH=/api-docs
ENV SPRING_JPA_GENERATE-DDL=true
ENV SPRING_SQL_INIT_MODE=always
ENV SPRING_JPA_DEFER-DATASOURCE-INITIALIZATION=true
ENV SPRING_SQL_INIT_CONTINUE-ON-ERROR=true
ENTRYPOINT ["java","-jar","/instant-payment-service.jar"]