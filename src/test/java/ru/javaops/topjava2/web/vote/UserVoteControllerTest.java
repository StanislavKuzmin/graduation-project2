package ru.javaops.topjava2.web.vote;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javaops.topjava2.web.AbstractControllerTest;
import ru.javaops.topjava2.web.user.ProfileController;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.topjava2.web.user.UserTestData.ANOTHER_USER_MAIL;
import static ru.javaops.topjava2.web.user.UserTestData.USER_MAIL;
import static ru.javaops.topjava2.web.vote.VoteTestData.*;

class UserVoteControllerTest extends AbstractControllerTest {

    private final String REST_URL_SLASH = ProfileController.REST_URL + UserVoteController.REST_URL + '/';

    @Test
    @WithUserDetails(value = ANOTHER_USER_MAIL)
    void getVoteToday() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + "today"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_TO_MATCHER.contentJson(voteTo6));
    }

    @Test
    @WithUserDetails(value = ANOTHER_USER_MAIL)
    void getVoteHistory() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + "history")
                .param("startDate", "2024-01-29").param("endDate", "2024-01-30"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_TO_MATCHER.contentJson(voteTo8, voteTo7));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getNotVoteToday() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + "today"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ANOTHER_USER_MAIL)
    void getVoteHistoryWithoutStart() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + "history")
                .param("endDate", "2024-01-30"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_TO_MATCHER.contentJson(voteTo8, voteTo7));
    }
}