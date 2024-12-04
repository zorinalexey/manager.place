WORKDIR /app

COPY /build/libs/service.jar build/

WORKDIR /app/build
EXPOSE 8081
ENTRYPOINT java -jar service.jar