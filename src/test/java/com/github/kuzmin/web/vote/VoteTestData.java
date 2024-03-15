package com.github.kuzmin.web.vote;

import com.github.kuzmin.model.Vote;
import com.github.kuzmin.to.VoteTo;
import com.github.kuzmin.web.restaurant.RestaurantTestData;
import com.github.kuzmin.web.user.UserTestData;
import com.github.kuzmin.web.MatcherFactory;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class VoteTestData {
    public static final MatcherFactory.Matcher<Vote> VOTE_MATCHER = MatcherFactory.usingAssertions(Vote.class,
            (a, e) -> assertThat(a).usingRecursiveComparison().ignoringFields("restaurant.dishes").isEqualTo(e),
            (a, e) -> {
                throw new UnsupportedOperationException();
            });
    public static final MatcherFactory.Matcher<VoteTo> VOTE_TO_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(VoteTo.class);
    public static final VoteTo voteTo1 = new VoteTo(LocalDate.of(2024, 1, 30), RestaurantTestData.restaurant1);
    public static final VoteTo voteTo2 = new VoteTo(LocalDate.of(2024, 1, 30), 2L, RestaurantTestData.restaurant2);
    public static final VoteTo voteTo3 = new VoteTo(LocalDate.of(2024, 1, 30), 1L, RestaurantTestData.restaurant3);
    public static final VoteTo voteTo4 = new VoteTo(LocalDate.of(2024, 1, 29), 1L, RestaurantTestData.restaurant2);
    public static final VoteTo voteTo5 = new VoteTo(LocalDate.of(2024, 1, 30), 2L, RestaurantTestData.restaurant2);
    public static final VoteTo voteTo6 = new VoteTo(LocalDate.now(), UserTestData.another_user, RestaurantTestData.restaurant3);
    public static final VoteTo voteTo7 = new VoteTo(LocalDate.of(2024, 1, 29), UserTestData.another_user, RestaurantTestData.restaurant1);
    public static final VoteTo voteTo8 = new VoteTo(LocalDate.of(2024, 1, 30), UserTestData.another_user, RestaurantTestData.restaurant3);

    public static Vote getNew() {
        Vote vote = new Vote(UserTestData.USER_ID, LocalDate.of(2024, 1, 31));
        vote.setRestaurant(RestaurantTestData.restaurant1);
        return vote;
    }

    public static Vote getEarlyVote() {
        Vote vote = new Vote(UserTestData.USER_ID, LocalDate.of(2024, 1, 30));
        vote.setRestaurant(RestaurantTestData.restaurant2);
        vote.setUser(UserTestData.user);
        return vote;
    }
}
