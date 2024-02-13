package ru.javaops.topjava2.to;

import ru.javaops.topjava2.model.Dish;

import java.time.LocalDate;


public record MenuTo(LocalDate date, Dish dish) {
}
