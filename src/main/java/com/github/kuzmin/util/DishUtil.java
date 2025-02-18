package com.github.kuzmin.util;

import com.github.kuzmin.model.Dish;
import com.github.kuzmin.to.DishTo;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DishUtil {
    public static  Dish createFromTo(DishTo dishTo) {
        return new Dish(null, dishTo.getName(), dishTo.getPrice());
    }
}
