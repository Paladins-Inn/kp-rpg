# Copyright (c) 2021 Kaiserpfalz EDV-Service, Roland T. Lichti.
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program.  If not, see <https://www.gnu.org/licenses/>.


# Staged build for building on quay.io.
#
# Author: klenkes74 <rlichti@kaiserpfalz-edv.de>
# Since: 1.1.0 2021-01-18

#
# TypeScript build stage
#
###FROM registry.access.redhat.com/ubi8/nodejs-14 as npm
###
###USER root
###
###ENV LANG='en_GB.UTF-8' LANGUAGE='en_GB:en'
###
###RUN mkdir /app-src && cd /app-src
###ADD tomb-ui .
###
###RUN rm -rf npm node_modules package-lock.json
###RUN npm install --save --force
###RUN npm run generate-fetcher
###RUN npm run build
###RUN cp -a ./dist /

#
# Maven Build stage
#
FROM quay.io/eclipse/che-java11-maven:7.24.2 AS maven

USER root

COPY . /projects
RUN mkdir -p /projects/rpg-bot/src/main/resources/META-INF/resources
###COPY --from=npm /dist/* /projects/rpg-bot/src/main/resources/META-INF/resources/

ARG MVN_PARAMETER="--batch-mode --no-transfer-progress \
    -DskipTests=true -Dmaven.test.skip -Dskip.jar=true -Dskip.javadoc=true -Dskip.source=true \
    -Dskip.site=true \
    -Dquarkus.container-image.build=false -Dquarkus.container-image.push=false"

RUN mvn ${MVN_PARAMETER} clean install
#RUN cd rpg-bom && mvn ${MVN_PARAMETER} clean install
#RUN cd rpg-parent && mvn ${MVN_PARAMETER} clean install
#RUN cd rpg-core && mvn ${MVN_PARAMETER} clean install

#RUN cd testsupport && mvn ${MVN_PARAMETER} clean install
#RUN cd rpg-game-modules && mvn ${MVN_PARAMETER} clean install

#RUN cd integrations && mvn ${MVN_PARAMETER} -N clean install
#RUN cd integrations/discord && mvn ${MVN_PARAMETER} clean install
#RUN cd integrations/drivethru && mvn ${MVN_PARAMETER} clean install
#RUN cd integrations/datastore && mvn ${MVN_PARAMETER} clean install

#RUN cd rpg-bot && mvn ${MVN_PARAMETER} clean install

RUN cp -a rpg-bot/target/app-runner.jar /app.jar

#
# Package stage
#
FROM registry.access.redhat.com/ubi8/ubi-minimal:8.3

LABEL io.k8s.description="This is a discord bot and connected webservice for supporting RPG tabletop games online without providing a VTT."
LABEL io.k8s.display-name="TOMB Discord Bot"
LABEL io.openshift.expose-services="8080/TCP"
LABEL io.openshift.tags="quarkus rpg discord"
LABEL maintainer="Kaiserpfalz EDV-Service"
LABEL summary="Provides a supporting system for online tabletop RPG playing."
LABEL vendor="Kaiserpfalz EDV-Service"
LABEL version="1.2.0-SNAPSHOT"


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

COPY --from=maven /app.jar /deployments/app.jar
RUN chown 1001 /deployments/app.jar

EXPOSE 8080
EXPOSE 8443
USER 1001

ENTRYPOINT [ "/deployments/run-java.sh" ]