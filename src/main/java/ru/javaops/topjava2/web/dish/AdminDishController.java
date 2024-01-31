package ru.javaops.topjava2.web.dish;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javaops.topjava2.error.DataConflictException;
import ru.javaops.topjava2.model.Dish;
import ru.javaops.topjava2.model.DishDate;
import ru.javaops.topjava2.model.DishDateId;
import ru.javaops.topjava2.repository.DishDateRepository;
import ru.javaops.topjava2.repository.DishRepository;
import ru.javaops.topjava2.repository.RestaurantRepository;
import ru.javaops.topjava2.to.DishTo;
import ru.javaops.topjava2.web.restaurant.AdminRestaurantController;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static ru.javaops.topjava2.util.validation.ValidationUtil.*;

@RestController
@RequestMapping(value = AdminRestaurantController.REST_URL + DishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class AdminDishController {
    private final DishRepository dishRepository;
    private final DishDateRepository dishDateRepository;

    public AdminDishController(DishRepository dishRepository,
                               DishDateRepository dishDateRepository) {
        this.dishRepository = dishRepository;
        this.dishDateRepository = dishDateRepository;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int restaurantId, @PathVariable int id) {
        log.info("delete {}", id);
        dishRepository.delete(id, restaurantId);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable int restaurantId, @Valid @RequestBody Dish dish, @PathVariable int id) {
        log.info("update {} with id={}", dish, id);
        assureIdConsistent(dish, id);
        checkNotFoundWithId(dishRepository.saveSafety(dish, restaurantId), dish.getId());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dish> createWithLocation(@Valid @RequestBody Dish dish, @PathVariable int restaurantId) {
        log.info("create {}", dish);
        checkNew(dish);
        Dish created = dishRepository.saveSafety(dish, restaurantId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(DishController.REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @Transactional
    @PostMapping(value = "/today-menu", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Dish addToTodayMenu(@PathVariable int restaurantId, @RequestParam int id) {
        Dish dish = dishRepository.get(id, restaurantId);
        checkNotFoundWithId(dish, id);
        DishDate dishDate = new DishDate(dish, LocalDate.now());
        dishDateRepository.save(dishDate);
        return dish;
    }

    @Transactional
    @DeleteMapping("/today-menu")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFromTodayMenu(@PathVariable int restaurantId, @RequestParam int id) {
        DishDateId dishDateId = new DishDateId(id, LocalDate.now());
        dishDateRepository.get(dishDateId);
        if (dishRepository.get(id, restaurantId).getRestaurantId() != restaurantId) {
            throw new DataConflictException("Dish with id " + id + " doesn't belong to restaurant with id " + restaurantId);
        }
        dishDateRepository.delete(dishDateId);
    }

    @Transactional
    @PostMapping(value = "/today-menu/copy", consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<DishTo> copyMenuFromDate(@PathVariable int restaurantId, @RequestParam LocalDate localDate) {
        List<DishTo> dishTos = dishDateRepository.getRestaurantMenuHistory(localDate, restaurantId);
        List<DishDate> todayMenu = createTodayMenu(restaurantId, dishTos);
        dishDateRepository.saveAll(todayMenu);
        return dishDateRepository.getRestaurantMenuToday(restaurantId);
    }

    private List<DishDate> createTodayMenu(int restaurantId, List<DishTo> dishTos) {
        return dishTos.stream()
                .map(dt -> {
                    Dish dish = new Dish(dt.id(), dt.getName(), dt.getPrice(), dt.getCalories());
                    dish.setRestaurantId(restaurantId);
                    return new DishDate(dish, LocalDate.now());
                })
                .toList();
    }
}
