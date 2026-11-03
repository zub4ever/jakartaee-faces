# Jakarta Faces Starter

Esqueleto de projeto com **Jakarta EE 11** + **Jakarta Faces 4.1** + **Bootstrap 5**, rodando no **WildFly 40** com **PostgreSQL**, tudo via Docker Compose.

## Subir

```bash
cp .env.example .env          # opcional: ajuste senha/portas
docker compose up --build
```

Acesse http://localhost:8181. Para parar: `docker compose down`. Para apagar o banco também, use `docker compose down -v`.

Depois de mudar código Java ou `.xhtml`, rode de novo `docker compose up --build app`.

## Estrutura

```
├── Dockerfile                    # build Maven + imagem WildFly configurada
├── docker-compose.yml            # app + PostgreSQL
├── docker/configure-wildfly.cli  # driver JDBC + datasource AppDS
├── pom.xml                       # só a API jakarta.jakartaee-api (provided)
└── src/main
    ├── java/com/exemplo/app
    │   ├── model/Item.java              # entidade JPA (exemplo)
    │   ├── repository/ItemRepository.java  # acesso a dados (CDI + @Transactional)
    │   └── web/ItemBean.java            # backing bean @Named @ViewScoped
    ├── resources/META-INF/persistence.xml
    └── webapp
        ├── index.xhtml                  # página de exemplo (CRUD com Ajax)
        ├── resources/css/app.css
        └── WEB-INF
            ├── templates/layout.xhtml   # layout base com Bootstrap 5
            ├── web.xml, faces-config.xml, beans.xml
```

## Como as peças se conectam

1. O `configure-wildfly.cli` cria o datasource `java:jboss/datasources/AppDS`. As credenciais vêm das variáveis `DB_*` do compose.
2. O `persistence.xml` usa esse datasource. Com `hbm2ddl.auto=update`, o Hibernate cria as tabelas sozinho.
3. O `ItemRepository` recebe o `EntityManager` e faz as transações com `@Transactional`.
4. O `ItemBean` expõe dados e ações para a página via EL (`#{itemBean...}`).
5. O `index.xhtml` usa o template e componentes `h:` com classes do Bootstrap.

## Começando seu domínio

- Apague `Item`, `ItemRepository`, `ItemBean` e o conteúdo de `index.xhtml`.
- Troque o pacote `com.exemplo.app` pelo seu.
- Novas páginas: crie `pagina.xhtml` com `<ui:composition template="/WEB-INF/templates/layout.xhtml">`.

## Antes de publicar

- No `web.xml`, mude `jakarta.faces.PROJECT_STAGE` para `Production`.
- No `persistence.xml`, troque `hbm2ddl.auto=update` por migrações (Flyway/Liquibase).
- Use uma senha forte no `.env` e não exponha a porta 5432 do banco.

## Versões

| Item | Versão | Onde mudar |
|---|---|---|
| Jakarta EE API | 11.0.0 | `pom.xml` |
| WildFly | 40.0.1.Final (JDK 21) | `ARG WILDFLY_TAG` no `Dockerfile` |
| Driver PostgreSQL | 42.7.7 | `ARG PG_JDBC_VERSION` no `Dockerfile` |
| PostgreSQL | 17 | `docker-compose.yml` |
| Bootstrap | 5.3.3 (CDN) | `layout.xhtml` |
