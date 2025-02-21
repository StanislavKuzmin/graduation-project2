package com.github.kuzmin.web.vote;

import com.github.kuzmin.model.Restaurant;
import com.github.kuzmin.model.Vote;
import com.github.kuzmin.to.RestaurantTo;
import com.github.kuzmin.to.RestaurantVoteTo;
import com.github.kuzmin.to.UserVoteTo;
import com.github.kuzmin.to.VoteTo;
import com.github.kuzmin.util.UsersUtil;
import com.github.kuzmin.web.MatcherFactory;
import com.github.kuzmin.web.restaurant.RestaurantTestData;
import com.github.kuzmin.web.user.UserTestData;

import java.time.LocalDate;

public class VoteTestData {
    public static final MatcherFactory.Matcher<VoteTo> VOTE_TO_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(VoteTo.class);
    public static final MatcherFactory.Matcher<RestaurantVoteTo> RESTAURANT_VOTE_TO_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(RestaurantVoteTo.class);
    public static final MatcherFactory.Matcher<UserVoteTo> USER_VOTE_TO_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(UserVoteTo.class);
    public static final UserVoteTo anotherUserVoteToNow = new UserVoteTo(UsersUtil.createToWithoutPassword(UserTestData.another_user), RestaurantTo.fromEntity(RestaurantTestData.restaurant3), LocalDate.now());
    public static final UserVoteTo anotherUserVoteToPast = new UserVoteTo(UsersUtil.createToWithoutPassword(UserTestData.another_user), RestaurantTo.fromEntity(RestaurantTestData.restaurant1), LocalDate.of(2024, 1, 29));

    public static VoteTo getEarlyVote(int restaurantId) {
        return new VoteTo(LocalDate.of(2024, 1, 31), restaurantId);
    }

    public static Vote getChangeVote() {
        return new Vote(30, UserTestData.user, RestaurantTestData.restaurant2, LocalDate.of(2024, 1, 30));
    }

    public static RestaurantVoteTo getRestaurantVoteTo(LocalDate date, Restaurant restaurant, Long votesNumber) {
        return new RestaurantVoteTo(RestaurantTo.fromEntity(restaurant), votesNumber, date);
    }
}
