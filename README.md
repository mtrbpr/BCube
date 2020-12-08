Execute bellow commands to run this application as a docker container;

    - mvn clean install
    - sudo docker build --tag bcube:1.0 .
    - sudo docker run -d -p 8080:8080 --name bcube bcube:1.0

The above commands are also available in startup.sh script

The application will be up and running on port 8080 
    - http://127.0.0.1:8080/Network-Topology-1.0-SNAPSHOT

Or if you have tomcat already installed on your machine, you can simply deploy the application by copying the war file into the webapps folder of tomcat and restart tomcat

This project is available on this repository:
    - https://github.com/mtrbpr/BCube