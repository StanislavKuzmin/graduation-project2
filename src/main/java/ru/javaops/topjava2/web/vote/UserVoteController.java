package ru.javaops.topjava2.web.vote;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.javaops.topjava2.model.VoteId;
import ru.javaops.topjava2.repository.VoteRepository;
import ru.javaops.topjava2.to.VoteTo;
import ru.javaops.topjava2.web.AuthUser;
import ru.javaops.topjava2.web.user.ProfileController;

import java.time.LocalDate;
import java.util.List;

import static ru.javaops.topjava2.util.DateUtil.atThisDayOrMax;
import static ru.javaops.topjava2.util.DateUtil.atThisDayOrMin;

@RestController
@RequestMapping(value = ProfileController.REST_URL + UserVoteController.REST_URL)
@Slf4j
public class UserVoteController {

    public static final String REST_URL = "/votes";
    private final VoteRepository voteRepository;

    public UserVoteController(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    @GetMapping("/today")
    public VoteTo getVoteToday() {
        int userId = AuthUser.authId();
        log.info("get vote today for user with id={}", userId);
        return voteRepository.getUserVoteToday(new VoteId(userId, LocalDate.now()));
    }

    @GetMapping("/history")
    public List<VoteTo> getVoteHistory(@RequestParam @Nullable LocalDate startDate,
                                             @RequestParam @Nullable LocalDate endDate) {
        int userId = AuthUser.authId();
        log.info("get vote history for user with id={}", userId);
        return voteRepository.getUserVoteHistoryBetweenOpen(atThisDayOrMin(startDate), atThisDayOrMax(endDate), userId);
    }
}
