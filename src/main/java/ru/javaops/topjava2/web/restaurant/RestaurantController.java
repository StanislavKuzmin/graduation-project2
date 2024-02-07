package ru.javaops.topjava2.web.restaurant;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
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
import ru.javaops.topjava2.to.RestaurantTo;
import ru.javaops.topjava2.web.AuthUser;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javaops.topjava2.util.DateUtil.atThisDayOrMax;
import static ru.javaops.topjava2.util.DateUtil.atThisDayOrMin;
import static ru.javaops.topjava2.util.RestaurantUtil.addWithZeroVote;
import static ru.javaops.topjava2.util.validation.ValidationUtil.checkNotFoundWithId;

@RestController
@RequestMapping(value = RestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class RestaurantController {
    public static final String REST_URL = "/api/restaurants";
    private static final LocalTime END_VOTE = LocalTime.of(11, 0);
    private final RestaurantRepository restaurantRepository;
    private final VoteRepository voteRepository;
    private final UserRepository userRepository;

    public RestaurantController(RestaurantRepository restaurantRepository, VoteRepository voteRepository, UserRepository userRepository) {
        this.restaurantRepository = restaurantRepository;
        this.voteRepository = voteRepository;
        this.userRepository = userRepository;
    }

    @Cacheable("restaurant")
    @GetMapping
    public List<Restaurant> getAll() {
        log.info("getAll");
        return restaurantRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    @GetMapping("/{id}")
    public Restaurant get(@PathVariable int id) {
        log.info("get {}", id);
        return restaurantRepository.getExisted(id);
    }

    @GetMapping("/{id}/with-dishes")
    public Restaurant getWithDishes(@PathVariable int id) {
        log.info("getWithDishes {}", id);
        return checkNotFoundWithId(restaurantRepository.getWithDishes(id), id);
    }

    @GetMapping("/today-votes")
    public List<RestaurantTo> getAllVoteToday() {
        log.info("getAllVoteToday");
        return addWithZeroVote(restaurantRepository.findAll(), voteRepository.getRestaurantsToday());
    }

    @GetMapping("/{id}/history-votes")
    public List<RestaurantTo> getVoteHistory(@RequestParam @Nullable LocalDate startDate,
                                             @RequestParam @Nullable LocalDate endDate,
                                             @PathVariable int id) {
        log.info("get vote history for restaurant with id={}", id);
        return voteRepository.getRestaurantHistoryBetweenOpen(atThisDayOrMin(startDate), atThisDayOrMax(endDate), id);
    }

    @GetMapping("/by-name")
    public Restaurant getByName(String name) {
        log.info("getByEmail {}", name);
        return restaurantRepository.getExistedByName(name);
    }

    @Transactional
    @PostMapping("/vote")
    public Vote vote(@RequestParam int restaurantId) {
        checkNotFoundWithId(restaurantRepository.getExisted(restaurantId), restaurantId);
        int userId = AuthUser.authId();
        VoteId voteId = new VoteId(userId, LocalDate.now());
        Vote vote = voteRepository.get(voteId);
        if (vote != null && LocalTime.now().isAfter(END_VOTE)) {
            throw new DataConflictException("You can't change your decision, because the time is more than 11.00");
        }
        Restaurant restaurant = restaurantRepository.getReferenceById(restaurantId);
        vote = checkUpdateOrCreate(userId, voteId, vote, restaurant);
        voteRepository.save(vote);
        return vote;
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
