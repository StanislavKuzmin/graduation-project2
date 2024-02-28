package ru.javaops.topjava2.util;

import lombok.experimental.UtilityClass;
import ru.javaops.topjava2.model.Restaurant;
import ru.javaops.topjava2.model.Vote;
import ru.javaops.topjava2.to.VoteTo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@UtilityClass
public class VoteUtil {
    public static List<VoteTo> addWithZeroVote(List<Restaurant> restaurants, List<VoteTo> voteTos) {
        if (voteTos == null) {
            return createWithZeroVoteTo(LocalDate.now(), restaurants);
        }
        Map<LocalDate, List<VoteTo>> dateListMap = voteTos.stream()
                .collect(Collectors.groupingBy(VoteTo::voteDate));
        List<VoteTo> allWithVote = new ArrayList<>();
        for (Map.Entry<LocalDate, List<VoteTo>> map : dateListMap.entrySet()) {
            allWithVote.addAll(new ArrayList<>(new HashSet<>(map.getValue()) {{
                addAll(createWithZeroVoteTo(map.getKey(), restaurants));
            }}));
        }
        return allWithVote;
    }
    private static List<VoteTo> createWithZeroVoteTo(LocalDate date, List<Restaurant> restaurants) {
        return restaurants.stream().map(r -> new VoteTo(date, r))
                .collect(Collectors.toList());
    }

    public static VoteTo createTo(Vote vote) {
        return new VoteTo(vote.getId().getVoteDate(), vote.getUser(), vote.getRestaurant());
    }
}
