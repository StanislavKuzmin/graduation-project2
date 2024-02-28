package ru.javaops.topjava2.to;

import ru.javaops.topjava2.model.Restaurant;
import ru.javaops.topjava2.model.User;

import java.time.LocalDate;
import java.util.Objects;

import static ru.javaops.topjava2.util.RestaurantUtil.createTo;
import static ru.javaops.topjava2.util.UsersUtil.createToWithoutPassword;

public record VoteTo(LocalDate voteDate, Long votesNumber, UserTo userTo, RestaurantTo restaurantTo) {

    public VoteTo(LocalDate voteDate, User user, Restaurant restaurant) {
        this(voteDate, 0L, createToWithoutPassword(user), createTo(restaurant));
    }

    public VoteTo(LocalDate voteDate, Long votesNumber, Restaurant restaurant) {
        this(voteDate, votesNumber, null, createTo(restaurant));
    }

    public VoteTo(LocalDate voteDate, Restaurant restaurant) {
        this(voteDate, 0L, null, createTo(restaurant));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VoteTo voteTo = (VoteTo) o;
        return Objects.equals(voteDate, voteTo.voteDate) && Objects.equals(restaurantTo, voteTo.restaurantTo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(voteDate, restaurantTo);
    }
}
