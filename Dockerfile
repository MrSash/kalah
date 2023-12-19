FROM maven:3.9.6 as copy
WORKDIR /build
COPY . ./

FROM copy as build
WORKDIR /build
RUN mvn package -DskipTests

FROM build as test
WORKDIR /build
CMD mvn test

FROM openjdk:17-oracle as run
WORKDIR /applicaiton
COPY --from=build /build/target/kalah*.jar /applicaiton/kalah.jar
CMD java -jar /applicaiton/kalah.jar
