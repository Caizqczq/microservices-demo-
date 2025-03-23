
FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /workspace/app

RUN apt-get update && \
    apt-get install -y maven

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

COPY --from=build /workspace/app/target/*.jar app.jar

EXPOSE 9555
ENTRYPOINT ["java", "-jar", "/app/app.jar"]