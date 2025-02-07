package com.github.kuzmin.web.menu;

import com.github.kuzmin.model.MenuItem;
import com.github.kuzmin.to.MenuTo;
import com.github.kuzmin.web.MatcherFactory;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static com.github.kuzmin.web.dish.DishTestData.*;

public class MenuTestData {

    public static final MatcherFactory.Matcher<MenuItem> MENU_MATCHER = MatcherFactory.usingAssertions(MenuItem.class,
            (a, e) -> assertThat(a).usingRecursiveComparison().ignoringFields("dish.restaurantId").isEqualTo(e),
            (a, e) -> assertThat(a).usingRecursiveComparison().ignoringFields("dish.restaurantId").isEqualTo(e));

    public static final MatcherFactory.Matcher<MenuTo> MENU_TO_MATCHER = MatcherFactory.usingAssertions(MenuTo.class,
            (a, e) -> assertThat(a).usingRecursiveComparison().ignoringFields("dishTo.restaurantId").isEqualTo(e),
            (a, e) -> assertThat(a).usingRecursiveComparison().ignoringFields("dishTo.restaurantId").isEqualTo(e));

    public static final MenuItem MENU_ITEM_1 = new MenuItem(dish1, LocalDate.now());
    public static final MenuItem MENU_ITEM_2 = new MenuItem(dish2, LocalDate.now());
    public static final MenuItem MENU_ITEM_3 = new MenuItem(dish3, LocalDate.now());
    public static final MenuTo menuTo1 = new MenuTo(LocalDate.now(), dish4);
    public static final MenuTo menuTo2 = new MenuTo(LocalDate.now(), dish6);
    public static final MenuTo menuTo3 = new MenuTo(LocalDate.of(2024, 1, 30), dish5);
    public static final MenuTo menuTo4 = new MenuTo(LocalDate.of(2024, 1, 30), dish6);
    public static final String[] indexes = new String[] {dish1.getId().toString(), dish2.getId().toString(), dish3.getId().toString()};


}
