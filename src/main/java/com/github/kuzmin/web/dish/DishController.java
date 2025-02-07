package com.github.kuzmin.web.dish;

import com.github.kuzmin.model.Dish;
import com.github.kuzmin.to.DishTo;
import com.github.kuzmin.web.restaurant.RestaurantController;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.github.kuzmin.repository.DishRepository;

import java.util.List;

import static com.github.kuzmin.util.validation.ValidationUtil.checkNotFound;

@RestController
@RequestMapping(value = RestaurantController.REST_URL + DishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@Tag(name = "Info about dishes of restaurants", description = "Read information about dishes of restaurants")
@RequiredArgsConstructor
public class DishController {
    static final String REST_URL = "/{restaurantId}/dishes";
    private final DishRepository dishRepository;

    @GetMapping
    public List<DishTo> getAll(@PathVariable int restaurantId) {
        log.info("getAll");
        return dishRepository.getAllByRestaurant(restaurantId);
    }

    @GetMapping("/{id}")
    public Dish get(@PathVariable int restaurantId, @PathVariable int id) {
        log.info("get {}", id);
        return checkNotFound(dishRepository.get(id, restaurantId), "id=" + id + " or doesn't belong to entity with id=" + restaurantId);
    }
}
