docker build . -t qr-history
docker run -p 8080:8080 --name qr-history qr-history
