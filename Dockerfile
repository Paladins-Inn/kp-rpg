#
# Build stage
#
FROM quay.io/eclipse/che-java11-maven:7.24.2 AS build

USER root

COPY . /projects

RUN mvn --no-transfer-progress \
    -DskipTests=true -Dmaven.test.skip -Dskip.jar=true -Dskip.javadoc=true -Dskip.source=true -Dskip.site=true \
    -Dquarkus.container-image.build=false -Dquarkus.container-image.push=false \
    -pl !rpg-dsa5,!rpg-fate,!rpg-hexxen,!rpg-saga,!rpg-torg,!rpg-traveller,!rpg-wod,!rpg-bot,!tomb-ui \
    clean install

RUN mvn --no-transfer-progress \
    -DskipTests=true -Dmaven.test.skip -Dskip.jar=true -Dskip.javadoc=true -Dskip.source=true -Dskip.site=true \
    -Dquarkus.container-image.build=false -Dquarkus.container-image.push=false \
    -pl !rpg-core,!rpg-bot,!tomb-ui \
    clean install

RUN mvn --no-transfer-progress \
    -DskipTests=true -Dmaven.test.skip -Dskip.jar=true -Dskip.javadoc=true -Dskip.source=true -Dskip.site=true \
    -Dquarkus.container-image.build=false -Dquarkus.container-image.push=false \
    -rf :rpg-bot -pl !tomb-ui \
    clean package

#
# Package stage
#
FROM registry.access.redhat.com/ubi8/ubi-minimal:8.3

ARG JAVA_PACKAGE=java-11-openjdk-headless
ARG RUN_JAVA_VERSION=1.3.8

ENV LANG='en_GB.UTF-8' LANGUAGE='en_GB:en'

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

# Configure the JAVA_OPTIONS, you can add -XshowSettings:vm to also display the heap size.
ENV JAVA_OPTIONS="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager"

COPY --from=build /projects/rpg-bot/target/*-runner.jar /deployments/app.jar

RUN chown 1001 /deployments/app.jar

EXPOSE 8080
EXPOSE 8443
USER 1001

ENTRYPOINT [ "/deployments/run-java.sh" ]