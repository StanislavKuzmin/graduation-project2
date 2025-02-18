package com.github.kuzmin.web.menu;

import com.github.kuzmin.web.restaurant.RestaurantController;
import com.github.kuzmin.web.user.UserTestData;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.github.kuzmin.web.AbstractControllerTest;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.github.kuzmin.web.menu.MenuTestData.*;
import static com.github.kuzmin.web.restaurant.RestaurantTestData.RESTAURANT2_ID;

class MenuControllerTest extends AbstractControllerTest {

    private static final String REST_URL_SLASH = RestaurantController.REST_URL + '/' + RESTAURANT2_ID + "/menu" + '/';

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void getTodayRestaurantMenu() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + "today"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_TO_MATCHER.contentJson(menuTo2));
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void getTodayAllMenu() throws Exception {
        perform(MockMvcRequestBuilders.get(RestaurantController.REST_URL + "/menu" + '/' + "today"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_TO_MATCHER.contentJson(menuTo1, menuTo2, menuTo3));
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void getRestaurantMenuByDate() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + "by-date")
                .param("date", "2024-01-30"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_TO_MATCHER.contentJson(byDateMenuTo2));
    }

    @Test
    void getTodayMenuUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + "today"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}