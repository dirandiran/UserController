Docker container usages:
download+install docker engine: https://docs.docker.com/install/

Build:

выбрать профиль "docker"
clean + install скипнуть тесты, база не поднята. Подымается по факту старта всего


на выходе образ: user-controller:latest
можно взять с докер хабба: dirandiran/user-controller:2.0.0

запуск всего стека с докер композ:
docker-compose up -d

Stop all:
docker-compose down

Полная команда запуска отдельно сервиса в контейнере, при условии что SQL сервер и админка запущены из файла ./docker-compose.yml:
docker run --name=app -p 8085:8085 --network=usercontroller_net --link=usercontroller_db_1 user-controller


Профиль "h2":
mvn clean install с тестами.
h2 console: http://localhost:8085/h2
создание таблицы + наполнение данными: data.sql

Сваггер для простого тестирования работающего сервиса:
http://localhost:8085/swagger-ui.html
в UI выбрать "user-rest-controller"

з.ы. http.get http://localhost:8085/ выкачивает файл маппинга. Из за application/text. Либо в сваггере ошибка приходит по curl get (если выставить producers = application/json)