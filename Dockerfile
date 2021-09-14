FROM openjdk:11
ARG VERSION=0.0.1-SNAPSHOT
EXPOSE 8080
WORKDIR /app/
COPY build/libs/dataentryservice-${VERSION}.jar /app/app.jar
ENTRYPOINT ["java", "-jar","/app/app.jar"]