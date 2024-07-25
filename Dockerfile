FROM gradle:8.5.0-jdk17 AS build
WORKDIR /home/gradle/project
COPY . .
RUN gradle clean build -x test

FROM eclipse-temurin:17.0.5_8-jre-focal AS extract
WORKDIR extracted
COPY --from=build /home/gradle/project/build/libs/*.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract

FROM eclipse-temurin:17.0.5_8-jre-focal
WORKDIR application
COPY --from=extract extracted/dependencies/ ./
COPY --from=extract extracted/spring-boot-loader/ ./
COPY --from=extract extracted/snapshot-dependencies/ ./
COPY --from=extract extracted/application/ ./

ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]