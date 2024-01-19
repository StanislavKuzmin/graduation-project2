package ru.javaops.topjava2.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.topjava2.model.DishDate;
import ru.javaops.topjava2.model.DishDateId;
import ru.javaops.topjava2.to.DishTo;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface DishDateRepository extends JpaRepository<DishDate, DishDateId> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Vote v WHERE v.id=:id")
    int delete(@Param("id") DishDateId id);

    @EntityGraph(attributePaths = {"dish"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT new ru.javaops.topjava2.to.DishTo(d.dish.id, d.dish.name, d.dish.price, d.dish.calories, d.id.date)" +
            " FROM DishDate d WHERE d.restaurant.id =:restaurantId AND d.id.date =:date" +
            " ORDER BY d.dish.price DESC")
    List<DishTo> getRestaurantMenuHistory(@Param("date") LocalDate date, @Param("restaurantId") int restaurantId);

    @EntityGraph(attributePaths = {"dish"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT new ru.javaops.topjava2.to.DishTo(d.dish.id, d.dish.name, d.dish.price, d.dish.calories, d.id.date)" +
            " FROM DishDate d WHERE d.restaurant.id =:restaurantId AND d.id.date =current_date" +
            " ORDER BY d.dish.price DESC")
    List<DishTo> getRestaurantMenuToday(@Param("restaurantId") int restaurantId);

    
}
