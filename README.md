# ToDo API

Uma API RESTful para gerenciamento de tarefas, com testes automatizados, monitoramento e documentação via Swagger.

## Sumário

- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Configuração do Projeto](#configuração-do-projeto)
    - [Variáveis de Ambiente](#variáveis-de-ambiente)
- [Como Executar](#como-executar)
- [Testes](#testes)
- [Docker](#docker)
    - [Dockerfile (Multi-Stage)](#dockerfile-multi-stage)
    - [docker-compose.yml](#docker-composeyml)
    - [Como Rodar com Docker](#como-rodar-com-docker)

## Tecnologias Utilizadas

- Spring Boot
- Spring Data JPA
- PostgreSQL
- Spring Boot Actuator (para monitoramento)
- Springdoc OpenAPI (para documentação Swagger)
- JUnit e Mockito (para testes automatizados)

## Configuração do Projeto

### Variáveis de Ambiente

O projeto tem as seguintes variáveis de ambiente:

- `DB_HOST`
- `DB_PORT`
- `DB_NAME`
- `DB_SCHEMA`
- `DB_USER`
- `DB_PASSWORD`

Podem ser configuradas com um arquivo .env, na sua IDE ou dentro do arquivo de `docker-compose.yml`

## Como Executar

1. Clone o repositório

```bash
git clone git@github.com:aetussilicon/ToDo-API.git
cd todoapi
```

2. Configure as variáveis de ambiente.

3. Execução

 - Usando Gradle

 ```bash
    ./gradlew bootRun
 ```

 A API ficará disponível em http://localhost:8080

4. Documentação Swagger

A documentação estará disponível em:

 - http://localhost:8080/swagger-ui/index.html

5. Monitoramento com Actuator

Alguns endpoints úteis do Actuator:

 - Health: http://localhost:8080/actuator/health
 - Métricas: http://localhost:8080/actuator/metrics
 - Informações: http://localhost:8080/actuator/info

## Testes

Para excutar os testes:

```bash
./gradlew test
```

## Docker

A aplicação está preparada para rodar em contêiner.

### Dockerfile (Multi-Stage)
A aplicação utiliza um Dockerfile multi-stage que executa `clean build test` durante a fase de construção:

```dockerfile
# Etapa 1: Build, testes e empacotamento
FROM gradle:7.6.0-jdk23 AS build
WORKDIR /home/gradle/project
COPY . .
RUN gradle clean build --no-daemon

# Etapa 2: Imagem final de execução
FROM openjdk:23-jdk-slim
WORKDIR /app
COPY --from=build /home/gradle/project/build/libs/*SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### docker-compose.yml

```ymal
version: "3.8"

services:
  todoapi:
    build: .
    container_name: todoapi
    ports:
      - "8080:8080"
    environment:
      - DB_HOST=localhost
      - DB_PORT=5432
      - DB_NAME=todoapi_db
      - DB_SCHEMA=public
      - DB_USER=todoapi_user
      - DB_PASSWORD=todoapi_password
    depends_on:
      - postgres

  postgres:
    image: postgres:17
    container_name: todoapi-postgres
    ports:
      - "5432:5432"
    environment:
      - POSTGRES_DB=todoapi_db
      - POSTGRES_USER=todoapi_user
      - POSTGRES_PASSWORD=todoapi_password
    volumes:
      - pgdata:/var/lib/postgresql/data

volumes:
  pgdata:
```

## Como Rodar com Docker

Na raiz do projeto, execute:

```bash
docker-compose up --build
```
