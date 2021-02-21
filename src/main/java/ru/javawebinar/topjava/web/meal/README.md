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
    