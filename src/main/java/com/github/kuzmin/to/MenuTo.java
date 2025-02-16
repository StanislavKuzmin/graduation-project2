package com.github.kuzmin.to;

import com.github.kuzmin.model.Dish;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;


public record MenuTo(LocalDate date, List<DishTo> dishTo, RestaurantTo restaurantTo) {
}
