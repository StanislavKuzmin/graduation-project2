package com.github.kuzmin.repository;

import com.github.kuzmin.model.Vote;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface VoteRepository extends BaseRepository<Vote> {

    @Query("SELECT v FROM Vote v WHERE v.user.id = :userId AND v.voteDate = :date")
    Optional<Vote> getUserVoteByDate(@Param("date") LocalDate date,
                           @Param("userId") int userId);

    @Query("SELECT v FROM Vote v WHERE v.voteDate = :date")
    List<Vote> getRestaurantsVotesByDate(@Param("date") LocalDate date);

    @Query("SELECT v FROM Vote v WHERE v.restaurant.id = :restaurantId AND v.voteDate = :date")
    List<Vote> getRestaurantVotesByDate(@Param("restaurantId") int restaurantId, @Param("date") LocalDate date);
}
