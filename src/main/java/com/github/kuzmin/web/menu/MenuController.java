package com.github.kuzmin.web.menu;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.github.kuzmin.repository.MenuRepository;
import com.github.kuzmin.to.MenuTo;
import com.github.kuzmin.web.restaurant.RestaurantController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = RestaurantController.REST_URL + MenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@Tag(name = "Info about menu of restaurants", description = "Read information about menu of restaurants today or in the past")
public class MenuController {
    public static final String REST_URL = "/{restaurantId}/menu";
    private final MenuRepository menuRepository;

    public MenuController(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @GetMapping("/today")
    public List<MenuTo> getTodayMenu(@PathVariable int restaurantId) {
        log.info("today menu for the restaurant with id= {}", restaurantId);
        return menuRepository.getRestaurantMenuToday(restaurantId);
    }

    @GetMapping("/history")
    public List<MenuTo> getHistoryMenu(@PathVariable int restaurantId,
                                       @RequestParam LocalDate date) {
        log.info("menu for the restaurant with id= {} at the date={}", restaurantId, date);
        return menuRepository.getRestaurantMenuHistory(date, restaurantId);
    }
}
