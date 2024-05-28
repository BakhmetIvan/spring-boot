FROM openjdk:17-jdk-alpine as builder
WORKDIR library
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} library.jar
RUN java -Djarmode=layertools -jar library.jar extract

FROM openjdk:17-jdk-alpine
WORKDIR library
COPY --from=builder library/dependencies/ ./
COPY --from=builder library/spring-boot-loader/ ./
COPY --from=builder library/snapshot-dependencies/ ./
COPY --from=builder library/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"gig]
EXPOSE 8088
