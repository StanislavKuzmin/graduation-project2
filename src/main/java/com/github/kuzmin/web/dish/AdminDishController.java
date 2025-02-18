package com.github.kuzmin.web.dish;

import com.github.kuzmin.model.Dish;
import com.github.kuzmin.repository.DishRepository;
import com.github.kuzmin.repository.RestaurantRepository;
import com.github.kuzmin.to.DishTo;
import com.github.kuzmin.util.validation.ValidationUtil;
import com.github.kuzmin.web.restaurant.AdminRestaurantController;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static com.github.kuzmin.util.DishUtil.createFromTo;
import static com.github.kuzmin.util.validation.ValidationUtil.checkNotFoundWithId;

@RestController
@RequestMapping(value = AdminDishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@Tag(name = "Manage dishes of restaurants", description = "Create, update and delete dishes of restaurants")
@RequiredArgsConstructor
public class AdminDishController {

    public static final String REST_URL = AdminRestaurantController.REST_URL + DishController.REST_URL;
    private final DishRepository dishRepository;
    private final RestaurantRepository restaurantRepository;
    private final UniqueDishValidator validator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(validator);
    }

    @PatchMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void excludedFromMenu(@PathVariable int restaurantId, @PathVariable int id, @RequestParam String isExcludeFromMenu) {
        log.info("exclude from menu of restaurant={} dish with id={}", restaurantId, id);
        Dish dish = checkNotFoundWithId(dishRepository.get(id, restaurantId), id);
        dish.setIsExcludedFromMenu(Boolean.valueOf(isExcludeFromMenu));
        dishRepository.save(dish);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dish> createWithLocation(@Valid @RequestBody DishTo dishTo, @PathVariable int restaurantId) {
        log.info("create dish {}", dishTo);
        ValidationUtil.checkNew(dishTo);
        Dish dish = createFromTo(dishTo);
        dish.setRestaurant(restaurantRepository.getReferenceById(restaurantId));
        Dish created = dishRepository.save(dish);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getRestaurant().getId(), created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}
