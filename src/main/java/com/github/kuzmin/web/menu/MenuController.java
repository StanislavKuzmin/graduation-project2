package com.github.kuzmin.web.menu;

import com.github.kuzmin.config.TimeProvider;
import com.github.kuzmin.service.MenuService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.github.kuzmin.to.MenuTo;
import com.github.kuzmin.web.restaurant.RestaurantController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = RestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@CacheConfig(cacheNames = {"menu"})
@Slf4j
@Tag(name = "Info about menu of restaurants", description = "Read information about menu of restaurants")
@RequiredArgsConstructor
public class MenuController {
    private final MenuService menuService;
    private final TimeProvider timeProvider;

    @GetMapping("/menu/today")
    @Cacheable
    public List<MenuTo> getTodayMenuAllRestaurant() {
        log.info("today menu for all restaurants");
        return menuService.getAllByDate(timeProvider.getCurrentDate());
    }

    @GetMapping("/menu/by-date")
    public List<MenuTo> getByDateMenuAllRestaurant(@RequestParam LocalDate date) {
        log.info("Menu for all restaurants by date {}", date);
        return menuService.getAllByDate(date);
    }

    @GetMapping("/{restaurantId}/menu/today")
    public MenuTo getTodayMenuRestaurant(@PathVariable int restaurantId) {
        log.info("today menu for the restaurant with id {}", restaurantId);
        return menuService.getByDate(timeProvider.getCurrentDate(), restaurantId);
    }

    @GetMapping("/{restaurantId}/menu/by-date")
    public MenuTo getMenuRestaurantByDate(@PathVariable int restaurantId, @RequestParam LocalDate date) {
        log.info("Menu for the restaurant with id {} by date={}", restaurantId, date);
        return menuService.getByDate(date, restaurantId);
    }
}
