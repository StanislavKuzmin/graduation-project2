package com.github.kuzmin.web.vote;

import com.github.kuzmin.config.TimeProvider;
import com.github.kuzmin.service.VoteService;
import com.github.kuzmin.to.RestaurantVoteTo;
import com.github.kuzmin.to.VoteTo;
import com.github.kuzmin.web.AuthUser;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static java.util.Optional.ofNullable;

@RestController
@RequestMapping(value = VoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@CacheConfig(cacheNames = {"votes"})
@Tag(name = "Voting restaurant system", description = "Vote for the restaurant and get info about results of election today or in the past")
@RequiredArgsConstructor
public class VoteController {

    public static final String REST_URL = "/api/votes";
    private final VoteService voteService;
    private final TimeProvider timeProvider;

    @GetMapping("/today")
    @Cacheable
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

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @CacheEvict(allEntries = true)
    public ResponseEntity<VoteTo> vote(@Valid @RequestBody VoteTo voteTo) {
        int userId = AuthUser.authId();
        return ResponseEntity.of(ofNullable(voteService.vote(voteTo.restaurantId(), userId, voteTo.voteDate())));
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @CacheEvict(allEntries = true)
    public ResponseEntity<VoteTo> changeVote(@Valid @RequestBody VoteTo voteTo) {
        int userId = AuthUser.authId();
        return ResponseEntity.of(ofNullable(voteService.changeVote(voteTo.restaurantId(), userId, voteTo.voteDate())));
    }
}
