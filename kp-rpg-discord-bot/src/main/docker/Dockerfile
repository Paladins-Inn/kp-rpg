####
# This Dockerfile is used in order to build a container that runs the Quarkus application in JVM mode
#
# Before building the container image run:
#
# mvn package
#
# Then, build the image with:
#
# docker build -f src/main/docker/Dockerfile.jvm -t klenkes74/kp-rpg-discord-bot .
#
# Then run the container using:
#
# docker run -i --rm -p 8080:8080 klenkes74/kp-rpg-discord-bot
#
# If you want to include the debug port into your docker image
# you will have to expose the debug port (default 5005) like this :  EXPOSE 8080 5050
# 
# Then run the container using : 
#
# docker run -i --rm -p 8080:8080 -p 5005:5005 -e JAVA_ENABLE_DEBUG="true" quarkus/parcer-jvm
#
###
FROM registry.access.redhat.com/ubi8/ubi-minimal:8.4

ARG JAVA_PACKAGE=java-11-openjdk-headless
ARG RUN_JAVA_VERSION=1.3.8

# Install java and the run-java script
# Also set up permissions for user `1001`
RUN microdnf install curl ca-certificates ${JAVA_PACKAGE} \
    && microdnf update \
    && microdnf clean all \
    && mkdir /deployments \
    && chown 1001 /deployments \
    && chmod "g+rwX" /deployments \
    && chown 1001:root /deployments \
    && curl https://repo1.maven.org/maven2/io/fabric8/run-java-sh/${RUN_JAVA_VERSION}/run-java-sh-${RUN_JAVA_VERSION}-sh.sh -o /deployments/run-java.sh \
    && chown 1001 /deployments/run-java.sh \
    && chmod 540 /deployments/run-java.sh \
    && echo "securerandom.source=file:/dev/urandom" >> /etc/alternatives/jre/lib/security/java.security

COPY ./app-runner.jar /deployments/app.jar

# Configure the JAVA_OPTIONS, you can add -XshowSettings:vm to also display the heap size.
ENV JAVA_OPTIONS="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager" \
    LANG='en_GB.UTF-8' \
    LANGUAGE='en_GB:en' \
    APP_NAME="${project.artifactId}" \
    APP_VERSION="${project.version}"

LABEL \
    io.k8s.description="This is a discord bot and connected webservice for supporting RPG tabletop games online without providing a VTT." \
    io.k8s.display-name="TOMB Discord Bot" \
    io.openshift.expose-services="8080/TCP" \
    io.openshift.tags="quarkus rpg discord" \
    maintainer="Kaiserpfalz EDV-Service" \
    summary="Provides a supporting system for online tabletop RPG playing." \
    vendor="Kaiserpfalz EDV-Service" \
    version="${project.version}"

EXPOSE 8080
USER 1001

ENTRYPOINT [ "/deployments/run-java.sh" ]