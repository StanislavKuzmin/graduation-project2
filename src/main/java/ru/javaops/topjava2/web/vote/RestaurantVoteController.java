package ru.javaops.topjava2.web.vote;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.javaops.topjava2.error.DataConflictException;
import ru.javaops.topjava2.model.Restaurant;
import ru.javaops.topjava2.model.Vote;
import ru.javaops.topjava2.model.VoteId;
import ru.javaops.topjava2.repository.RestaurantRepository;
import ru.javaops.topjava2.repository.UserRepository;
import ru.javaops.topjava2.repository.VoteRepository;
import ru.javaops.topjava2.to.VoteTo;
import ru.javaops.topjava2.web.AuthUser;
import ru.javaops.topjava2.web.restaurant.RestaurantController;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javaops.topjava2.util.DateUtil.atThisDayOrMax;
import static ru.javaops.topjava2.util.DateUtil.atThisDayOrMin;
import static ru.javaops.topjava2.util.VoteUtil.addWithZeroVote;
import static ru.javaops.topjava2.util.validation.ValidationUtil.checkNotFoundWithId;

@RestController
@RequestMapping(value = RestaurantController.REST_URL + RestaurantVoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@Tag(name = "Voting restaurant system", description = "Vote for the restaurant and get info about results of election today or in the past")
public class RestaurantVoteController {

    public static final String REST_URL = "/votes";
    private static final LocalTime END_VOTE = LocalTime.of(11, 0);
    private final RestaurantRepository restaurantRepository;
    private final VoteRepository voteRepository;
    private final UserRepository userRepository;
    private final CacheManager cacheManager;
    private final Clock clock;

    public RestaurantVoteController(RestaurantRepository restaurantRepository,
                                    VoteRepository voteRepository,
                                    UserRepository userRepository,
                                    CacheManager cacheManager,
                                    Clock clock) {
        this.restaurantRepository = restaurantRepository;
        this.voteRepository = voteRepository;
        this.userRepository = userRepository;
        this.cacheManager = cacheManager;
        this.clock = clock;
    }

    @GetMapping("/today")
    public List<VoteTo> getAllVoteToday() {
        log.info("getAllVoteToday");
        return addWithZeroVote(restaurantRepository.findAll(), voteRepository.getRestaurantsToday(LocalDate.now(clock)));
    }

    @GetMapping("/{id}/history")
    public List<VoteTo> getVoteHistory(@RequestParam @Nullable LocalDate startDate,
                                       @RequestParam @Nullable LocalDate endDate,
                                       @PathVariable int id) {
        log.info("get vote history for restaurant with id={}", id);
        return voteRepository.getRestaurantHistoryBetweenOpen(atThisDayOrMin(startDate), atThisDayOrMax(endDate), id);
    }

    @Transactional
    @PostMapping
    public Vote vote(@RequestParam int restaurantId) {
        Restaurant restaurant = checkNotFoundWithId(getFromCacheOrRepo(restaurantId), restaurantId);
        int userId = AuthUser.authId();
        VoteId voteId = new VoteId(userId, LocalDate.now(clock));
        Vote vote = voteRepository.get(voteId);
        if (vote != null && LocalTime.now(clock).isAfter(END_VOTE)) {
            throw new DataConflictException("You can't change your decision, because the time is more than 11.00");
        }
        vote = checkUpdateOrCreate(userId, voteId, vote, restaurant);
        return voteRepository.save(vote);
    }

    private Restaurant getFromCacheOrRepo(int restaurantId) {
        Cache cache = cacheManager.getCache("restaurant");
        if (cache.get(restaurantId) == null) {
            Restaurant restaurant = restaurantRepository.getExisted(restaurantId);
            cache.put(restaurantId, restaurant);
            return restaurant;
        }
        return cache.get(restaurantId, Restaurant.class);
    }

    private Vote checkUpdateOrCreate(int userId, VoteId voteId, Vote vote, Restaurant restaurant) {
        if (vote != null) {
            log.info("user with id={} changed his voice for restaurant with id={}", userId, restaurant.id());
            vote.setRestaurant(restaurant);
        } else {
            log.info("user with id={} first time vote for restaurant with id={}", userId, restaurant.id());
            vote = new Vote(voteId);
            vote.setUser(userRepository.getReferenceById(userId));
            vote.setRestaurant(restaurant);
        }
        return vote;
    }
}
