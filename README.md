# run environment
```properties
MYSQL_DATABASE=
MYSQL_ROOT_PASSWORD=
MYSQL_USER=
MYSQL_PASSWORD=
EXPOSE_PORT=
TARGET_PORT=
DATASOURCE_URL=jdbc:mysql://{YOUR_DATABASE_HOS}
PROFILE={local/dev/stg/prd}
```

# build and run
- $ docker build -t shop-app .
- $ docker compose up -d
