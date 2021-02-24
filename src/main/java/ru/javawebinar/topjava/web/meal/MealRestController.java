package ru.javawebinar.topjava.web.meal;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;

@RestController
@RequestMapping(value = MealRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class MealRestController extends AbstractMealController {

    static final String REST_URL = "/rest/user";

    @Override
    @GetMapping("/{id}")
    public Meal get(@PathVariable int id) {
        return super.get(id);
    }

    // TODO: если русские символы в поле "description" - вылетает ошибка
    //  "...Invalid UTF-8 middle byte 0xf6..."
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Meal> createWithLocation(@RequestBody Meal meal) {
        Meal created = super.create(meal);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        super.delete(id);
    }

    @Override
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@RequestBody Meal meal, @PathVariable int id) {
        super.update(meal, id);
    }

    @GetMapping
    public List<MealTo> getAll() {
        return super.getAll();
    }

    // http://localhost:8080/topjava_war_exploded/rest/user/by?start=2020-01-01T08%3A50&end=2020-01-31T23%3A58
    @GetMapping("/by")
    public List<MealTo> getBetweenDateTime(@RequestParam @DateTimeFormat(iso = DATE_TIME) LocalDateTime start,
                                           @RequestParam @DateTimeFormat(iso = DATE_TIME) LocalDateTime end
    ) {
        return super.getBetween(start.toLocalDate(), start.toLocalTime(), end.toLocalDate(), end.toLocalTime());
    }

    // для строки браузера
    // http://localhost:8080/topjava_war_exploded/rest/user/byNullSafe?startDate=&startTime=00%3A01&endDate=&endTime=
    // http://localhost:8080/topjava_war_exploded/rest/user/byNullSafe?startDate=2020-01-01&startTime=08%3A50&endDate=2020-01-31&endTime=23%3A58
    @GetMapping("/byNullSafe")
    public List<MealTo> getBetweenDateTime2(@RequestParam LocalDate startDate,
                                            @RequestParam LocalTime startTime,
                                            @RequestParam LocalDate endDate,
                                            @RequestParam LocalTime endTime
    ) {
        return super.getBetween(startDate, startTime, endDate, endTime);
    }

}