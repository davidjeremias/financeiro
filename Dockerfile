FROM adoptopenjdk/openjdk8:ubi

EXPOSE 8080

RUN mkdir /opt/app

ADD target/financeiro.jar /opt/app

ENTRYPOINT ["java", "-jar", "/opt/app/financeiro.jar"]