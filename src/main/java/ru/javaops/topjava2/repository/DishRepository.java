package ru.javaops.topjava2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.topjava2.model.Dish;
import ru.javaops.topjava2.to.DishTo;

import java.util.List;

@Transactional(readOnly = true)
public interface DishRepository extends JpaRepository<Dish, Integer> {
    @Query("SELECT new ru.javaops.topjava2.to.DishTo(d.id, d.name, d.price, d.calories)" +
            " FROM Dish d WHERE d.restaurantId =:restaurantId ORDER BY d.price ASC")
    List<DishTo> getAll(@Param("restaurantId") int restaurantId);

    default Dish get(int id, int restaurantId) {
        return findById(id)
                .filter(dish -> dish.getRestaurantId() == restaurantId)
                .orElse(null);
    }

    @Modifying
    @Transactional
    @Query("DELETE FROM Dish d WHERE d.id=:id AND d.restaurantId=:restaurantId")
    int delete(@Param("id") int id, @Param("restaurantId") int restaurantId);


    @Transactional
    default Dish saveSafety(Dish dish, int restaurantId) {
        if (!dish.isNew() && get(dish.id(), restaurantId) == null) {
            return null;
        }
        dish.setRestaurantId(restaurantId);
        return save(dish);
    }
}
