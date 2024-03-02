package ru.javaops.topjava2.web.menu;

import ru.javaops.topjava2.model.Menu;
import ru.javaops.topjava2.to.MenuTo;
import ru.javaops.topjava2.web.MatcherFactory;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javaops.topjava2.web.dish.DishTestData.*;

public class MenuTestData {

    public static final MatcherFactory.Matcher<Menu> MENU_MATCHER = MatcherFactory.usingAssertions(Menu.class,
            (a, e) -> assertThat(a).usingRecursiveComparison().ignoringFields("dish.restaurantId").isEqualTo(e),
            (a, e) -> assertThat(a).usingRecursiveComparison().ignoringFields("dish.restaurantId").isEqualTo(e));

    public static final MatcherFactory.Matcher<MenuTo> MENU_TO_MATCHER = MatcherFactory.usingAssertions(MenuTo.class,
            (a, e) -> assertThat(a).usingRecursiveComparison().ignoringFields("dishTo.restaurantId").isEqualTo(e),
            (a, e) -> assertThat(a).usingRecursiveComparison().ignoringFields("dishTo.restaurantId").isEqualTo(e));

    public static final Menu menu1 = new Menu(dish1, LocalDate.now());
    public static final Menu menu2 = new Menu(dish2, LocalDate.now());
    public static final Menu menu3 = new Menu(dish3, LocalDate.now());
    public static final MenuTo menuTo1 = new MenuTo(LocalDate.now(), dish4);
    public static final MenuTo menuTo2 = new MenuTo(LocalDate.now(), dish6);
    public static final MenuTo menuTo3 = new MenuTo(LocalDate.of(2024, 1, 30), dish5);
    public static final MenuTo menuTo4 = new MenuTo(LocalDate.of(2024, 1, 30), dish6);
    public static final String[] indexes = new String[] {dish1.getId().toString(), dish2.getId().toString(), dish3.getId().toString()};


}
