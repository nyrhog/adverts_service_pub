echo off
call buildScript.bat
docker stop sample_app
docker rm sample_app
docker build -t sample_app .
docker run -d -it -p 8080:8080 -e TZ=Europe/Moscow --network my-custom-net --name sample_app sample_app
PAUSE