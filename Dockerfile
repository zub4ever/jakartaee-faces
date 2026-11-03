# Versão da imagem do WildFly (ARG antes do primeiro FROM para poder ser usado no FROM)
ARG WILDFLY_TAG=40.0.1.Final-jdk21

# ---------- Etapa 1: build do .war ----------
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /src
COPY pom.xml .
RUN mvn -B -q dependency:go-offline
COPY src ./src
RUN mvn -B -q package -DskipTests

# ---------- Etapa 2: WildFly com driver PostgreSQL + datasource ----------
FROM quay.io/wildfly/wildfly:${WILDFLY_TAG}

ARG PG_JDBC_VERSION=42.7.7
ADD --chown=jboss:jboss \
    https://repo1.maven.org/maven2/org/postgresql/postgresql/${PG_JDBC_VERSION}/postgresql-${PG_JDBC_VERSION}.jar \
    /tmp/postgresql.jar
COPY --chown=jboss:jboss docker/configure-wildfly.cli /tmp/configure-wildfly.cli

RUN /opt/jboss/wildfly/bin/jboss-cli.sh --file=/tmp/configure-wildfly.cli \
 && rm -rf /opt/jboss/wildfly/standalone/configuration/standalone_xml_history \
           /opt/jboss/wildfly/standalone/data \
           /opt/jboss/wildfly/standalone/log \
           /tmp/postgresql.jar /tmp/configure-wildfly.cli

COPY --from=build --chown=jboss:jboss /src/target/ROOT.war /opt/jboss/wildfly/standalone/deployments/ROOT.war

EXPOSE 8080
