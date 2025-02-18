package com.github.kuzmin.repository;

import com.github.kuzmin.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface DishRepository extends JpaRepository<Dish, Integer> {
    @Query("SELECT d FROM Dish d WHERE  d.restaurant.id =:restaurantId AND d.isExcludedFromMenu = false ORDER BY d.name")
    List<Dish> getAllByRestaurant(@Param("restaurantId") int restaurantId);

    default Dish get(int id, int restaurantId) {
        return findById(id)
                .filter(dish -> (dish.getRestaurant().id() == restaurantId && !dish.getIsExcludedFromMenu()) )
                .orElse(null);
    }
    @Query("SELECT COUNT(d.id) = 1 FROM Dish d WHERE d.name=:name  AND d.restaurant.id=:restaurantId AND d.price =:price")
    boolean exists(@Param("name")String name, @Param("restaurantId") int restaurantId, @Param("price") int price);
}
