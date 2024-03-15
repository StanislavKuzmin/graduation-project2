package com.github.kuzmin.util;

import com.github.kuzmin.model.Dish;
import lombok.experimental.UtilityClass;
import com.github.kuzmin.to.DishTo;

@UtilityClass
public class DishUtil {

    public static DishTo createTo(Dish dish) {
        return new DishTo(dish.getId(), dish.getName(), dish.getPrice(), dish.getCalories(), dish.getRestaurantId());
    }
}
