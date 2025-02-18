package com.github.kuzmin.web.dish;

import com.github.kuzmin.web.AbstractControllerTest;
import com.github.kuzmin.web.restaurant.RestaurantController;
import com.github.kuzmin.web.restaurant.RestaurantTestData;
import com.github.kuzmin.web.user.UserTestData;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.github.kuzmin.to.DishTo.fromDish;
import static com.github.kuzmin.web.dish.DishTestData.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DishControllerTest extends AbstractControllerTest {

    private static final String REST_URL_SLASH = RestaurantController.REST_URL + '/' + RestaurantTestData.RESTAURANT1_ID + "/dishes";

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_TO_MATCHER.contentJson(fromDish(dish3_rest1), fromDish(dish1_rest1), fromDish(dish2_rest1)));
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + '/' + DISH1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_TO_MATCHER.contentJson(fromDish(dish1_rest1)));
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + '/' + NOT_FOUND))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}