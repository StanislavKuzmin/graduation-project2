package com.github.kuzmin.to;

import com.github.kuzmin.model.Dish;
import com.github.kuzmin.util.DishUtil;

import java.time.LocalDate;
import java.util.Objects;


public record MenuTo(LocalDate date, DishTo dishTo) {
    public MenuTo(LocalDate date, Dish dish) {
        this(date, DishUtil.createTo(dish));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuTo menuTo = (MenuTo) o;
        return Objects.equals(date, menuTo.date) && Objects.equals(dishTo, menuTo.dishTo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, dishTo);
    }
}
