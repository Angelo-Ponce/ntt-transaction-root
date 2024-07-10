FROM eclipse-temurin:17-jdk

COPY target/ntt-transaction-root-0.0.1-SNAPSHOT.jar ntt-transaction-ws-0.0.1-SNAPSHOT.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","ntt-transaction-ws-0.0.1-SNAPSHOT.jar"]