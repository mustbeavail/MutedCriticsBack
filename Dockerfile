# ---- build stage ----
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /build
COPY pom.xml .
COPY src ./src
RUN mvn -B clean package -DskipTests

# ---- runtime stage ----
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /build/target/*.war app.war
EXPOSE 80
ENV JAVA_OPTS="-Xmx400m -Xss512k"
# application.properties 는 이미지에 없음 → 실행 시 /app/config 에 마운트된 파일을 읽음
ENTRYPOINT ["sh","-c","exec java $JAVA_OPTS -jar app.war --spring.config.additional-location=file:/app/config/"]
