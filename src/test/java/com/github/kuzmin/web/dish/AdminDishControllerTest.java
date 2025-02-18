package com.github.kuzmin.web.dish;

import com.github.kuzmin.model.Dish;
import com.github.kuzmin.repository.DishRepository;
import com.github.kuzmin.util.JsonUtil;
import com.github.kuzmin.web.AbstractControllerTest;
import com.github.kuzmin.web.restaurant.AdminRestaurantController;
import com.github.kuzmin.web.user.UserTestData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.github.kuzmin.web.restaurant.RestaurantTestData.RESTAURANT1_ID;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminDishControllerTest extends AbstractControllerTest {

    private static final String REST_URL_SLASH = AdminRestaurantController.REST_URL + '/' + RESTAURANT1_ID + "/dishes" + '/';
    private static final String REST_URL = AdminRestaurantController.REST_URL + '/' + RESTAURANT1_ID + "/dishes";

    @Autowired
    private DishRepository repository;

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void excludeFromMenu() throws Exception {
        Dish excluded = DishTestData.getExcluded();
        perform(MockMvcRequestBuilders.patch(REST_URL_SLASH + DishTestData.DISH1_ID)
                .param("isExcludeFromMenu", "true"))
                .andDo(print())
                .andExpect(status().isNoContent());
        DishTestData.DISH_MATCHER.assertMatch(repository.findById(DishTestData.DISH1_ID).get(), excluded);
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void createWithLocation() throws Exception {
        ResultActions actions = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(DishTestData.getNew())))
                .andDo(print())
                .andExpect(status().isCreated());
        Dish created = DishTestData.DISH_MATCHER.readFromJson(actions);
        int newId = created.id();
        DishTestData.DISH_MATCHER.assertMatch(repository.get(newId, RESTAURANT1_ID), created);
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void createWithLocationDuplicate() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(DishTestData.getDuplicate())))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void createWithLocationNotValid() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(DishTestData.getNewNotValid())))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }
}