# build stage
FROM gradle:7.6.1-jdk17-alpine AS build
WORKDIR /app
COPY . .

RUN gradle clean build --no-daemon
# build stage

FROM openjdk:17-alpine
# COPY build/libs/*.jar app.jar
COPY --from=build /app/build/libs/*.jar ./

RUN mv $(ls *.jar | grep -v plain) app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]