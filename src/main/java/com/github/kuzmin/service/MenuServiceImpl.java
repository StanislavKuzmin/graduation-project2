package com.github.kuzmin.service;

import com.github.kuzmin.config.TimeProvider;
import com.github.kuzmin.error.DataConflictException;
import com.github.kuzmin.model.Dish;
import com.github.kuzmin.model.MenuItem;
import com.github.kuzmin.repository.DishRepository;
import com.github.kuzmin.repository.MenuRepository;
import com.github.kuzmin.to.DishTo;
import com.github.kuzmin.to.MenuTo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.github.kuzmin.to.RestaurantTo.fromEntity;
import static com.github.kuzmin.util.validation.ValidationUtil.checkNotFound;
import static com.github.kuzmin.util.validation.ValidationUtil.checkNotFoundWithId;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;
    private final DishRepository dishRepository;
    private final TimeProvider timeProvider;

    @Override
    @Transactional
    public MenuItem addToMenuByFutureDate(LocalDate date, int dishId, int restaurantId) {
        checkIsDateBefore(date);
        if (menuRepository.existsByMenu(dishId, date, restaurantId)) {
            return null;
        }
        Dish dish = checkNotFound(dishRepository.get(dishId, restaurantId), "id=" + dishId
                + " or doesn't belong to restaurant with id=" + restaurantId
                + "or excluded from menu");
        return menuRepository.save(new MenuItem(null, dish, date));
    }

    @Override
    public void deleteFromMenuByFutureDate(LocalDate date, int dishId, int restaurantId) {
        checkIsDateBefore(date);
        checkNotFoundWithId(menuRepository.delete(dishId, restaurantId, date) != 0, dishId);
    }

    @Override
    public List<MenuTo> getAllByDate(LocalDate date) {
        return menuRepository.getAllRestaurantMenuByDate(date)
                .stream()
                .collect(Collectors.groupingBy(menuItem -> menuItem.getDish().getRestaurant()))
                .entrySet()
                .stream()
                .map(mapItem -> new MenuTo(date,
                        mapItem.getValue().stream().map(MenuItem::getDish).map(DishTo::fromDish).toList(),
                        fromEntity(mapItem.getKey())))
                .toList();
    }

    @Override
    public MenuTo getByDate(LocalDate date, int restaurantId) {
        return Optional.of(menuRepository.getRestaurantMenuByDate(restaurantId, date))
                .map(list -> new MenuTo(date,
                        list.stream().map(MenuItem::getDish).map(DishTo::fromDish).toList(),
                        fromEntity(list.stream().map(menuItem -> menuItem.getDish().getRestaurant()).findFirst().get())))
                .get();
    }

    private void checkIsDateBefore(LocalDate date) {
        LocalDate currentDate = timeProvider.getCurrentDate();
        if (date.isBefore(currentDate)) {
            throw new DataConflictException("Date is before than current date");
        }
    }
}
