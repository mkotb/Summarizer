FROM maven:3.6.0-jdk-8-alpine

WORKDIR /usr/src/bot
COPY . /usr/src/bot

RUN mvn clean package
CMD ["java", "-jar", "/usr/src/bot/target/summary-bot-1.0-SNAPSHOT.jar"]
