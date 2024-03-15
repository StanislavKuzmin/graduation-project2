package com.github.kuzmin.web.dish;

import com.github.kuzmin.model.Dish;
import com.github.kuzmin.to.DishTo;
import com.github.kuzmin.web.MatcherFactory;

public class DishTestData {

    public static final MatcherFactory.Matcher<Dish> DISH_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Dish.class, "restaurantId");
    public static final MatcherFactory.Matcher<DishTo> DISH_TO_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(DishTo.class);
    public static final int NOT_FOUND = 100;
    public static final int DISH1_ID = 8;
    public static final Dish dish1 = new Dish(DISH1_ID, "roast", 29900, 400);
    public static final Dish dish2 = new Dish(DISH1_ID + 1, "solyanka", 45005, 300);
    public static final Dish dish3 = new Dish(DISH1_ID + 2, "beet soup", 56002, 200);
    public static final Dish dish4 = new Dish(DISH1_ID + 3, "dressed herring", 32300, 300);
    public static final Dish dish5 = new Dish(DISH1_ID + 4, "goulash", 61002, 100);
    public static final Dish dish6 = new Dish(DISH1_ID + 5, "open sandwich", 21005, 400);
    public static final Dish dish7 = new Dish(DISH1_ID + 6, "pizza", 46400, 600);
    public static final Dish dish8 = new Dish(DISH1_ID + 7, "kebab", 73100, 500);
    public static final Dish dish9 = new Dish(DISH1_ID + 8, "hot dog", 33200, 200);

    public static Dish getNew() {
        return new Dish(null, "beer", 450780, 500);
    }

    public static Dish getNewNotValid() {
        return new Dish(null, "beer", Integer.MAX_VALUE, 5500);
    }

    public static Dish getUpdated() {
        return new Dish(DISH1_ID, "roast", 350000, 400);
    }
}
