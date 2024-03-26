# Bookkeeper 

## How to run locally

### Simple boot 
```shell
cd docker
docker compose -f docker-compose.local.yaml up -d
```

If you need to rebuild project
```shell
docker compose -f docker-compose.local.yaml up -d --build --force-recreate
```

### Separate prod likely environment
Allows to restart application without affecting DB
```shell
cd docker
docker compose up -d
cd ..
./gradlew bootRun
```