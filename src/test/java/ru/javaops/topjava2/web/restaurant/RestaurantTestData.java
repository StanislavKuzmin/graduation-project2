package ru.javaops.topjava2.web.restaurant;

import ru.javaops.topjava2.model.Restaurant;
import ru.javaops.topjava2.web.MatcherFactory;

public class RestaurantTestData {

    public static final MatcherFactory.Matcher<Restaurant> RESTAURANT_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Restaurant.class, "dishes");
    public static final int RESTAURANT1_ID = 5;
    public static final int RESTAURANT2_ID = 6;
    public static final int RESTAURANT3_ID = 7;
    public static final int NOT_FOUND = 100;
    public static final Restaurant restaurant1 = new Restaurant(RESTAURANT1_ID, "Kwakin", "Zvenigorodskaya street, 2/44");
    public static final Restaurant restaurant2 = new Restaurant(RESTAURANT2_ID, "Market_Place", "Nevskii avenue, 22");
    public static final Restaurant restaurant3 = new Restaurant(RESTAURANT3_ID, "Dzamiko", "Admiralteiskaya street, 2");

    public static Restaurant getNew() {
        return new Restaurant(null, "Cucumber", "Cosmonavtov avenue, 2");
    }

    public static Restaurant getUpdated() {
        return new Restaurant(RESTAURANT3_ID, "Dzamiko", "Nevskii avenue, 33");
    }
}
