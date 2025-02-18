package com.github.kuzmin.web.vote;

import com.github.kuzmin.config.TimeProvider;
import com.github.kuzmin.service.VoteService;
import com.github.kuzmin.to.RestaurantVoteTo;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.github.kuzmin.to.VoteTo;
import com.github.kuzmin.web.AuthUser;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = VoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@Tag(name = "Voting restaurant system", description = "Vote for the restaurant and get info about results of election today or in the past")
@RequiredArgsConstructor
public class VoteController {

    public static final String REST_URL = "/api/votes";
    private final VoteService voteService;
    private final TimeProvider timeProvider;

    @GetMapping("/today")
    public List<RestaurantVoteTo> getAllVoteToday() {
        log.info("getAllVoteToday");
        return voteService.getAllVoteForRestaurantsByDate(timeProvider.getCurrentDate());
    }

    @GetMapping("/{restaurantId}/today")
    public RestaurantVoteTo getVoteToday(@PathVariable int restaurantId) {
        log.info("get today vote for restaurant with id={}", restaurantId);
        return voteService.getVoteForRestaurantByDate(timeProvider.getCurrentDate(), restaurantId);
    }

    @GetMapping("/{restaurantId}/history")
    public RestaurantVoteTo getVoteByDate(@RequestParam LocalDate date,
                                           @PathVariable int restaurantId) {
        log.info("get vote history for restaurant with id={}", restaurantId);
        return voteService.getVoteForRestaurantByDate(date, restaurantId);
    }

    @PostMapping
    public VoteTo vote(@RequestParam int restaurantId) {
        int userId = AuthUser.authId();
        return voteService.vote(restaurantId, userId);
    }

    @PutMapping
    public VoteTo changeVote(@RequestParam int restaurantId) {
        int userId = AuthUser.authId();
        return voteService.changeVote(restaurantId, userId);
    }
}
