package com.github.kuzmin.repository;

import com.github.kuzmin.error.NotFoundException;
import com.github.kuzmin.model.Restaurant;
import com.github.kuzmin.to.RestaurantTo;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {
    @EntityGraph(attributePaths = {"dishes"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT r FROM Restaurant r WHERE r.id=?1")
    Restaurant getWithDishes(int id);

    @Query("SELECT r FROM Restaurant r WHERE r.name = :name")
    Optional<Restaurant> findByName(String name);

    @Query("SELECT r FROM Restaurant r WHERE r.name = LOWER(:name) AND r.address = LOWER(:address)")
    Optional<Restaurant> findByNameAndAddress(String name, String address);

    default Restaurant getExistedByName(String name) {
        return findByName(name).orElseThrow(() -> new NotFoundException("Restaurant with name=" + name + " not found"));
    }

    default Restaurant prepareAndSave(Restaurant restaurant) {
        restaurant.setName(restaurant.getName().toLowerCase());
        restaurant.setAddress(restaurant.getAddress().toLowerCase());
        return save(restaurant);
    }

    default List<RestaurantTo> getAll() {
        return findAll(Sort.by(Sort.Direction.ASC, "name")).stream().
                map(r -> new RestaurantTo(r.getId(), r.getName(), r.getAddress())).collect(Collectors.toList());
    }
}
