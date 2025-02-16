package com.github.kuzmin.util;

import com.github.kuzmin.model.Dish;
import com.github.kuzmin.model.Restaurant;
import lombok.experimental.UtilityClass;
import com.github.kuzmin.to.DishTo;

@UtilityClass
public class DishUtil {

    public static Dish updateFromTo(Dish dish, DishTo dishTo) {
        dish.setName(dishTo.getName());
        dish.setPrice(dishTo.getPrice());
        dish.setIsExcludedFromMenu(dishTo.getIsExcludedFromMenu());
        return dish;
    }

    public static  Dish createFromTo(DishTo dishTo) {
        return new Dish(null, dishTo.getName(), dishTo.getPrice());
    }
}
