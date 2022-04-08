### STAGE 1: BUILD ###
FROM maven:3.6.3-jdk-11-slim AS build
RUN mkdir -p /workspace
WORKDIR /workspace
COPY pom.xml /workspace
#RUN mvn -f pom.xml dependency:resolve
COPY src /workspace/src
RUN mvn -f pom.xml clean package -Dmaven.test.skip=true
COPY data /workspace/data

### STAGE 2: RUN ###
FROM adoptopenjdk:11-jre-hotspot
COPY --from=build /workspace/target/*.war app.war
COPY --from=build /workspace/data /data

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.war"]
