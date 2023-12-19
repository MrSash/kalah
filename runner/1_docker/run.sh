#! /bin/bash

cd ../../
source ./runner/config/config.sh
docker build --target run -t kalah_runner .
docker run -p "$PORT":8080 kalah_runner