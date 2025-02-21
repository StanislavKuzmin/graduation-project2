package com.github.kuzmin.web.vote;

import com.github.kuzmin.web.AbstractControllerTest;
import com.github.kuzmin.web.user.ProfileController;
import com.github.kuzmin.web.user.UserTestData;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.github.kuzmin.web.vote.VoteTestData.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserVoteControllerTest extends AbstractControllerTest {

    private final String REST_URL_SLASH = ProfileController.REST_URL + UserVoteController.REST_URL + '/';

    @Test
    @WithUserDetails(value = UserTestData.ANOTHER_USER_MAIL)
    void getVoteToday() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + "today"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_VOTE_TO_MATCHER.contentJson(anotherUserVoteToNow));
    }

    @Test
    @WithUserDetails(value = UserTestData.ANOTHER_USER_MAIL)
    void getVoteHistory() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + "history")
                .param("date", "2024-01-29"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_VOTE_TO_MATCHER.contentJson(anotherUserVoteToPast));
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void getNotVoteToday() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + "today"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().string(""));
    }
}