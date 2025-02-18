package com.github.kuzmin.repository;

import com.github.kuzmin.model.MenuItem;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface MenuRepository extends BaseRepository<MenuItem> {

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM menu_item WHERE menu_item.id IN (SELECT menu_item.id FROM menu_item LEFT JOIN dish ON menu_item.dish_id = dish.id" +
            " WHERE menu_item.dish_id =:dishId AND dish.restaurant_id=:restaurantId AND menu_item.date=:date)", nativeQuery = true)
    int delete(@Param("dishId") Integer dishId, @Param("restaurantId") Integer restaurantId, @Param("date") LocalDate date);

    @Query("SELECT m FROM MenuItem m WHERE m.dish.restaurant.id =:restaurantId AND m.date =:date AND m.dish.isExcludedFromMenu = false" +
            " ORDER BY m.dish.name")
    List<MenuItem> getRestaurantMenuByDate(@Param("restaurantId") int restaurantId, @Param("date") LocalDate date);

    @Query("SELECT m FROM MenuItem m WHERE m.date =:date AND m.dish.isExcludedFromMenu = false")
    List<MenuItem> getAllRestaurantMenuByDate(@Param("date") LocalDate date);

    @Query("SELECT COUNT(m.id) = 1 FROM MenuItem m WHERE m.dish.id=:dishId AND m.dish.restaurant.id =:restaurantId AND m.date =:date")
    boolean existsByMenu(@Param("dishId") Integer dishId, @Param("date") LocalDate date, @Param("restaurantId") int restaurantId);
}
