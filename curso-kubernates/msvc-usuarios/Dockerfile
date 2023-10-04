FROM amazoncorretto:17-alpine

WORKDIR /app/msvc-usuarios

COPY ./pom.xml /app
COPY ./msvc-usuarios/.mvn ./.mvn
COPY ./msvc-usuarios/mvnw .
COPY ./msvc-usuarios/pom.xml .

RUN ./mvnw clean package -Dmaven.Test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -rf ./target/

COPY ./msvc-usuarios/src ./src

RUN ./mvnw clean package -DskipTests

EXPOSE 8001

ENTRYPOINT ["java", "-jar", "./target/msvc-usuarios-0.0.1-SNAPSHOT.jar"]