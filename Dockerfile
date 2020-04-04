FROM openjdk:8-jdk-alpine

ADD target/userController.jar userController.jar

ENTRYPOINT ["java","-jar","/userController.jar"]