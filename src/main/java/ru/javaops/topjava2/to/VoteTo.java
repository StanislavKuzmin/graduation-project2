package ru.javaops.topjava2.to;

import ru.javaops.topjava2.model.Restaurant;

import java.time.LocalDate;
import java.util.Objects;

public record VoteTo(LocalDate voteDate, Long votesNumber, Restaurant restaurant) {

    public VoteTo(LocalDate voteDate, Restaurant restaurant) {
        this(voteDate, 0L, restaurant);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VoteTo voteTo = (VoteTo) o;
        return Objects.equals(voteDate, voteTo.voteDate) && Objects.equals(restaurant, voteTo.restaurant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(voteDate, restaurant);
    }
}
