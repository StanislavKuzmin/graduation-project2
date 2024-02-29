package ru.javaops.topjava2.web.vote;

import ru.javaops.topjava2.model.Vote;
import ru.javaops.topjava2.to.VoteTo;
import ru.javaops.topjava2.web.MatcherFactory;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javaops.topjava2.web.restaurant.RestaurantTestData.*;
import static ru.javaops.topjava2.web.user.UserTestData.*;

public class VoteTestData {
    public static final MatcherFactory.Matcher<Vote> VOTE_MATCHER = MatcherFactory.usingAssertions(Vote.class,
            (a, e) -> assertThat(a).usingRecursiveComparison().ignoringFields("restaurant.dishes").isEqualTo(e),
            (a, e) -> {
                throw new UnsupportedOperationException();
            });
    public static final MatcherFactory.Matcher<VoteTo> VOTE_TO_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(VoteTo.class);
    public static final VoteTo voteTo1 = new VoteTo(LocalDate.of(2024, 1, 30), restaurant1);
    public static final VoteTo voteTo2 = new VoteTo(LocalDate.of(2024, 1, 30), 2L, restaurant2);
    public static final VoteTo voteTo3 = new VoteTo(LocalDate.of(2024, 1, 30), 1L, restaurant3);
    public static final VoteTo voteTo4 = new VoteTo(LocalDate.of(2024, 1, 29), 1L, restaurant2);
    public static final VoteTo voteTo5 = new VoteTo(LocalDate.of(2024, 1, 30), 2L, restaurant2);
    public static final VoteTo voteTo6 = new VoteTo(LocalDate.now(), another_user, restaurant3);
    public static final VoteTo voteTo7 = new VoteTo(LocalDate.of(2024, 1, 29), another_user, restaurant1);
    public static final VoteTo voteTo8 = new VoteTo(LocalDate.of(2024, 1, 30), another_user, restaurant3);

    public static Vote getNew() {
        Vote vote = new Vote(USER_ID, LocalDate.of(2024, 1, 31));
        vote.setRestaurant(restaurant1);
        return vote;
    }

    public static Vote getEarlyVote() {
        Vote vote = new Vote(USER_ID, LocalDate.of(2024, 1, 30));
        vote.setRestaurant(restaurant2);
        vote.setUser(user);
        return vote;
    }
}
