FROM openjdk:17

EXPOSE 8090

WORKDIR /app

ADD target/order-management-api.jar order-management-api.jar

ENTRYPOINT ["java", "jar", "/order-management-api.jar" ]