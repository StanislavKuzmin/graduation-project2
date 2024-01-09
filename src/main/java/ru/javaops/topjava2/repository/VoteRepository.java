package ru.javaops.topjava2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.topjava2.model.Vote;
import ru.javaops.topjava2.model.VoteId;

@Transactional(readOnly = true)
public interface VoteRepository extends JpaRepository<Vote, VoteId> {


}
