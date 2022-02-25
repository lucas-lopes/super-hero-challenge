FROM adoptopenjdk/openjdk11:alpine
VOLUME /tmp
ARG JAR_FILE
ADD target/super-hero-challenge-0.0.1-SNAPSHOT.jar super-hero-challenge.jar
ENTRYPOINT ["java", "-jar", "super-hero-challenge.jar"]