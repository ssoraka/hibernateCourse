#!/bin/zsh

docker run --name some-postgres -e POSTGRES_PASSWORD=pass -p 5432:5432 -d postgres