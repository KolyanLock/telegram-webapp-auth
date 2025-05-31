# Telegram Webapp Auth

Приложения для бесшовной авторизации в Telegram Mini Apps через backend

## Требования

- Docker + Docker Compose (Docker Desktop будет достаточно)
- Java 21 и Maven 3.9.x (опционально)

## Настройка

1. Создайте `.env` файл в корне проекта:
    ```properties
    BOT_TOKEN=ваш_телеграм_bot_api_token
    DB_URL=jdbc:ваш_db_url/ваше_db_name
    DB_NAME=ваше_db_name
    DB_USER=ваш_db_user
    DB_PASSWORD=ваш_db_password
    ```

2. Быстрый запуск для деплоя на хостинг с `docker-compose` (Docker-образ будет собран из `Dockerfile`, если
   отсутствует):
    ```bash
    docker-compose up -d
    ```

3. Для запуска приложения без `Dockerfile`:

   Сами создайте бд любым способом и файл `src/main/resources/secret/secret.properties`
   ```properties
    BOT_TOKEN=ваш_телеграм_bot_api_token
    DB_URL=jdbc:ваш_db_url/ваше_db_name
    DB_USER=ваш_db_user
    DB_PASSWORD=ваш_db_password
    ```

4. Сборка и запуск приложения без контейнера (используйте флаг `-DskipTests`, если хотите пропустить тесты):
    - Полная сборка с node, tailwind и запуск тестов
        ```bash
        mvn clean package
        ```
    - Быстрая сборка с пропуском frontend-maven-plugin
      ```bash
      mvn clean package -Pskip-frontend
      ```
    - запуск jar-файла приложения без контейнера
      ```bash
      java -jar target/telegram-webapp-auth-0.0.1-SNAPSHOT.jar
      ```

## Actuator

- После запуска сервиса доступен actuator health по адресу:  
  <http://localhost:8080/actuator/health>
- Доступны отдельно liveness и readiness пробы: <br>
  <http://localhost:8080/actuator/health/liveness> <br>
  <http://localhost:8080/actuator/health/readiness>
