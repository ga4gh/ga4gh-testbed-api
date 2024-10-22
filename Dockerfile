##################################################
# BUILDER CONTAINER
##################################################

FROM openjdk:11.0.12 as builder

USER root

WORKDIR /usr/src/dependencies

# INSTALL MAKE
RUN apt update \
    && apt install build-essential -y

# INSTALL SQLITE3
RUN wget https://www.sqlite.org/2021/sqlite-autoconf-3340100.tar.gz \
    && tar -zxf sqlite-autoconf-3340100.tar.gz \
    && cd sqlite-autoconf-3340100 \
    && ./configure \
    && make \
    && make install

# USER 'make' and 'sqlite3' to create the dev database
COPY Makefile Makefile
COPY database/sqlite database/sqlite
RUN make sqlite-db-refresh

##################################################
# GRADLE CONTAINER
##################################################

FROM gradle:7.3.3-jdk11 as gradleimage

WORKDIR /home/gradle/source

COPY build.gradle build.gradle
COPY gradlew gradlew
COPY settings.gradle settings.gradle
COPY src src

RUN gradle wrapper

RUN ./gradlew bootJar

##################################################
# FINAL CONTAINER
##################################################

FROM adoptopenjdk/openjdk12:jre-12.0.2_10-alpine

USER root

ARG VERSION

WORKDIR /usr/src/app

# copy jar, dev db, and dev resource files
COPY --from=gradleimage /home/gradle/source/build/libs/ga4gh-testbed-api-${VERSION}.jar ga4gh-testbed-api.jar
COPY --from=builder /usr/src/dependencies/ga4gh-testbed-api.dev.db ga4gh-testbed-api.dev.db
COPY src/test/resources/ src/test/resources/

RUN chmod 666 ga4gh-testbed-api.dev.db

EXPOSE 4500
ENTRYPOINT ["java", "-jar", "ga4gh-testbed-api.jar", "--server.address=0.0.0.0", "--server.port=4500"]
