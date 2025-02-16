package com.github.kuzmin.service;

import com.github.kuzmin.model.MenuItem;
import com.github.kuzmin.to.MenuTo;

import java.time.LocalDate;
import java.util.List;


public interface MenuService {
    MenuItem addToMenuByFutureDate(LocalDate date, int dishId, int restaurantId);

    void deleteFromMenuByFutureDate(LocalDate date, int dishId, int restaurantId);

    List<MenuTo> getAllByDate(LocalDate date);

    MenuTo getByDate(LocalDate date, int restaurantId);
}
