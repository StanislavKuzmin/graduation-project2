package com.github.kuzmin.to;

import com.github.kuzmin.model.Vote;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record VoteTo(@NotNull LocalDate voteDate, @NotNull Integer restaurantId) {
    public static VoteTo fromEntity(Vote vote) {
        return new VoteTo(vote.getVoteDate(), vote.getRestaurant().getId());
    }
}
