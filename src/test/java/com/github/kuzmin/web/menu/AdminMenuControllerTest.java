package com.github.kuzmin.web.menu;

import com.github.kuzmin.repository.MenuRepository;
import com.github.kuzmin.web.AbstractControllerTest;
import com.github.kuzmin.web.dish.DishTestData;
import com.github.kuzmin.web.restaurant.AdminRestaurantController;
import com.github.kuzmin.web.user.UserTestData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static com.github.kuzmin.web.dish.DishTestData.dish1_rest1;
import static com.github.kuzmin.web.dish.DishTestData.dish2_rest1;
import static com.github.kuzmin.web.restaurant.RestaurantTestData.RESTAURANT1_ID;
import static com.github.kuzmin.web.restaurant.RestaurantTestData.RESTAURANT2_ID;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminMenuControllerTest extends AbstractControllerTest {

    @Autowired
    MenuRepository repository;
    private static final String REST_URL = AdminRestaurantController.REST_URL + '/' + RESTAURANT1_ID + "/menu";

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void addToTodayMenu() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL)
                .param("dishId", dish2_rest1.getId().toString())
                .param("date", LocalDate.now().toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MenuTestData.MENU_MATCHER.contentJson(MenuTestData.MENU_ITEM_2));
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void deleteFromTodayMenu() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL)
                .param("dishId", dish1_rest1.getId().toString())
                .param("date", LocalDate.now().toString()))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertFalse(repository.existsByMenu(dish1_rest1.getId(), LocalDate.now(), RESTAURANT2_ID));
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void addToTodayMenuDuplicate() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL)
                .param("dishId", dish1_rest1.getId().toString())
                .param("date", LocalDate.now().toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void addToTodayMenuNotFound() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL)
                .param("dishId", String.valueOf(DishTestData.NOT_FOUND))
                .param("date", LocalDate.now().toString()))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void deleteFromTodayMenuNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL)
                .param("dishId", String.valueOf(dish2_rest1.id()))
                .param("date", LocalDate.now().toString()))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void addToTodayMenuForbidden() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL)
                .param("dishId", dish2_rest1.getId().toString())
                .param("date", LocalDate.now().toString()))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void deleteFromTodayMenuForbidden() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL)
                .param("dishId", dish1_rest1.getId().toString())
                .param("date", LocalDate.now().toString()))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}