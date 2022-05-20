# Spring Boot Gateway Implementation

## Description

This is a simple Spring Boot Gateway implementation. This gateway was created to be used with our final project in the Web Service course at IT-Högskolan, Gothenburg.</br>
This service connects our schoolmates microservices and provides a secure entrance to our service cluster.

## Current configuration
The Gateway is by default configured to run with the [Authentication service](https://github.com/fredrik-philippe-vimbayi/auth-microservice) and the [Image service](https://github.com/Patlenlix/image-storage).</br>
To `add more routes/services`, please see `step 7 of Deployment`

## Deployment

1. Create network:</br>
   `docker network create net`
2. Start consul:</br>
   `docker run -d -p 8500:8500 -p 8600:8600/udp --name=consul --network=net consul agent -server -ui -node=server-1 -bootstrap-expect=1 -client='0.0.0.0'`
3. Add config file to consul
   1. Open Consul UI: [http://localhost:8500](http://localhost:8500)
   2. Go to Key/Value
   3. Create the following folder structure: `config/image-service/`
   4. Create a file named: `data (.yml)`
   5. Add the following YML data:

```yaml
spring:
   cloud:
      consul:
         discovery:
            register: false
            registerHealthCheck: false
         host: consul
server:
   port: 8000
key:
   public: <your-public-key
``` 

4. Add your Public JWT key to the config
   1. Get a Private/Public keypair from our classmates [Authentication service](https://github.com/fredrik-philippe-vimbayi/auth-microservice)
   2. Make sure the Private key in the `Authentication Consul config` is paired with the Public Key the `Gateway Consul config`
   3. Add your Public key where `<your-public-key>` is written, make sure to remove any `""` surrounding it.
5. Clone this project</br>
   `git clone https://github.com/Patlenlix/gateway.git`
6. Open project locally and run `mvn package`
7. Start Docker desktop
8. Run Dockerfile while standing in project root</br>
   `docker build -t gateway:latest .`
9. Start the Gateway container</br>
   `docker run -d --name gateway -p 8000:8000 --network=net -e CONSUL_HOST=consul ghcr.io/patlenlix/gateway:latest`
10. To add more routes/services
    1. Add a new line with `.pathMatchers()` in the `SecurityConfiguration` matching the endpoints of the new service
    2. Add a new route in `application.yml` with appropriate `predicates` (paths) and `filters` (redirections)
