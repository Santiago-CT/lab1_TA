
FROM maven:3.8.5-openjdk-17 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests


FROM eclipse-temurin:17-jdk-focal

RUN apt-get update && apt-get install -y \
    libgtk-3-0 \
    libx11-6 \
    libxrender1 \
    libxtst6 \
    libgl1 \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY --from=builder /app/target/demo-1.0-SNAPSHOT-jar-with-dependencies.jar ./app.jar


CMD ["java", "-jar", "app.jar"]