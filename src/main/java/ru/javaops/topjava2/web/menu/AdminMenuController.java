package ru.javaops.topjava2.web.menu;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.javaops.topjava2.error.NotFoundException;
import ru.javaops.topjava2.model.Dish;
import ru.javaops.topjava2.model.Menu;
import ru.javaops.topjava2.model.MenuId;
import ru.javaops.topjava2.repository.DishRepository;
import ru.javaops.topjava2.repository.MenuRepository;
import ru.javaops.topjava2.to.MenuTo;
import ru.javaops.topjava2.web.restaurant.AdminRestaurantController;

import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ru.javaops.topjava2.util.validation.ValidationUtil.checkNotFound;

@RestController
@RequestMapping(value = AdminRestaurantController.REST_URL + AdminMenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class AdminMenuController {

    public static final String REST_URL = "/{restaurantId}/menu";
    private final DishRepository dishRepository;
    private final MenuRepository menuRepository;
    private final Clock clock;

    public AdminMenuController(DishRepository dishRepository, MenuRepository menuRepository, Clock clock) {
        this.dishRepository = dishRepository;
        this.menuRepository = menuRepository;
        this.clock = clock;
    }

    @Transactional
    @PostMapping("/add")
    public List<Menu> addDishesToTodayMenu(@PathVariable int restaurantId, @RequestParam("dishesIndex[]") List<String> dishesIndex) {
        List<Integer> dishesId = dishesIndex.stream().map(Integer::parseInt).toList();
        List<Menu> todayMenu = new ArrayList<>();
        for (Integer id : dishesId) {
            Dish dish = checkNotFound(dishRepository.get(id, restaurantId), "id=" + id + " or doesn't belong to entity with id=" + restaurantId);
            Menu menu = new Menu(dish, LocalDate.now(clock));
            todayMenu.add(menu);
        }
        log.info("Add dish with indexes={} in today menu for the restaurant with id={}",
                Arrays.toString(dishesId.toArray()), restaurantId);
        return menuRepository.saveAll(todayMenu);
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
    public List<Menu> addMenuFromDate(@PathVariable int restaurantId, @RequestParam LocalDate date) {
        List<MenuTo> menuTos = menuRepository.getRestaurantMenuHistory(date, restaurantId);
        if (!menuTos.isEmpty()) {
            List<Menu> todayMenu = createTodayMenu(restaurantId, menuTos);
            log.info("Add menu from date={} to today menu for the restaurant with id={}", date, restaurantId);
            return menuRepository.saveAll(todayMenu);
        }
        throw new NotFoundException("There is no menu to copy for the selected date " + date + "for the restaurant with id=" + restaurantId);
    }

    private List<Menu> createTodayMenu(int restaurantId, List<MenuTo> menuTos) {
        return menuTos.stream()
                .map(m -> {
                    Dish dish = new Dish(m.dishTo().id(), m.dishTo().getName(), m.dishTo().getPrice(), m.dishTo().getCalories());
                    dish.setRestaurantId(restaurantId);
                    return new Menu(dish, LocalDate.now());
                }).toList();
    }
}
