package com.github.kuzmin.web.vote;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.github.kuzmin.model.VoteId;
import com.github.kuzmin.repository.VoteRepository;
import com.github.kuzmin.to.VoteTo;
import com.github.kuzmin.web.AuthUser;
import com.github.kuzmin.web.user.ProfileController;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

import static com.github.kuzmin.util.DateUtil.atThisDayOrMax;
import static com.github.kuzmin.util.DateUtil.atThisDayOrMin;

@RestController
@RequestMapping(value = ProfileController.REST_URL + UserVoteController.REST_URL)
@Slf4j
@Tag(name = "Info about vote of profile", description = "Get information about vote of profile")
public class UserVoteController {

    public static final String REST_URL = "/votes";
    private final VoteRepository voteRepository;
    private final Clock clock;

    public UserVoteController(VoteRepository voteRepository, Clock clock) {
        this.voteRepository = voteRepository;
        this.clock = clock;
    }

    @GetMapping("/today")
    public VoteTo getVoteToday() {
        int userId = AuthUser.authId();
        log.info("get vote today for user with id={}", userId);
        return voteRepository.getUserVoteToday(new VoteId(userId, LocalDate.now(clock)));
    }

    @GetMapping("/history")
    public List<VoteTo> getVoteHistory(@RequestParam @Nullable LocalDate startDate,
                                       @RequestParam @Nullable LocalDate endDate) {
        int userId = AuthUser.authId();
        log.info("get vote history for user with id={}", userId);
        return voteRepository.getUserVoteHistoryBetweenOpen(atThisDayOrMin(startDate), atThisDayOrMax(endDate), userId);
    }
}
