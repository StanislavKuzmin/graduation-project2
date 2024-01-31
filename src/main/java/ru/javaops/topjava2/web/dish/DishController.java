package ru.javaops.topjava2.web.dish;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.javaops.topjava2.model.Dish;
import ru.javaops.topjava2.repository.DishDateRepository;
import ru.javaops.topjava2.repository.DishRepository;
import ru.javaops.topjava2.to.DishTo;
import ru.javaops.topjava2.web.restaurant.RestaurantController;

import java.time.LocalDate;
import java.util.List;

import static ru.javaops.topjava2.util.validation.ValidationUtil.checkNotFoundWithId;

@RestController
@RequestMapping(value = RestaurantController.REST_URL + DishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class DishController {
    static final String REST_URL = "/{restaurantId}/dishes";
    private final DishRepository dishRepository;
    private final DishDateRepository dishDateRepository;

    public DishController(DishRepository dishRepository, DishDateRepository dishDateRepository) {
        this.dishRepository = dishRepository;
        this.dishDateRepository = dishDateRepository;
    }

    @GetMapping
    public List<Dish> getAll(@PathVariable int restaurantId) {
        log.info("getAll");
        return dishRepository.getAll(restaurantId);
    }

    @GetMapping("/{id}")
    public Dish get(@PathVariable int restaurantId, @PathVariable int id) {
        log.info("get {}", id);
        return checkNotFoundWithId(dishRepository.get(id, restaurantId), id);
    }

    @GetMapping("/today")
    public List<DishTo> getTodayMenu(@PathVariable int restaurantId) {
        return dishDateRepository.getRestaurantMenuToday(restaurantId);
    }

    @GetMapping("/history")
    public List<DishTo> getHistoryMenu(@PathVariable int restaurantId,
                                       @RequestParam LocalDate date) {
        return dishDateRepository.getRestaurantMenuHistory(date, restaurantId);
    }
}
