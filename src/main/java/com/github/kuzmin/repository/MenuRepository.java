package com.github.kuzmin.repository;

import com.github.kuzmin.model.MenuItem;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface MenuRepository extends JpaRepository<MenuItem, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM MenuItem m WHERE m.id=:id")
    int delete(@Param("id") Integer id);

    @Query("SELECT m FROM MenuItem m WHERE m.dish.restaurantId =:restaurantId AND m.date =:date" +
            " ORDER BY m.dish.price DESC")
    List<MenuItem> getRestaurantMenuHistory(@Param("date") LocalDate date, @Param("restaurantId") int restaurantId);

    @Query("SELECT m FROM MenuItem m WHERE m.dish.restaurantId =:restaurantId AND m.date =current_date" +
            " ORDER BY m.dish.price DESC")
    List<MenuItem> getRestaurantMenuToday(@Param("restaurantId") int restaurantId);

    @Query("SELECT m FROM MenuItem m WHERE m.date =current_date")
    List<MenuItem> getAllRestaurantMenuToday();

    @Query("SELECT COUNT(m.id) = 1 FROM MenuItem m WHERE m.id=:id")
    boolean existsByMenuId(@Param("id") Integer id);
}
