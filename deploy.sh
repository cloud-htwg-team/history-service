docker build . -t history-service
docker run -p 8080:8080 --name history-service history-service
