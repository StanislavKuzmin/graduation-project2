package com.github.kuzmin.web.menu;

import com.github.kuzmin.error.DataConflictException;
import com.github.kuzmin.error.NotFoundException;
import com.github.kuzmin.model.Dish;
import com.github.kuzmin.model.MenuId;
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
    private final DishRepository dishRepository;
    private final MenuRepository menuRepository;
    private final Clock clock;

    @Transactional
    @PostMapping("/add")
    public List<MenuItem> addDishesToTodayMenu(@PathVariable int restaurantId, @RequestParam("dishesIndex[]") List<String> dishesIndex) {
        List<Integer> dishesId = dishesIndex.stream().map(Integer::parseInt).toList();
        List<MenuItem> todayMenuItems = new ArrayList<>();
        for (Integer id : dishesId) {
            Dish dish = checkNotFound(dishRepository.get(id, restaurantId), "id=" + id + " or doesn't belong to entity with id=" + restaurantId);
            MenuId menuId = new MenuId(id, LocalDate.now(clock));
            if (menuRepository.existsByMenuId(menuId)) {continue;}
            MenuItem menuItem = new MenuItem(menuId, dish);
            todayMenuItems.add(menuItem);
        }
        if (todayMenuItems.isEmpty()) {
            throw new DataConflictException("Dishes with id=" + Arrays.toString(dishesId.toArray()) + " has already in today menu");
        }
        log.info("Add dish with indexes={} in today menu for the restaurant with id={}",
                Arrays.toString(todayMenuItems.stream().map(m -> m.getDish().id()).toArray()), restaurantId);
        return menuRepository.saveAll(todayMenuItems);
    }

    @Transactional
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFromTodayMenu(@PathVariable int restaurantId, @RequestParam int id) {
        checkNotFound(dishRepository.get(id, restaurantId), "id=" + id + " or doesn't belong to entity with id=" + restaurantId);
        MenuId menuId = new MenuId(id, LocalDate.now(clock));
        checkNotFound(menuRepository.existsByMenuId(menuId), "id= " + id + " is not in today menu");
        log.info("Delete dish with id={} from today menu for the restaurant with id={}", id, restaurantId);
        menuRepository.delete(menuId);
    }

    @Transactional
    @PostMapping("/add-from-date")
    public List<MenuItem> addMenuFromDate(@PathVariable int restaurantId, @RequestParam LocalDate date) {
        List<MenuTo> menuTos = menuRepository.getRestaurantMenuHistory(date, restaurantId);
        if (!menuTos.isEmpty()) {
            return addDishesToTodayMenu(restaurantId, menuTos.stream().map(m -> m.dishTo().getId().toString()).toList());
        }
        throw new NotFoundException("There is no menu to copy for the selected date " + date + "for the restaurant with id=" + restaurantId);
    }
}
