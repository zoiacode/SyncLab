FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn -e -X clean package -DskipTests

FROM eclipse-temurin:17-jdk
WORKDIR /app

COPY --from=build /app/target/SyncLab-0.0.1-SNAPSHOT-jar-with-dependencies.jar app.jar


EXPOSE 4567

CMD ["java", "-jar", "app.jar"]
