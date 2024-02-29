package ru.javaops.topjava2.web.dish;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.javaops.topjava2.model.Dish;
import ru.javaops.topjava2.repository.DishRepository;
import ru.javaops.topjava2.to.DishTo;
import ru.javaops.topjava2.web.restaurant.RestaurantController;

import java.util.List;

import static ru.javaops.topjava2.util.validation.ValidationUtil.checkNotFound;

@RestController
@RequestMapping(value = RestaurantController.REST_URL + DishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@Tag(name = "Info about dishes of restaurants", description = "Read information about dishes of restaurants")
public class DishController {
    static final String REST_URL = "/{restaurantId}/dishes";
    private final DishRepository dishRepository;

    public DishController(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }

    @GetMapping
    public List<DishTo> getAll(@PathVariable int restaurantId) {
        log.info("getAll");
        return dishRepository.getAll(restaurantId);
    }

    @GetMapping("/{id}")
    public Dish get(@PathVariable int restaurantId, @PathVariable int id) {
        log.info("get {}", id);
        return checkNotFound(dishRepository.get(id, restaurantId), "id=" + id + " or doesn't belong to entity with id=" + restaurantId);
    }
}
