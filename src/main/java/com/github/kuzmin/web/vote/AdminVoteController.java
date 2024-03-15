package com.github.kuzmin.web.vote;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import com.github.kuzmin.model.VoteId;
import com.github.kuzmin.repository.UserRepository;
import com.github.kuzmin.repository.VoteRepository;
import com.github.kuzmin.to.VoteTo;
import com.github.kuzmin.web.user.AdminUserController;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

import static com.github.kuzmin.util.DateUtil.atThisDayOrMax;
import static com.github.kuzmin.util.DateUtil.atThisDayOrMin;
import static com.github.kuzmin.util.validation.ValidationUtil.checkNotFoundWithId;

@RestController
@RequestMapping(value = AdminUserController.REST_URL + AdminVoteController.REST_URL)
@Slf4j
@Tag(name = "Info about vote of users", description = "Get information about vote of all users")
public class AdminVoteController {

    public static final String REST_URL = "/{id}/votes";
    private final UserRepository userRepository;
    private final VoteRepository voteRepository;
    private final Clock clock;

    public AdminVoteController(UserRepository userRepository, VoteRepository voteRepository, Clock clock) {
        this.userRepository = userRepository;
        this.voteRepository = voteRepository;
        this.clock = clock;
    }

    @GetMapping("/today")
    public VoteTo getUserVoteToday(@PathVariable int id) {
        checkNotFoundWithId(userRepository.getExisted(id), id);
        log.info("get vote today for user with id={}", id);
        return voteRepository.getUserVoteToday(new VoteId(id, LocalDate.now(clock)));
    }

    @GetMapping("/history")
    public List<VoteTo> getUserVoteHistory(@RequestParam @Nullable LocalDate startDate,
                                                 @RequestParam @Nullable LocalDate endDate,
                                                 @PathVariable int id) {
        checkNotFoundWithId(userRepository.getExisted(id), id);
        log.info("get vote history for user with id={}", id);
        return voteRepository.getUserVoteHistoryBetweenOpen(atThisDayOrMin(startDate), atThisDayOrMax(endDate), id);
    }
}
