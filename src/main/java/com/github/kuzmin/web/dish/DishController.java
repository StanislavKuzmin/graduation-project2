package com.github.kuzmin.web.dish;

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

import static com.github.kuzmin.to.DishTo.fromDish;
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
        log.info("getAll dishes of restaurant {}", restaurantId);
        return dishRepository.getAllByRestaurant(restaurantId).stream().map(DishTo::fromDish).toList();
    }

    @GetMapping("/{id}")
    public DishTo get(@PathVariable int restaurantId, @PathVariable int id) {
        log.info("get dish {} of restaurant with if {}", id, restaurantId);
        return fromDish(checkNotFound(dishRepository.get(id, restaurantId), "id=" + id
                + " or doesn't belong to restaurant with id=" + restaurantId
                + "or excluded from menu"));
    }
}
