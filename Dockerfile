FROM lz1998/gmc:0.1.19
FROM openjdk:11
ADD target/bot-0.jar app.jar
EXPOSE 8081
ENTRYPOINT [ "java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar" ]