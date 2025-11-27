FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY target/*-jar-with-dependencies.jar app.jar

EXPOSE 4567

CMD ["java", "-jar", "app.jar"]