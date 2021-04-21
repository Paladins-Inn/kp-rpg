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


FROM registry.access.redhat.com/ubi8/ubi-minimal:8.3

LABEL io.k8s.description="@project.description@"
LABEL io.k8s.display-name="@project.name@"
LABEL io.openshift.expose-services="8080/TCP"
LABEL io.openshift.tags="spring-boot rpg"
LABEL name="@project.artifactId@"
LABEL url="@project.url@"
LABEL maintainer="@project.organization.name@"
LABEL summary="@project.description@"
LABEL vendor="@project.organization.name@"
LABEL author="Roland T. Lichti <rlichti@kaiserpfalz-edv.de>"
LABEL version="@project.version@"


ARG JAVA_PACKAGE=java-11-openjdk-headless
ARG RUN_JAVA_VERSION=1.3.8

ENV LANG='en_GB.UTF-8' \
    LANGUAGE='en_GB:en' \
    APP_NAME="@project.artifactId@" \
    APP_VERSION="@project.version@" \
    VERSION="@project.version@"

# Install java and the run-java script
# Also set up permissions for user `1001`
RUN microdnf install curl ca-certificates ${JAVA_PACKAGE} \
    && microdnf update \
    && microdnf clean all \
    && mkdir -p /deployments/config \
    && chmod "g+rwX" -R /deployments \
    && chown 1001:root -R /deployments \
    && curl https://repo1.maven.org/maven2/io/fabric8/run-java-sh/${RUN_JAVA_VERSION}/run-java-sh-${RUN_JAVA_VERSION}-sh.sh -o /deployments/run-java.sh \
    && chown 1001 /deployments/run-java.sh \
    && chmod 540 /deployments/run-java.sh \
    && echo "securerandom.source=file:/dev/urandom" >> /etc/alternatives/jre/lib/security/java.security

COPY classes/Dockerfile /deployments

COPY delphi-council-is-@project.version@.jar /deployments/app.jar
RUN chmod 444 /deployments/app.jar

EXPOSE 8080
USER $UID

ENTRYPOINT [ "/deployments/run-java.sh" ]
