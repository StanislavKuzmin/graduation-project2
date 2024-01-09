package ru.javaops.topjava2.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class VoteId implements Serializable {

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "vote_date")
    private LocalDate voteDate;

    public VoteId(Integer userId, LocalDate voteDate) {
        this.userId = userId;
        this.voteDate = voteDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VoteId that = (VoteId) o;
        return Objects.equals(userId, that.userId) && Objects.equals(voteDate, that.voteDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, voteDate);
    }
}
