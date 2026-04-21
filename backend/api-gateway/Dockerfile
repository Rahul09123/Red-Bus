# Stage 1: Build (use Maven image and leverage layer cache)
FROM maven:3.9.8-eclipse-temurin-21 AS build
WORKDIR /app

# Copy pom first to cache dependencies
COPY pom.xml ./
RUN mvn -q -e -DskipTests -Dhttp.keepAlive=true -Dcheckstyle.skip=true dependency:go-offline

# Copy sources and build
COPY src ./src
RUN mvn -q -DskipTests package

# Stage 2: Run (JRE is smaller than JDK)
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENV EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://eureka-server:8761/eureka/
ENTRYPOINT ["java", "-jar", "app.jar"]
