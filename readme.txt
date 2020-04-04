Сборка:

выбрать профиль "docker"
clean + install скипнуть тесты, база не поднята. Подымается по факту старта всего

на выходе образ: user-controller:latest
можно взять с докер хабба: dirandiran/user-controller:1.0.0

запуск всего стека с докер композ:
docker-compose up -d

Stop all:
docker-compose down

Полная команда запуска отдельно сервиса в контейнере, при условии что SQL сервер и админка запущены из файла ./docker-compose.yml:
docker run --name=app -p 8085:8085 --network=usercontroller_net --link=usercontroller_db_1 user-controller