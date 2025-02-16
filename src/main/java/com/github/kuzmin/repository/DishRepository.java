package com.github.kuzmin.repository;

import com.github.kuzmin.error.NotFoundException;
import com.github.kuzmin.model.Dish;
import com.github.kuzmin.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface DishRepository extends JpaRepository<Dish, Integer> {
    @Query("SELECT d FROM Dish d WHERE  d.restaurant.id =:restaurantId AND d.isExcludedFromMenu = false ORDER BY d.name")
    List<Dish> getAllByRestaurant(@Param("restaurantId") int restaurantId);

    default Dish get(int id, int restaurantId) {
        return findById(id)
                .filter(dish -> (dish.getRestaurant().id() == restaurantId && !dish.getIsExcludedFromMenu()) )
                .orElse(null);
    }
    @Query("SELECT d FROM Dish d WHERE d.name=LOWER(:name)  AND d.restaurant.id=:restaurantId")
    Optional<Dish> findByName(@Param("name")String name, @Param("restaurantId") int restaurantId);
}
