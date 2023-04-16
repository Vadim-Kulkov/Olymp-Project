FROM openjdk:11
ADD /target/Olymp_Project-0.0.1-SNAPSHOT.jar OlympProject.jar
ENTRYPOINT ["java","-jar","OlympProject.jar"]
