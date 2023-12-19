#! /bin/bash

cd ../../
docker build --target test -t kalah_test_runner .
docker run kalah_test_runner