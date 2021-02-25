

Автоматические проверки качества кода

Codacy Badge (app.codacy.com) -
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/7bd1b8ad1a0d48039a9d7809fd24b1b1)](https://www.codacy.com/manual/vinogor/topjava/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=vinogor/topjava&amp;utm_campaign=Badge_Grade)

Build Status (travis-ci.org) -
[![Build Status](https://travis-ci.org/vinogor/topjava.svg?branch=master)](https://travis-ci.org/vinogor/topjava)

Для корректного логирования при запуске TomCat - укажите каталог для создания логов через установку переменной окружения TOPJAVA_ROOT, например, на корень проекта
( добавить и в run и debug: TOPJAVA_ROOT=c:\Development\IdeaProjects\topjava\ )

VM options: -Dspring.profiles.active="datajpa,postgres"

mvn clean package -DskipTests=true org.codehaus.cargo:cargo-maven2-plugin:1.7.10:run
( TODO: Fatal error compiling: error: invalid target release: 15 )

### Тестирование MealRestController

- get by id
> curl -X GET "http://localhost:8080/topjava_war_exploded/rest/user/100002"

- createWithLocation (dateTime - должно быть уникально)
> curl -H "Content-Type: application/json" -X POST -d '{"dateTime":"2020-01-31T20:14:14","description":"66666","calories":510}' "http://localhost:8080/topjava_war_exploded/rest/user"    

- delete by id
> curl -X DELETE "http://localhost:8080/topjava_war_exploded/rest/user/100002"

- get all
> curl -X GET "http://localhost:8080/topjava_war_exploded/rest/user" 

- getBetweenDateTime
> curl -X GET "http://localhost:8080/topjava_war_exploded/rest/user/by?start=2020-01-01T08:50&end=2020-01-31T23:58"

- getBetweenDateTime2    
> curl -X GET "http://localhost:8080/topjava_war_exploded/rest/user/byNullSafe?startDate=2020-01-01&startTime=08:50&endDate=2020-01-31&endTime=23:58"

> curl -X GET "http://localhost:8080/topjava_war_exploded/rest/user/byNullSafe?startDate=&startTime=&endDate=&endTime="
   