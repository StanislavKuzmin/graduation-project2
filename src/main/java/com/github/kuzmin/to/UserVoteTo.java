package com.github.kuzmin.to;

import com.github.kuzmin.model.Vote;

import java.time.LocalDate;

import static com.github.kuzmin.util.UsersUtil.createToWithoutPassword;

public record UserVoteTo(UserTo userTo, RestaurantTo restaurantTo, LocalDate date) {
    public static UserVoteTo fromEntity(Vote vote) {
        return new UserVoteTo(createToWithoutPassword(vote.getUser()), RestaurantTo.fromEntity(vote.getRestaurant()), vote.getVoteDate());
    }
}
