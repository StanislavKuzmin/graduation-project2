package com.github.kuzmin.repository;

import com.github.kuzmin.error.NotFoundException;
import com.github.kuzmin.model.Vote;
import com.github.kuzmin.to.VoteTo;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface VoteRepository extends JpaRepository<Vote, Integer> {

    default Vote get(Integer id) {
        return findById(id).orElse(null);
    }

    @EntityGraph(attributePaths = {"restaurant"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT v FROM Vote v WHERE v.restaurant.id = :restaurantId AND v.voteDate >= :startDate AND v.voteDate <= :endDate" +
                 " ORDER BY v.voteDate DESC")
    List<Vote> getRestaurantHistoryBetweenOpen(@Param("startDate") LocalDate startDate,
                                                 @Param("endDate") LocalDate endDate,
                                                 @Param("restaurantId") int restaurantId);

    @EntityGraph(attributePaths = {"user", "restaurant"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT v FROM Vote v WHERE v.user.id = :userId AND v.voteDate >= :startDate AND v.voteDate <= :endDate" +
            " ORDER BY v.voteDate DESC")
    List<VoteTo> getUserVoteHistoryBetweenOpen(@Param("startDate") LocalDate startDate,
                                               @Param("endDate") LocalDate endDate,
                                               @Param("userId") int userId);

    @EntityGraph(attributePaths = {"restaurant"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT v FROM Vote v WHERE v.voteDate = current_date ")
    List<Vote> getRestaurantsToday();

    @EntityGraph(attributePaths = {"restaurant"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT v FROM Vote v WHERE v.restaurant.id = :restaurantId AND v.voteDate = current_date ")
    List<Vote> getRestaurantToday(@Param("restaurantId") int restaurantId);

    @EntityGraph(attributePaths = {"user", "restaurant"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT v FROM Vote v WHERE v.user.id = :userId")
    Optional<Vote> findUserVoteToday(@Param("userId") Integer userId);

    default Vote getUserVoteToday(Integer userId) {
        return findUserVoteToday(userId).orElseThrow(() -> new NotFoundException("The user has not voted yet"));
    }

}
