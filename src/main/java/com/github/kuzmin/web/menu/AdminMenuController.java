package com.github.kuzmin.web.menu;

import com.github.kuzmin.config.TimeProvider;
import com.github.kuzmin.error.DataConflictException;
import com.github.kuzmin.error.NotFoundException;
import com.github.kuzmin.model.Dish;
import com.github.kuzmin.service.MenuService;
import com.github.kuzmin.to.MenuTo;
import com.github.kuzmin.web.restaurant.AdminRestaurantController;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.github.kuzmin.model.MenuItem;
import com.github.kuzmin.repository.DishRepository;
import com.github.kuzmin.repository.MenuRepository;

import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.github.kuzmin.util.validation.ValidationUtil.checkNotFound;

@RestController
@RequestMapping(value = AdminRestaurantController.REST_URL + AdminMenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@Tag(name = "Manage menu of restaurants", description = "Create, update and delete menu of restaurants")
@RequiredArgsConstructor
public class AdminMenuController {

    public static final String REST_URL = "/{restaurantId}/menu";
    private final MenuService menuService;

    @PostMapping
    public MenuItem addDishesToMenuByDate(@PathVariable int restaurantId,
                                          @RequestParam int dishId,
                                          @RequestParam LocalDate date) {
        log.info("Add dish with index={} in menu by date={} for the restaurant with id={}",
                dishId, date, restaurantId);
        return menuService.addToMenuByFutureDate(date, dishId, restaurantId);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFromMenuByDate(@PathVariable int restaurantId,
                                     @RequestParam int dishId,
                                     @RequestParam LocalDate date) {
        log.info("Delete dish with id={} from today menu for the restaurant with id={}", dishId, restaurantId);
        menuService.deleteFromMenuByFutureDate(date, restaurantId, dishId);
    }
}
