FROM amazoncorretto:17-alpine as builder

WORKDIR /app/msvc-usuarios

COPY ./pom.xml /app
COPY ./msvc-usuarios/.mvn ./.mvn
COPY ./msvc-usuarios/mvnw .
COPY ./msvc-usuarios/pom.xml .

RUN ./mvnw clean package -Dmaven.Test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -rf ./target/

COPY ./msvc-usuarios/src ./src

RUN ./mvnw clean package -DskipTests

FROM amazoncorretto:17-alpine

WORKDIR /app
RUN mkdir ./logs

COPY --from=builder /app/msvc-usuarios/target/msvc-usuarios-0.0.1-SNAPSHOT.jar .

EXPOSE 8001

#ENTRYPOINT ["java", "-jar", "msvc-usuarios-0.0.1-SNAPSHOT.jar"]
CMD ["java", "-jar", "msvc-usuarios-0.0.1-SNAPSHOT.jar"]