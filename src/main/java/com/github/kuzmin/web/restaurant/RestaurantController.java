package com.github.kuzmin.web.restaurant;

import com.github.kuzmin.model.Restaurant;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.github.kuzmin.repository.RestaurantRepository;
import com.github.kuzmin.to.RestaurantTo;

import java.util.List;

import static com.github.kuzmin.to.RestaurantTo.fromEntity;
import static com.github.kuzmin.util.validation.ValidationUtil.checkNotFoundWithId;

@RestController
@RequestMapping(value = RestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@CacheConfig(cacheNames = {"restaurant"})
@Tag(name = "Info about restaurants", description = "Read information about restaurants in application")
@RequiredArgsConstructor
public class RestaurantController {

    public static final String REST_URL = "/api/restaurants";
    private final RestaurantRepository restaurantRepository;

    @GetMapping
    @Cacheable
    public List<RestaurantTo> getAll() {
        log.info("getAll");
        return restaurantRepository.findAll().stream().map(RestaurantTo::fromEntity).toList();
    }

    @GetMapping("/{id}")
    @Cacheable(key = "#id")
    public RestaurantTo get(@PathVariable int id) {
        log.info("get restaurant {}", id);
        return fromEntity(restaurantRepository.getExisted(id));
    }

    @GetMapping("/{id}/with-dishes")
    public Restaurant getWithDishes(@PathVariable int id) {
        log.info("get with dishes restaurant {}", id);
        return checkNotFoundWithId(restaurantRepository.getWithDishes(id), id);
    }

    @GetMapping("/by-name")
    public RestaurantTo getByName(String name) {
        log.info("getByName restaurant {}", name);
        return fromEntity(restaurantRepository.getExistedByName(name));
    }
}
