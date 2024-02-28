package ru.javaops.topjava2.web.menu;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javaops.topjava2.repository.MenuRepository;
import ru.javaops.topjava2.web.AbstractControllerTest;
import ru.javaops.topjava2.web.restaurant.AdminRestaurantController;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.topjava2.web.dish.DishTestData.*;
import static ru.javaops.topjava2.web.menu.MenuTestData.*;
import static ru.javaops.topjava2.web.restaurant.RestaurantTestData.RESTAURANT1_ID;
import static ru.javaops.topjava2.web.user.UserTestData.ADMIN_MAIL;
import static ru.javaops.topjava2.web.user.UserTestData.USER_MAIL;

class AdminMenuControllerTest extends AbstractControllerTest {

    @Autowired
    MenuRepository repository;
    private static final String REST_URL_SLASH = AdminRestaurantController.REST_URL + '/' + RESTAURANT1_ID + "/menu" + '/';
    private static final String REST_URL = AdminRestaurantController.REST_URL + '/' + RESTAURANT1_ID + "/menu";

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void addToTodayMenu() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL_SLASH + "add")
                .param("dishesIndex[]", indexes))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_MATCHER.contentJson(menu2, menu3));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteFromTodayMenu() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL)
                .param("id", String.valueOf(DISH1_ID)))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertFalse(repository.existsByMenuId(menu1.getId()));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void addToTodayMenuNotFound() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL_SLASH + "add")
                .param("dishesIndex[]", String.valueOf(NOT_FOUND)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteFromTodayMenuNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL)
                .param("id", String.valueOf(dish2.id())))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void addToMenuFromDate() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL_SLASH + "add-from-date")
                .param("date", "2024-01-30"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_MATCHER.contentJson(menu3, menu2));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void addToMenuFromDateNotFound() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL_SLASH + "add-from-date")
                .param("date", "2024-01-31"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void addToTodayMenuForbidden() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL_SLASH + "add")
                .param("dishesIndex[]", indexes))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void deleteFromTodayMenuForbidden() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL)
                .param("id", String.valueOf(DISH1_ID)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}