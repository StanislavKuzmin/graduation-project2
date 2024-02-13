package ru.javaops.topjava2.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.topjava2.error.NotFoundException;
import ru.javaops.topjava2.model.Vote;
import ru.javaops.topjava2.model.VoteId;
import ru.javaops.topjava2.to.VoteTo;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Transactional(readOnly = true)
public interface VoteRepository extends JpaRepository<Vote, VoteId> {

    default Vote get(VoteId id) {
        return findById(id).filter(vote -> Objects.equals(vote.getId().getUserId(), id.getUserId())).orElse(null);
    }

    @EntityGraph(attributePaths = {"restaurant"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT new ru.javaops.topjava2.to.VoteTo(v.id.voteDate, count(v.restaurant.id), v.restaurant)" +
                 " FROM Vote v WHERE v.restaurant.id = :restaurantId AND v.id.voteDate >= :startDate AND v.id.voteDate <= :endDate" +
                 " GROUP BY v.id.voteDate, v.restaurant" +
                 " ORDER BY v.id.voteDate DESC")
    List<VoteTo> getRestaurantHistoryBetweenOpen(@Param("startDate") LocalDate startDate,
                                                       @Param("endDate") LocalDate endDate,
                                                       @Param("restaurantId") int restaurantId);

    @EntityGraph(attributePaths = {"restaurant"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT new ru.javaops.topjava2.to.VoteTo(v.id.voteDate, v.restaurant)" +
            " FROM Vote v WHERE v.id.userId = :userId AND v.id.voteDate >= :startDate AND v.id.voteDate <= :endDate" +
            " GROUP BY v.id.voteDate, v.restaurant" +
            " ORDER BY v.id.voteDate DESC")
    List<VoteTo> getUserVoteHistoryBetweenOpen(@Param("startDate") LocalDate startDate,
                                               @Param("endDate") LocalDate endDate,
                                               @Param("userId") int userId);

    @EntityGraph(attributePaths = {"restaurant"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT new ru.javaops.topjava2.to.VoteTo(v.id.voteDate, count(v.restaurant.id), v.restaurant)" +
            " FROM Vote v WHERE v.id.voteDate = current_date" +
            " GROUP BY v.restaurant")
    List<VoteTo> getRestaurantsToday();

    @EntityGraph(attributePaths = {"restaurant"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT new ru.javaops.topjava2.to.VoteTo(v.id.voteDate, v.restaurant)" +
            " FROM Vote v WHERE v.id = :id" +
            " GROUP BY v.restaurant")
    Optional<VoteTo> findUserVoteToday(@Param("id") VoteId id);

    default VoteTo getUserVoteToday(VoteId id) {
        return findUserVoteToday(id).orElseThrow(() -> new NotFoundException("The user has not voted yet"));
    }

}
