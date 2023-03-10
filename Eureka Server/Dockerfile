FROM eclipse-temurin:17-jdk-jammy
#ARG JAR_FILE=target/*.jar
#COPY ${JAR_FILE} app.jar
#ENTRYPOINT ["java","-jar","app.jar"]
WORKDIR /Eureka Server
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline
COPY src ./src

EXPOSE 8761
CMD ["./mvnw", "spring-boot:run"]