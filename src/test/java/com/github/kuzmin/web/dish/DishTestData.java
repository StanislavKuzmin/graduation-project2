package com.github.kuzmin.web.dish;

import com.github.kuzmin.model.Dish;
import com.github.kuzmin.model.Restaurant;
import com.github.kuzmin.to.DishTo;
import com.github.kuzmin.web.MatcherFactory;

import static com.github.kuzmin.web.restaurant.RestaurantTestData.*;

public class DishTestData {
    public static final MatcherFactory.Matcher<Dish> DISH_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Dish.class, "restaurant");
    public static final MatcherFactory.Matcher<DishTo> DISH_TO_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(DishTo.class);
    public static final int NOT_FOUND = 100;
    public static final int DISH1_ID = 8;
    private static final Restaurant restaurant1 = new Restaurant(RESTAURANT1_ID, "kwakin", "zvenigorodskaya street, 2/44");
    public static final Dish dish1_rest1 = new Dish(DISH1_ID, "roast", restaurant1, 29900);
    public static final Dish dish2_rest1 = new Dish(DISH1_ID + 1, "solyanka", restaurant1, 45005);
    public static final Dish dish3_rest1 = new Dish(DISH1_ID + 2, "beet soup", restaurant1, 56002);
    public static final Dish dish4_rest2 = new Dish(DISH1_ID + 3, "dressed herring", restaurant2, 32300);
    public static final Dish dish5_rest2 = new Dish(DISH1_ID + 4, "goulash", restaurant2, 61002);
    public static final Dish dish6_rest2 = new Dish(DISH1_ID + 5, "open sandwich", restaurant2, 21005);
    public static final Dish dish7_rest3 = new Dish(DISH1_ID + 6, "pizza", restaurant3, 46400);
    public static final Dish dish8_rest3 = new Dish(DISH1_ID + 7, "kebab", restaurant3, 73100);
    public static final Dish dish9_rest3 = new Dish(DISH1_ID + 8, "hot dog", restaurant3, 33200);

    public static DishTo getNew() {
        return new DishTo(null, "beer", 450780, null, null);
    }

    public static DishTo getNewNotValid() {
        return new DishTo(null, "beer", RESTAURANT1_ID, Integer.MAX_VALUE, false);
    }

    public static DishTo getDuplicate() {
        return new DishTo(null, "roast",  RESTAURANT1_ID,29900, false);
    }

    public static Dish getExcluded() {
        Dish dish = new Dish(DISH1_ID, "roast", 29900);
        dish.setIsExcludedFromMenu(true);
        return dish;
    }
}
