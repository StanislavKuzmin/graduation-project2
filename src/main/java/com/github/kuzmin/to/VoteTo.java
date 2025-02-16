package com.github.kuzmin.to;

import com.github.kuzmin.model.Vote;

import java.time.LocalDate;

import static com.github.kuzmin.util.UsersUtil.createToWithoutPassword;

public record VoteTo(LocalDate voteDate, UserTo userTo, RestaurantTo restaurantTo) {
    public static VoteTo fromEntity(Vote vote) {
        return new VoteTo(vote.getVoteDate(), createToWithoutPassword(vote.getUser()), RestaurantTo.fromEntity(vote.getRestaurant()));
    }
}
