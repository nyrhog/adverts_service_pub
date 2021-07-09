echo off
gradle bootJar
docker build -t sample_app .
docker stop sample_app
docker rm sample_app
docker run -d -it -p 8080:8080 --network my-custom-net --name sample_app sample_app
PAUSE