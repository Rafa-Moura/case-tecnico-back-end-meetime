FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /app

COPY pom.xml mvnw ./
COPY .mvn .mvn/

RUN mvn dependency:go-offline

COPY src/ src/

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=builder /app/target/*.jar integration-api.jar

EXPOSE 8080

CMD ["java", "-jar", "integration-api.jar"]

RUN rm -rf /root/.m2/repository