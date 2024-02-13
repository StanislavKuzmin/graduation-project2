package ru.javaops.topjava2.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.topjava2.model.Menu;
import ru.javaops.topjava2.model.MenuId;
import ru.javaops.topjava2.to.MenuTo;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface MenuRepository extends JpaRepository<Menu, MenuId> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Menu m WHERE m.id=:id")
    int delete(@Param("id") MenuId id);

    @EntityGraph(attributePaths = {"dish"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT new ru.javaops.topjava2.to.MenuTo(m.id.date, m.dish)" +
            " FROM Menu m WHERE m.dish.restaurantId =:restaurantId AND m.id.date =:date" +
            " ORDER BY m.dish.price DESC")
    List<MenuTo> getRestaurantMenuHistory(@Param("date") LocalDate date, @Param("restaurantId") int restaurantId);

    @EntityGraph(attributePaths = {"dish"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT new ru.javaops.topjava2.to.MenuTo(m.id.date, m.dish)" +
            " FROM Menu m WHERE m.dish.restaurantId =:restaurantId AND m.id.date =current_date" +
            " ORDER BY m.dish.price DESC")
    List<MenuTo> getRestaurantMenuToday(@Param("restaurantId") int restaurantId);

    @Query("SELECT COUNT(m.id) = 1 FROM Menu m WHERE m.id=:id")
    boolean existsByMenuId(@Param("id") MenuId id);
}
