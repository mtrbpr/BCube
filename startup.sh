mvn clean install
sudo docker build --tag bcube:1.0 .
sudo docker run -d -p 8080:8080 --name bcube bcube:1.0