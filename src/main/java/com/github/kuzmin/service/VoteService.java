package com.github.kuzmin.service;

import com.github.kuzmin.to.VoteTo;

import java.time.LocalDate;
import java.util.List;

public interface VoteService {

    List<VoteTo> getAllVoteForRestaurantsToday();

    List<VoteTo> getVoteRestaurantHistoryBetweenOpen(LocalDate startDate, LocalDate endDate, Integer restaurantId);

    VoteTo vote(Integer restaurantId);

    VoteTo getUserVoteToday();

}
