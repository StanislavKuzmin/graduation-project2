package com.github.kuzmin.web.vote;

import com.github.kuzmin.config.TimeProvider;
import com.github.kuzmin.model.Vote;
import com.github.kuzmin.repository.VoteRepository;
import com.github.kuzmin.to.VoteTo;
import com.github.kuzmin.util.JsonUtil;
import com.github.kuzmin.web.AbstractControllerTest;
import com.github.kuzmin.web.user.UserTestData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.threeten.extra.MutableClock;

import java.time.*;
import java.time.temporal.ChronoUnit;

import static com.github.kuzmin.web.restaurant.RestaurantTestData.*;
import static com.github.kuzmin.web.vote.VoteTestData.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = VoteControllerTest.CustomClockConfiguration.class)
class VoteControllerTest extends AbstractControllerTest {
    private static final LocalDateTime TEST_DATE_TIME = LocalDateTime.of(2024, 1, 30, 10, 0);
    private static final String REST_URL_SLASH = VoteController.REST_URL + '/';
    private static final String REST_URL = VoteController.REST_URL;
    @Autowired
    private VoteRepository repository;

    @Autowired
    private Clock clock;
    @Autowired
    private MutableClock mutableClock;
    @Autowired
    private TimeProvider timeProvider;

    @AfterEach
    void resetClock() {
        mutableClock.setInstant(getTestInstant());
    }

    @Test
    @WithUserDetails(value = UserTestData.ANOTHER_USER_MAIL)
    void voteFirstTime() throws Exception {
        mutableClock.add(1L, ChronoUnit.DAYS);
        ResultActions actions = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(VoteTestData.getEarlyVote(restaurant1.getId()))))
                .andDo(print())
                .andExpect(status().isOk());
        VoteTo created = VOTE_TO_MATCHER.readFromJson(actions);
        VOTE_TO_MATCHER.assertMatch(VoteTo.fromEntity(repository.getUserVoteByDate(timeProvider.getCurrentDate(), UserTestData.ANOTHER_USER_ID).get()), created);
    }

    @Test
    void getAllVoteToday() throws Exception {
        LocalDate date = timeProvider.getCurrentDate();
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + "today"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_VOTE_TO_MATCHER.contentJson(getRestaurantVoteTo(date, restaurant3, 1L),
                        getRestaurantVoteTo(date, restaurant1, 0L),
                        getRestaurantVoteTo(date, restaurant2, 2L)));
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void getVoteHistory() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + RESTAURANT2_ID + "/history")
                .param("date", "2024-01-29"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_VOTE_TO_MATCHER.contentJson(getRestaurantVoteTo(LocalDate.of(2024, 1, 29), restaurant2, 1L)));
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void changeVoteBeforeDeadLine() throws Exception {
        Vote changeVote = getChangeVote();
        changeVote.setRestaurant(restaurant1);
        ResultActions actions = perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(VoteTo.fromEntity(changeVote))))
                .andDo(print())
                .andExpect(status().isOk());
        VoteTo created = VOTE_TO_MATCHER.readFromJson(actions);
        VOTE_TO_MATCHER.assertMatch(created, VoteTo.fromEntity(changeVote));
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void changeVoteForTomorrowVote() throws Exception {
        Vote changeVote = getChangeVote();
        changeVote.setVoteDate(LocalDate.of(2024, 1, 31));
        changeVote.setRestaurant(restaurant1);
        ResultActions actions = perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(VoteTo.fromEntity(changeVote))))
                .andDo(print())
                .andExpect(status().isOk());
        VoteTo created = VOTE_TO_MATCHER.readFromJson(actions);
        VOTE_TO_MATCHER.assertMatch(created, VoteTo.fromEntity(changeVote));
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void voteAfterDeadLine() throws Exception {
        mutableClock.add(3L, ChronoUnit.HOURS);
        Vote changeVote = getChangeVote();
        changeVote.setRestaurant(restaurant1);
        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(VoteTo.fromEntity(changeVote))))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    @WithUserDetails(value = UserTestData.GUEST_MAIL)
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void voteNoRoleForbidden() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(VoteTestData.getEarlyVote(restaurant1.getId()))))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    static Instant getTestInstant() {
        return TEST_DATE_TIME.toInstant(ZoneOffset.UTC);
    }

    @TestConfiguration
    static class CustomClockConfiguration {

        @Bean
        public MutableClock mutableClock() {
            return MutableClock.of(getTestInstant(), ZoneOffset.UTC);
        }

        @Bean
        @Primary
        public Clock fixedClock(@NonNull final MutableClock mutableClock) {
            return mutableClock;
        }
    }
}