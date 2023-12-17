# Quick start

Simple Java REST web service with Spring boot and Redis that provides information about GitHub user. No real use
cases :)

## Usage

1. Recommended way to build and run:

```bash
./gradlew build && docker compose build && docker compose up -d
```

2. Invoke API using curl or web browser and navigating to `http://localhost:7701/users/magdevlena`, or by using Swagger
   UI available on `http://localhost:7701/openapi/swagger-ui/index.html` (click on GET /users/{login} and use *Try it
   out* button).

3. To see application logs run:

```bash
docker logs usersservice-app-1 -f
```

Application keeps track of request count per login using Redis. Every time request is received appropriate counter is
incremented and logged on INFO level. Too see all logins and how many times they were used in request go
to `http://localhost:8001/` in your browser. Default configuration uses persistence, so counters are stored on disk and
loaded on startup (even if you rebuild the source code and rerun the containers).

4. Stop all containers using:

```bash
docker compose down
```

### Application configuration

Use `src/main/resources/application.yml` to change web service configuration.

#### Token

It is highly recommended to set `github.token` to get higher rate limit for GitHub API, that is accessed to get user
inforamtion.
Check [Creating a personal access token](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/managing-your-personal-access-tokens#creating-a-personal-access-token-classic)
and keep in mind to not share your public access token with anyone (when generating token, do not choose any scopes, as
they are not necessary to access GitHub users API).

#### Calculations

It is possible to adjust details how calculations for user are performed, using below settings. To learn more about
possible valueas and their meaning see: [
BigDecimal (https://docs.oracle.com/javase/8/docs/api/java/math/BigDecimal.html)

```yml
calculations:
  scale: 10
  rounding-mode: HALF_UP
```

#### OpenAPI (Swagger)

You can access OpenAPI using path configured in `application.yml`, if using default server port simply navigate in
browser to `http://localhost:7701/openapi/swagger-ui/index.html`.

```yml
springdoc:
  swagger-ui.path: /openapi/swagger-ui.html
  api-docs.path: /openapi/api-docs
```

## License

[MIT](https://choosealicense.com/licenses/mit/)