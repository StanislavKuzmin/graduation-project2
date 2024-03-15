package com.github.kuzmin.web.vote;

import com.github.kuzmin.web.user.AdminUserController;
import com.github.kuzmin.web.user.UserTestData;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.github.kuzmin.web.AbstractControllerTest;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.github.kuzmin.web.vote.VoteTestData.*;

class AdminVoteControllerTest extends AbstractControllerTest {

    private final String REST_URL_SLASH = AdminUserController.REST_URL + '/';

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void getVoteToday() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + UserTestData.ANOTHER_USER_ID + "/votes/today"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_TO_MATCHER.contentJson(voteTo6));
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void getVoteHistory() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + UserTestData.ANOTHER_USER_ID + "/votes/history")
                .param("startDate", "2024-01-29").param("endDate", "2024-01-30"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_TO_MATCHER.contentJson(voteTo8, voteTo7));
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void getNotVoteToday() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + UserTestData.USER_ID + "/votes/today"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void getVoteHistoryWithoutStart() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + UserTestData.ANOTHER_USER_ID + "/votes/history")
                .param("endDate", "2024-01-30"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_TO_MATCHER.contentJson(voteTo8, voteTo7));
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void getVoteTodayForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + UserTestData.ANOTHER_USER_ID + "/votes/today"))
                .andExpect(status().isForbidden());
    }
}