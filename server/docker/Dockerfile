FROM maven:3.9.9-amazoncorretto-24

ARG APP_DIRECTORY="/app"

# prepare the directory application
RUN mkdir -p $APP_DIRECTORY
WORKDIR $APP_DIRECTORY

# copy and build the project
COPY ./ $APP_DIRECTORY
RUN mvn -Dmaven.test.skip clean package spring-boot:repackage

WORKDIR $TARGET_DIRECTORY
ENTRYPOINT ["java", "-jar", "target/frequencies-server.war"]
