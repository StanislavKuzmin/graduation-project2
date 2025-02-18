package com.github.kuzmin.web.restaurant;

import com.github.kuzmin.model.Restaurant;
import com.github.kuzmin.to.RestaurantTo;
import com.github.kuzmin.util.validation.ValidationUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.github.kuzmin.repository.RestaurantRepository;

import java.net.URI;

import static com.github.kuzmin.to.RestaurantTo.fromTo;
import static com.github.kuzmin.to.RestaurantTo.newFromTo;
import static com.github.kuzmin.util.validation.ValidationUtil.assureIdConsistent;

@RestController
@RequestMapping(value = AdminRestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@CacheConfig(cacheNames = "restaurant")
@Tag(name = "Manage restaurant system", description = "Create, update and delete restaurants in application")
@RequiredArgsConstructor
public class AdminRestaurantController {
    public static final String REST_URL = "/api/admin/restaurants";
    private final RestaurantRepository restaurantRepository;
    private final UniqueNameAddressValidator validator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(validator);
    }

    @CacheEvict(allEntries = true)
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete restaurant {}", id);
        restaurantRepository.deleteExisted(id);
    }

    @CacheEvict(allEntries = true)
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody @Valid RestaurantTo restaurantTo, @PathVariable int id) {
        log.info("update restaurant {} with id={}", restaurantTo, id);
        assureIdConsistent(restaurantTo, id);
        restaurantRepository.save(fromTo(restaurantTo));
    }

    @CacheEvict(allEntries = true)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> createWithLocation(@RequestBody @Valid RestaurantTo restaurantTo) {
        log.info("create restaurant {}", restaurantTo);
        ValidationUtil.checkNew(restaurantTo);
        Restaurant created = restaurantRepository.save(newFromTo(restaurantTo));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(RestaurantController.REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}
