package com.github.kuzmin.service;

import com.github.kuzmin.to.RestaurantVoteTo;
import com.github.kuzmin.to.UserVoteTo;
import com.github.kuzmin.to.VoteTo;

import java.time.LocalDate;
import java.util.List;

public interface VoteService {
    List<RestaurantVoteTo> getAllVoteForRestaurantsByDate(LocalDate date);
    RestaurantVoteTo getVoteForRestaurantByDate(LocalDate date, int restaurantId);
    VoteTo vote(int restaurantId, int userId);
    VoteTo changeVote(int restaurantId, int userId);
    UserVoteTo getUserVoteByDate(LocalDate date, int userId);
}
