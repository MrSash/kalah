#! /bin/bash

cd ../../
source ./runner/config/config.sh
./runner/2_maven/apache-maven-3.9.6/bin/mvn clean package -DskipTests
java -jar -Dserver.port="$PORT" ./target/kalah*.jar
