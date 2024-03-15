package com.github.kuzmin.to;

import com.github.kuzmin.model.Restaurant;
import com.github.kuzmin.util.RestaurantUtil;
import com.github.kuzmin.util.UsersUtil;
import com.github.kuzmin.model.User;

import java.time.LocalDate;
import java.util.Objects;

public record VoteTo(LocalDate voteDate, Long votesNumber, UserTo userTo, RestaurantTo restaurantTo) {

    public VoteTo(LocalDate voteDate, User user, Restaurant restaurant) {
        this(voteDate, 0L, UsersUtil.createToWithoutPassword(user), RestaurantUtil.createTo(restaurant));
    }

    public VoteTo(LocalDate voteDate, Long votesNumber, Restaurant restaurant) {
        this(voteDate, votesNumber, null, RestaurantUtil.createTo(restaurant));
    }

    public VoteTo(LocalDate voteDate, Restaurant restaurant) {
        this(voteDate, 0L, null, RestaurantUtil.createTo(restaurant));
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
