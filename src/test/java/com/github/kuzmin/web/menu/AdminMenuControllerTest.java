package com.github.kuzmin.web.menu;

import com.github.kuzmin.web.restaurant.AdminRestaurantController;
import com.github.kuzmin.web.user.UserTestData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.github.kuzmin.repository.MenuRepository;
import com.github.kuzmin.web.AbstractControllerTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.github.kuzmin.web.dish.DishTestData.*;
import static com.github.kuzmin.web.restaurant.RestaurantTestData.RESTAURANT1_ID;

class AdminMenuControllerTest extends AbstractControllerTest {

    @Autowired
    MenuRepository repository;
    private static final String REST_URL_SLASH = AdminRestaurantController.REST_URL + '/' + RESTAURANT1_ID + "/menu" + '/';
    private static final String REST_URL = AdminRestaurantController.REST_URL + '/' + RESTAURANT1_ID + "/menu";

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void addToTodayMenu() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL_SLASH + "add")
                .param("dishesIndex[]", MenuTestData.indexes))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MenuTestData.MENU_MATCHER.contentJson(MenuTestData.menu2, MenuTestData.menu3));
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void deleteFromTodayMenu() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL)
                .param("id", String.valueOf(DISH1_ID)))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertFalse(repository.existsByMenuId(MenuTestData.menu1.getId()));
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void addToTodayMenuDuplicate() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL_SLASH + "add")
                .param("dishesIndex[]", dish1.getId().toString()))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void addToTodayMenuNotFound() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL_SLASH + "add")
                .param("dishesIndex[]", String.valueOf(NOT_FOUND)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void deleteFromTodayMenuNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL)
                .param("id", String.valueOf(dish2.id())))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void addToMenuFromDate() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL_SLASH + "add-from-date")
                .param("date", "2024-01-30"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MenuTestData.MENU_MATCHER.contentJson(MenuTestData.menu3, MenuTestData.menu2));
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void addToMenuFromDateNotFound() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL_SLASH + "add-from-date")
                .param("date", "2024-01-31"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void addToTodayMenuForbidden() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL_SLASH + "add")
                .param("dishesIndex[]", MenuTestData.indexes))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void deleteFromTodayMenuForbidden() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL)
                .param("id", String.valueOf(DISH1_ID)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}