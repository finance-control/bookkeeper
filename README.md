# Bookkeeper 

## How to run locally
```shell
cd docker
docker compose -f docker-compose.local.yaml up -d
```

If you need to rebuild project
```shell
docker compose -f docker-compose.local.yaml up -d --build --force-recreate
```