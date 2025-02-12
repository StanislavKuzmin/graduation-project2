package com.github.kuzmin.web.dish;

import com.github.kuzmin.model.Dish;
import com.github.kuzmin.util.validation.ValidationUtil;
import com.github.kuzmin.web.restaurant.AdminRestaurantController;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.github.kuzmin.repository.DishRepository;

import java.net.URI;

import static com.github.kuzmin.util.validation.ValidationUtil.*;

@RestController
@RequestMapping(value = AdminDishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@Tag(name = "Manage dishes of restaurants", description = "Create, update and delete dishes of restaurants")
@RequiredArgsConstructor
public class AdminDishController {

    public static final String REST_URL = AdminRestaurantController.REST_URL + DishController.REST_URL;
    private final DishRepository dishRepository;
    private final UniqueNameValidator validator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(validator);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int restaurantId, @PathVariable int id) {
        log.info("delete {}", id);
        dishRepository.deleteExisted(id, restaurantId);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable int restaurantId, @Valid @RequestBody Dish dish, @PathVariable int id) {
        log.info("update {} with id={}", dish, id);
        ValidationUtil.assureIdConsistent(dish, id);
        checkNotFound(dishRepository.saveSafety(dish, restaurantId), "id=" + id + " or doesn't belong to entity with id=" + restaurantId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dish> createWithLocation(@Valid @RequestBody Dish dish, @PathVariable int restaurantId) {
        log.info("create {}", dish);
        ValidationUtil.checkNew(dish);
        Dish created = dishRepository.saveSafety(dish, restaurantId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getRestaurantId(), created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}
