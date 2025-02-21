package com.github.kuzmin.web.vote;

import com.github.kuzmin.config.TimeProvider;
import com.github.kuzmin.service.VoteService;
import com.github.kuzmin.to.UserVoteTo;
import com.github.kuzmin.web.AuthUser;
import com.github.kuzmin.web.user.ProfileController;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

import static java.util.Optional.ofNullable;

@RestController
@RequestMapping(value = ProfileController.REST_URL + UserVoteController.REST_URL)
@Slf4j
@Tag(name = "Info about vote of profile", description = "Get information about vote of profile")
@RequiredArgsConstructor
public class UserVoteController {

    public static final String REST_URL = "/votes";
    private final VoteService voteService;
    private final TimeProvider timeProvider;

    @GetMapping("/today")
    public UserVoteTo getVoteToday() {
        int userId = AuthUser.authId();
        log.info("get vote today for user with id={}", userId);
        return voteService.getUserVoteByDate(timeProvider.getCurrentDate(), userId);
    }

    @GetMapping("/history")
    public ResponseEntity<UserVoteTo> getVoteByDate(@RequestParam LocalDate date) {
        int userId = AuthUser.authId();
        log.info("get vote history for user with id={}", userId);
        return ResponseEntity.of(ofNullable(voteService.getUserVoteByDate(date, userId)));
    }
}
