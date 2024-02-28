package ru.javaops.topjava2.web.restaurant;

import ru.javaops.topjava2.model.Restaurant;
import ru.javaops.topjava2.to.RestaurantTo;
import ru.javaops.topjava2.web.MatcherFactory;

import java.util.List;

import static ru.javaops.topjava2.web.dish.DishTestData.*;
import static org.assertj.core.api.Assertions.assertThat;

public class RestaurantTestData {

    public static final MatcherFactory.Matcher<Restaurant> RESTAURANT_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Restaurant.class, "dishes");
    public static final MatcherFactory.Matcher<Restaurant> RESTAURANT_MATCHER_WITH_DISHES = MatcherFactory.usingAssertions(Restaurant.class,
            (a, e) -> assertThat(a).usingRecursiveComparison().ignoringFields("dishes.restaurantId").isEqualTo(e),
            (a, e) -> {
                throw new UnsupportedOperationException();
            });
    public static final MatcherFactory.Matcher<RestaurantTo> RESTAURANT_TO_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(RestaurantTo.class);
    public static final int RESTAURANT1_ID = 5;
    public static final int RESTAURANT2_ID = 6;
    public static final int RESTAURANT3_ID = 7;
    public static final int NOT_FOUND = 100;
    public static final Restaurant restaurant1 = new Restaurant(RESTAURANT1_ID, "kwakin", "zvenigorodskaya street, 2/44");
    public static final Restaurant restaurant2 = new Restaurant(RESTAURANT2_ID, "market_place", "nevskii avenue, 22");
    public static final Restaurant restaurant3 = new Restaurant(RESTAURANT3_ID, "dzamiko", "admiralteiskaya street, 2");

    static {
        restaurant1.setDishes(List.of(dish1, dish2, dish3));
    }

    public static Restaurant getNew() {
        return new Restaurant(null, "cucumber", "cosmonavtov avenue, 2");
    }

    public static Restaurant getUpdated() {
        return new Restaurant(RESTAURANT3_ID, "dzamiko", "nevskii avenue, 33");
    }
}
