package ru.javaops.topjava2.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.topjava2.error.NotFoundException;
import ru.javaops.topjava2.model.Restaurant;
import ru.javaops.topjava2.to.RestaurantTo;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {
    @EntityGraph(attributePaths = {"dishes"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT r FROM Restaurant r WHERE r.id=?1")
    Restaurant getWithDishes(int id);

    @Query("SELECT r FROM Restaurant r WHERE r.name = LOWER(:name)")
    Optional<Restaurant> findByNameIgnoringCase(String name);

    default Restaurant getExistedByName(String name) {
        return findByNameIgnoringCase(name).orElseThrow(() -> new NotFoundException("Restaurant with name=" + name + " not found"));
    }

    default List<RestaurantTo> getAll() {
        return findAll(Sort.by(Sort.Direction.ASC, "name")).stream().
                map(r -> new RestaurantTo(r.getId(), r.getName(), r.getAddress())).collect(Collectors.toList());
    }
}
