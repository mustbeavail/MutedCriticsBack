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
# 보안: 비-root(appuser)로 실행 -> RCE 발생해도 컨테이너 내 root 획득 방지.
# 비-root는 특권포트(<1024) 바인딩 불가 -> 앱 포트를 8080으로 (config/nginx도 8080으로 맞춤)
RUN useradd -r -u 1001 -m -d /home/appuser appuser && chown -R appuser:appuser /app
USER appuser
EXPOSE 8080
ENV JAVA_OPTS="-Xmx400m -Xss512k"
# application.properties 는 이미지에 없음 → 실행 시 /app/config 에 마운트된 파일을 읽음
ENTRYPOINT ["sh","-c","exec java $JAVA_OPTS -jar app.war --spring.config.additional-location=file:/app/config/"]
