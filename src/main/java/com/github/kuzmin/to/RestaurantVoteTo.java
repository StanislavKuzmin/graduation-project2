package com.github.kuzmin.to;

import com.github.kuzmin.model.Vote;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static com.github.kuzmin.to.RestaurantTo.fromEntity;

public record RestaurantVoteTo(RestaurantTo restaurantTo, Long votesNumber, LocalDate date) {
    public static RestaurantVoteTo fromEntities(List<Vote> votes) {
        return new RestaurantVoteTo(votes.stream().map(vote -> fromEntity(vote.getRestaurant())).findFirst().get(),
                votes.stream().map(Vote::getId).filter(Objects::nonNull).count(),
                votes.stream().map(Vote::getVoteDate).findFirst().get());
    }
}
