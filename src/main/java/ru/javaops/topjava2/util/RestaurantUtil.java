package ru.javaops.topjava2.util;

import lombok.experimental.UtilityClass;
import ru.javaops.topjava2.model.Restaurant;
import ru.javaops.topjava2.to.RestaurantTo;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@UtilityClass
public class RestaurantUtil {
    public static List<RestaurantTo> addWithZeroVote(List<Restaurant> restaurants, List<RestaurantTo> restaurantTos) {
        if (restaurantTos == null) {
            return createWithZeroVoteTo(LocalDate.now(), restaurants);
        }
        Map<LocalDate, List<RestaurantTo>> dateListMap = restaurantTos.stream()
                .collect(Collectors.groupingBy(RestaurantTo::getVoteDate));
        List<RestaurantTo> allWithVote = new ArrayList<>();
        for (Map.Entry<LocalDate, List<RestaurantTo>> map : dateListMap.entrySet()) {
            allWithVote.addAll(new ArrayList<>(new HashSet<>(map.getValue()) {{
                addAll(createWithZeroVoteTo(map.getKey(), restaurants));
            }}));
        }
        return allWithVote;
    }
    private static List<RestaurantTo> createWithZeroVoteTo(LocalDate date, List<Restaurant> restaurants) {
        return restaurants.stream().map(r -> new RestaurantTo(r.id(), r.getName(), r.getAddress(), date, 0L))
                .collect(Collectors.toList());
    }
}
