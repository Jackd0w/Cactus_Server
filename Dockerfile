FROM openjdk:8-alpine

COPY target/uberjar/cactus-server.jar /cactus-server/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/cactus-server/app.jar"]
