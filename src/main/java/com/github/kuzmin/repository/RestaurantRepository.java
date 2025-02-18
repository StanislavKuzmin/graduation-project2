package com.github.kuzmin.repository;

import com.github.kuzmin.error.NotFoundException;
import com.github.kuzmin.model.Restaurant;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {
    @EntityGraph(attributePaths = {"dishes"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT r FROM Restaurant r WHERE r.id=?1")
    Restaurant getWithDishes(int id);

    @Query("SELECT r FROM Restaurant r WHERE r.name = :name")
    Optional<Restaurant> findByName(String name);

    @Query("SELECT r FROM Restaurant r WHERE r.name = :name AND r.address = :address")
    Optional<Restaurant> findByNameAndAddress(String name, String address);

    default Restaurant getExistedByName(String name) {
        return findByName(name).orElseThrow(() -> new NotFoundException("Restaurant with name=" + name + " not found"));
    }
}
