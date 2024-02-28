package ru.javaops.topjava2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.topjava2.error.NotFoundException;
import ru.javaops.topjava2.model.Dish;
import ru.javaops.topjava2.to.DishTo;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface DishRepository extends JpaRepository<Dish, Integer> {
    @Query("SELECT new ru.javaops.topjava2.to.DishTo(d.id, d.name, d.price, d.calories, d.restaurantId)" +
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

    @Query("SELECT d FROM Dish d WHERE d.name=LOWER(:name)  AND d.restaurantId=:restaurantId")
    Optional<Dish> findByName(@Param("name")String name, @Param("restaurantId") int restaurantId);

    default void deleteExisted(int id, int restaurantId) {
        if (delete(id, restaurantId) == 0) {
            throw new NotFoundException("Entity with id=" + id + " not found");
        }
    }

    @Transactional
    default Dish saveSafety(Dish dish, int restaurantId) {
        if (!dish.isNew() && get(dish.id(), restaurantId) == null) {
            return null;
        }
        dish.setRestaurantId(restaurantId);
        dish.setName(dish.getName().toLowerCase());
        return save(dish);
    }
}
