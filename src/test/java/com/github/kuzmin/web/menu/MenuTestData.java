package com.github.kuzmin.web.menu;

import com.github.kuzmin.model.MenuItem;
import com.github.kuzmin.to.MenuTo;
import com.github.kuzmin.web.MatcherFactory;

import java.time.LocalDate;
import java.util.List;

import static com.github.kuzmin.to.DishTo.fromDish;
import static com.github.kuzmin.to.RestaurantTo.fromEntity;
import static com.github.kuzmin.web.restaurant.RestaurantTestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static com.github.kuzmin.web.dish.DishTestData.*;

public class MenuTestData {

    public static final MatcherFactory.Matcher<MenuItem> MENU_MATCHER = MatcherFactory.usingAssertions(MenuItem.class,
            (a, e) -> assertThat(a).usingRecursiveComparison().ignoringFields("dish.restaurant").isEqualTo(e),
            (a, e) -> assertThat(a).usingRecursiveComparison().ignoringFields("dish.restaurant").isEqualTo(e));

    public static final MatcherFactory.Matcher<MenuTo> MENU_TO_MATCHER = MatcherFactory.usingAssertions(MenuTo.class,
            (a, e) -> assertThat(a).usingRecursiveComparison().ignoringFields("dishTo.restaurantTo").isEqualTo(e),
            (a, e) -> assertThat(a).usingRecursiveComparison().ignoringFields("dishTo.restaurantTo").isEqualTo(e));

    public static final int MENU_ITEM1_ID = 25;

    public static final MenuItem MENU_ITEM_1 = new MenuItem(MENU_ITEM1_ID, dish1_rest1, LocalDate.now());
    public static final MenuItem MENU_ITEM_2 = new MenuItem(MENU_ITEM1_ID + 1, dish2_rest1, LocalDate.now());
    public static final MenuItem MENU_ITEM_3 = new MenuItem(MENU_ITEM1_ID + 2, dish3_rest1, LocalDate.now());
    public static final MenuTo menuTo1 = new MenuTo(LocalDate.now(), List.of(fromDish(dish1_rest1)), fromEntity(restaurant1));
    public static final MenuTo menuTo2 = new MenuTo(LocalDate.now(), List.of(fromDish(dish4_rest2), fromDish(dish6_rest2)), fromEntity(restaurant2));
    public static final MenuTo menuTo3 = new MenuTo(LocalDate.now(), List.of(fromDish(dish7_rest3), fromDish(dish8_rest3), fromDish(dish9_rest3)), fromEntity(restaurant3));
    public static final String[] indexes = new String[] {dish1_rest1.getId().toString(), dish2_rest1.getId().toString(), dish3_rest1.getId().toString()};


}
