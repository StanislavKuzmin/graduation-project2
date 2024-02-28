package ru.javaops.topjava2.to;

import ru.javaops.topjava2.model.Dish;

import java.time.LocalDate;
import java.util.Objects;

import static ru.javaops.topjava2.util.DishUtil.createTo;


public record MenuTo(LocalDate date, DishTo dishTo) {
    public MenuTo(LocalDate date, Dish dish) {
        this(date, createTo(dish));
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
