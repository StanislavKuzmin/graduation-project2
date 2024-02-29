package ru.javaops.topjava2.web.vote;

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
import ru.javaops.topjava2.model.Vote;
import ru.javaops.topjava2.repository.VoteRepository;
import ru.javaops.topjava2.web.AbstractControllerTest;
import ru.javaops.topjava2.web.restaurant.RestaurantController;

import java.time.*;
import java.time.temporal.ChronoUnit;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.topjava2.util.VoteUtil.createTo;
import static ru.javaops.topjava2.web.restaurant.RestaurantTestData.*;
import static ru.javaops.topjava2.web.user.UserTestData.*;
import static ru.javaops.topjava2.web.vote.VoteTestData.*;

@ContextConfiguration(classes = RestaurantVoteControllerTest.CustomClockConfiguration.class)
class RestaurantVoteControllerTest extends AbstractControllerTest {
    private static final LocalDateTime TEST_DATE_TIME = LocalDateTime.of(2024, 1, 30, 10, 0);
    private static final String REST_URL_SLASH = RestaurantController.REST_URL + RestaurantVoteController.REST_URL + '/';
    private static final String REST_URL = RestaurantController.REST_URL + RestaurantVoteController.REST_URL;
    @Autowired
    private VoteRepository repository;

    @Autowired
    private Clock clock;
    @Autowired
    private MutableClock mutableClock;

    @AfterEach
    void resetClock() {
        mutableClock.setInstant(getTestInstant());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void voteFirstTime() throws Exception {
        mutableClock.add(1L, ChronoUnit.DAYS);
        Vote newVote = VoteTestData.getNew();
        ResultActions actions = perform(MockMvcRequestBuilders.post(REST_URL)
                .param("restaurantId", Integer.toString(RESTAURANT1_ID)))
                .andDo(print())
                .andExpect(status().isOk());
        Vote created = VOTE_MATCHER.readFromJson(actions);
        VOTE_MATCHER.assertMatch(created, newVote);
        created.setUser(user);
        VOTE_TO_MATCHER.assertMatch(repository.getUserVoteToday(created.getId()), createTo(created));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getAllVoteToday() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + "today"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_TO_MATCHER.contentJson(voteTo3, voteTo1, voteTo2));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getVoteHistory() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + RESTAURANT2_ID + "/history")
                .param("startDate", "2024-01-29").param("endDate", "2024-01-30"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_TO_MATCHER.contentJson(voteTo5, voteTo4));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void changeVoteBeforeDeadLine() throws Exception {
        Vote earlyVote = getEarlyVote();
        VOTE_TO_MATCHER.assertMatch(repository.getUserVoteToday(earlyVote.getId()), createTo(earlyVote));
        Vote changeVote = new Vote(USER_ID, LocalDate.of(2024, 1, 30));
        changeVote.setRestaurant(restaurant1);
        ResultActions actions = perform(MockMvcRequestBuilders.post(REST_URL)
                .param("restaurantId", Integer.toString(RESTAURANT1_ID)))
                .andDo(print())
                .andExpect(status().isOk());
        Vote created = VOTE_MATCHER.readFromJson(actions);
        VOTE_MATCHER.assertMatch(created, changeVote);
        created.setUser(user);
        VOTE_TO_MATCHER.assertMatch(repository.getUserVoteToday(created.getId()), createTo(created));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void voteAfterDeadLine() throws Exception {
        mutableClock.add(3L, ChronoUnit.HOURS);
        perform(MockMvcRequestBuilders.post(REST_URL)
                .param("restaurantId", Integer.toString(RESTAURANT1_ID)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getVoteHistoryWithoutStart() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + RESTAURANT2_ID + "/history")
                .param("endDate", "2024-01-30"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_TO_MATCHER.contentJson(voteTo5, voteTo4));
    }

    @Test
    @WithUserDetails(value = GUEST_MAIL)
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void voteFirstTimeForbidden() throws Exception {
        mutableClock.add(1L, ChronoUnit.DAYS);
        perform(MockMvcRequestBuilders.post(REST_URL)
                .param("restaurantId", Integer.toString(RESTAURANT1_ID)))
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