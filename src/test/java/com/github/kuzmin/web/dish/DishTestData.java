package com.github.kuzmin.web.dish;

import com.github.kuzmin.model.Dish;
import com.github.kuzmin.to.DishTo;
import com.github.kuzmin.web.MatcherFactory;

public class DishTestData {
    public static final MatcherFactory.Matcher<Dish> DISH_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Dish.class, "restaurantId");
    public static final MatcherFactory.Matcher<DishTo> DISH_TO_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(DishTo.class);
    public static final int NOT_FOUND = 100;
    public static final int DISH1_ID = 8;
    public static final Dish dish1_rest1 = new Dish(DISH1_ID, "roast", 29900);
    public static final Dish dish2_rest1 = new Dish(DISH1_ID + 1, "solyanka", 45005);
    public static final Dish dish3_rest1 = new Dish(DISH1_ID + 2, "beet soup", 56002);
    public static final Dish dish4_rest2 = new Dish(DISH1_ID + 3, "dressed herring", 32300);
    public static final Dish dish5_rest2 = new Dish(DISH1_ID + 4, "goulash", 61002);
    public static final Dish dish6_rest2 = new Dish(DISH1_ID + 5, "open sandwich", 21005);
    public static final Dish dish7_rest3 = new Dish(DISH1_ID + 6, "pizza", 46400);
    public static final Dish dish8_rest3 = new Dish(DISH1_ID + 7, "kebab", 73100);
    public static final Dish dish9_rest3 = new Dish(DISH1_ID + 8, "hot dog", 33200);

    public static Dish getNew() {
        return new Dish(null, "beer", 450780);
    }

    public static Dish getNewNotValid() {
        return new Dish(null, "beer", Integer.MAX_VALUE);
    }

    public static Dish getUpdated() {
        return new Dish(DISH1_ID, "roast", 350000);
    }
}
