package ru.javaops.topjava2.web.menu;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.javaops.topjava2.error.DataConflictException;
import ru.javaops.topjava2.error.NotFoundException;
import ru.javaops.topjava2.model.Dish;
import ru.javaops.topjava2.model.Menu;
import ru.javaops.topjava2.model.MenuId;
import ru.javaops.topjava2.repository.DishRepository;
import ru.javaops.topjava2.repository.MenuRepository;
import ru.javaops.topjava2.to.MenuTo;
import ru.javaops.topjava2.web.restaurant.AdminRestaurantController;

import java.time.LocalDate;
import java.util.List;

import static ru.javaops.topjava2.util.validation.ValidationUtil.checkNotFoundWithId;

@RestController
@RequestMapping(value = AdminRestaurantController.REST_URL + AdminMenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class AdminMenuController {

    public static final String REST_URL = "/{restaurantId}/menu";
    private final DishRepository dishRepository;
    private final MenuRepository menuRepository;

    public AdminMenuController(DishRepository dishRepository, MenuRepository menuRepository) {
        this.dishRepository = dishRepository;
        this.menuRepository = menuRepository;
    }

    @Transactional
    @PostMapping
    public Menu addDishToTodayMenu(@PathVariable int restaurantId, @RequestParam int id) {
        Dish dish = checkNotFoundWithId(dishRepository.get(id, restaurantId), id);
        MenuId menuId = new MenuId(dish.getId(), LocalDate.now());
        if (menuRepository.existsByMenuId(menuId)) {
            throw new DataConflictException("Dish with id= " + id + " has already in today menu");
        }
        if (dish.getRestaurantId() != restaurantId) {
            throw new DataConflictException("Dish with id " + id + " doesn't belong to restaurant with id " + restaurantId);
        }
        log.info("Add dish with id= {} in today menu", id);
        Menu menu = new Menu(menuId, dish);
        menuRepository.save(menu);
        return menu;
    }

    @Transactional
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFromTodayMenu(@PathVariable int restaurantId, @RequestParam int id) {
        Dish dish = checkNotFoundWithId(dishRepository.get(id, restaurantId), id);
        MenuId menuId = new MenuId(id, LocalDate.now());
        if (!menuRepository.existsByMenuId(menuId)) {
            throw new DataConflictException("Dish with id= " + id + " is not in today menu");
        }
        if (dish.getRestaurantId() != restaurantId) {
            throw new DataConflictException("Dish with id " + id + " doesn't belong to restaurant with id " + restaurantId);
        }
        log.info("Delete dish with id= {} from today menu", id);
        menuRepository.delete(menuId);
    }

    @Transactional
    @PostMapping(value = "/copy")
    public List<Menu> copyMenuFromDate(@PathVariable int restaurantId, @RequestParam LocalDate localDate) {
        List<MenuTo> menuTos = menuRepository.getRestaurantMenuHistory(localDate, restaurantId);
        if (menuTos != null) {
            log.info("Copy menu from date= {} to today menu", localDate);
            List<Menu> todayMenu = createTodayMenu(restaurantId, menuTos);
            menuRepository.saveAll(todayMenu);
            return todayMenu;
        }
        throw new NotFoundException("There is no menu to copy for the selected date " + localDate);
    }

    private List<Menu> createTodayMenu(int restaurantId, List<MenuTo> menuTos) {
        return menuTos.stream()
                .map(m -> {
                    Dish dish = new Dish(m.dish().id(), m.dish().getName(), m.dish().getPrice(), m.dish().getCalories());
                    dish.setRestaurantId(restaurantId);
                    return new Menu(dish, LocalDate.now());
                })
                .toList();
    }
}
