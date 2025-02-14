# ToDo API

Uma API RESTful para gerenciamento de tarefas, com testes automatizados, monitoramento e documentação via Swagger.

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
- `DB_USE`
- `DB_PASSWORD`

Podem ser configuradas com um arquivo .env, na sua IDE ou dentro do arquivo de `docker-compose.yml`

## Como excutar

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

4. Acessando a Documentação Swagger

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
    image: postgres:15
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

Substítua as variáveis de ambiente para as corretas.
