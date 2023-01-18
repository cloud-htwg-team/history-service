docker build . -t history-service
docker tag history-service eu.gcr.io/qrcode-374515/zeyesm/history-service/image
docker push eu.gcr.io/qrcode-374515/zeyesm/history-service/image
