package ru.javaops.topjava2.web.menu;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javaops.topjava2.web.AbstractControllerTest;
import ru.javaops.topjava2.web.restaurant.RestaurantController;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.topjava2.web.menu.MenuTestData.*;
import static ru.javaops.topjava2.web.restaurant.RestaurantTestData.RESTAURANT2_ID;
import static ru.javaops.topjava2.web.user.UserTestData.USER_MAIL;

class MenuControllerTest extends AbstractControllerTest {

    private static final String REST_URL_SLASH = RestaurantController.REST_URL + '/' + RESTAURANT2_ID + "/menu" + '/';

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getTodayMenu() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + "today"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_TO_MATCHER.contentJson(menuTo1, menuTo2));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getHistoryMenu() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + "history")
                .param("date", "2024-01-30"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_TO_MATCHER.contentJson(menuTo3, menuTo4));
    }

    @Test
    void getTodayMenuUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + "today"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}