docker build . -t history-service
docker run -p 8888:8888 --name history-service history-service
