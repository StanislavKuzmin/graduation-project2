package com.github.kuzmin.service;

import com.github.kuzmin.config.TimeProvider;
import com.github.kuzmin.error.DataConflictException;
import com.github.kuzmin.model.Restaurant;
import com.github.kuzmin.model.Vote;
import com.github.kuzmin.repository.RestaurantRepository;
import com.github.kuzmin.repository.UserRepository;
import com.github.kuzmin.repository.VoteRepository;
import com.github.kuzmin.to.RestaurantVoteTo;
import com.github.kuzmin.to.UserVoteTo;
import com.github.kuzmin.to.VoteTo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.github.kuzmin.to.VoteTo.fromEntity;
import static com.github.kuzmin.util.validation.ValidationUtil.checkNotFoundWithId;

@Service
@RequiredArgsConstructor
@Slf4j
public class VoteServiceImpl implements VoteService {
    private static final LocalTime END_VOTE = LocalTime.of(11, 0);
    private final VoteRepository voteRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final TimeProvider timeProvider;

    @Override
    public List<RestaurantVoteTo> getAllVoteForRestaurantsByDate(LocalDate date) {
        Map<Restaurant, List<Vote>> restaurantVoteMap = voteRepository.getRestaurantsVotesByDate(date).stream()
                .collect(Collectors.groupingBy(Vote::getRestaurant));
        return restaurantRepository.findAll().stream()
                .map(restaurant -> restaurantVoteMap.getOrDefault(restaurant, List.of(new Vote(null, null, restaurant, date))))
                .map(RestaurantVoteTo::fromEntities)
                .toList();
    }

    @Override
    public RestaurantVoteTo getVoteForRestaurantByDate(LocalDate date, int restaurantId) {
        return RestaurantVoteTo.fromEntities(voteRepository.getRestaurantVotesByDate(restaurantId, date));
    }

    @Override
    public VoteTo vote(int restaurantId, int userId, LocalDate date) {
        checkIsDateBefore(date);
        Optional<Vote> optVote = voteRepository.getUserVoteByDate(date, userId);
        if (optVote.isEmpty()) {
            Restaurant restaurant = checkNotFoundWithId(restaurantRepository.getReferenceById(restaurantId), restaurantId);
            log.info("user with id={} first time vote for restaurant with id={} to date={}", userId, restaurantId, date);
            return fromEntity(voteRepository.save(new Vote(null, userRepository.getReferenceById(userId),
                    restaurant,
                    date)));
        }
        return null;
    }

    @Override
    public VoteTo changeVote(int restaurantId, int userId, LocalDate date) {
        checkIsDateBefore(date);
        if (date.isEqual(timeProvider.getCurrentDate()) && timeProvider.getCurrentTime().isAfter(END_VOTE)) {
            throw new DataConflictException("You can't change your decision, because the time is more than 11.00");
        }
        Optional<Vote> optVote = voteRepository.getUserVoteByDate(date, userId);
        if (optVote.isPresent()) {
            Restaurant restaurant = checkNotFoundWithId(restaurantRepository.getReferenceById(restaurantId), restaurantId);
            log.info("user with id={} changed his voice to restaurant with id={} to date={}", userId, restaurantId, date);
            Vote vote = optVote.get();
            vote.setRestaurant(restaurant);
            return fromEntity(voteRepository.save(vote));
        }
        return null;
    }

    @Override
    public UserVoteTo getUserVoteByDate(LocalDate date, int userId) {
        Optional<Vote> optVote = voteRepository.getUserVoteByDate(date, userId);
        return optVote.map(UserVoteTo::fromEntity).orElse(null);
    }

    private void checkIsDateBefore(LocalDate date) {
        LocalDate currentDate = timeProvider.getCurrentDate();
        if (date.isBefore(currentDate)) {
            throw new DataConflictException("Date is before than current date");
        }
    }
}
