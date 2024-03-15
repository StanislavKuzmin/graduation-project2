package com.github.kuzmin.util;

import com.github.kuzmin.model.Restaurant;
import lombok.experimental.UtilityClass;
import com.github.kuzmin.to.RestaurantTo;

@UtilityClass
public class RestaurantUtil {

    public static RestaurantTo createTo(Restaurant restaurant) {
        return new RestaurantTo(restaurant.getId(), restaurant.getName(), restaurant.getAddress());
    }
}
