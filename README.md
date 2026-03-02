# quarkus-jpa-sample

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## PostgreSQL with docker-compose

You can start a local PostgreSQL instance with `docker-compose.yml`.  
`src/main/resources/application.properties` is configured to use the following connection:

- Host: `localhost`
- Port: `55901`
- Database: `npb`
- User/Password: `quarkus` / `quarkus`

### Start

```shell
docker compose up -d
```

### Check status

```shell
docker compose ps
docker compose logs -f postgres
```

### Stop

```shell
docker compose down
```

### Recreate with clean data

```shell
docker compose down -v
docker compose up -d
```

After PostgreSQL is up, start the application in dev mode.

```shell
./mvnw compile quarkus:dev
```

## Panache sample endpoint

A simple Panache sample endpoint is available for teams:

- `GET /v1/panache-sample/teams`
- Query params:
- `name` (optional): case-insensitive partial match
- `page` (optional, default `0`)
- `size` (optional, default `5`)
- `GET /v1/panache-sample/teams/native-stats` (native SQL + `COUNT`)
- `GET /v1/panache-sample/teams/native-summary` (native SQL + `COUNT` / `MAX`)
- `GET /v1/panache-sample/teams/native-cte-stats` (native SQL + CTE + `COUNT` + window function)
- Query param:
- `min_manager_count` (optional, default `0`)

Examples:

```shell
curl "http://localhost:8080/v1/panache-sample/teams"
curl "http://localhost:8080/v1/panache-sample/teams?name=giants&page=0&size=10"
curl "http://localhost:8080/v1/panache-sample/teams/native-stats"
curl "http://localhost:8080/v1/panache-sample/teams/native-summary"
curl "http://localhost:8080/v1/panache-sample/teams/native-cte-stats"
curl "http://localhost:8080/v1/panache-sample/teams/native-cte-stats?min_manager_count=1"
```

## Player endpoints

Player API examples:

- `GET /v1/player`
- `GET /v1/player/{id}`
- `POST /v1/player/`
- `DELETE /v1/player/{id}` (soft delete)

Examples:

```shell
curl "http://localhost:8080/v1/player?team_id=1&position=OF"
curl "http://localhost:8080/v1/player/1"
curl -X POST "http://localhost:8080/v1/player/" \
  -H "Content-Type: application/json" \
  -d '{"teamId":1,"name":"テスト太郎","uniformNumber":99,"position":"IF"}'
curl -X DELETE "http://localhost:8080/v1/player/1"
```

## Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```
It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:
```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using: 
```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/quarkus-jpa-sample-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.

## Related Guides

- Hibernate ORM with Panache ([guide](https://quarkus.io/guides/hibernate-orm-panache)): Simplify your persistence code for Hibernate ORM via the active record or the repository pattern
- JDBC Driver - PostgreSQL ([guide](https://quarkus.io/guides/datasource)): Connect to the PostgreSQL database via JDBC

## Provided Code

### Hibernate ORM

Create your first JPA entity

[Related guide section...](https://quarkus.io/guides/hibernate-orm)

[Related Hibernate with Panache section...](https://quarkus.io/guides/hibernate-orm-panache)

-----------

## For development

Here is the list of extensions installed in this project:

```shell
$ quarkus ext list
Current Quarkus extensions installed:

✬ ArtifactId                                         Extension Name
✬ quarkus-flyway                                     Flyway
✬ quarkus-hibernate-orm                              Hibernate ORM
✬ quarkus-hibernate-orm-panache                      Hibernate ORM with Panache
✬ quarkus-hibernate-orm-rest-data-panache            REST resources for Hibernate ORM with Panache
✬ quarkus-hibernate-validator                        Hibernate Validator
✬ quarkus-jdbc-postgresql                            JDBC Driver - PostgreSQL
✬ quarkus-logging-json                               Logging JSON
✬ quarkus-rest                                       REST
✬ quarkus-rest-jackson                               REST Jackson
✬ quarkus-smallrye-openapi                           SmallRye OpenAPI

To get more information, append `--full` to your command line.
```

### Dev console

- http://localhost:8080/q/dev-ui/welcome
