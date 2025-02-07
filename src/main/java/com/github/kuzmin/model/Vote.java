package com.github.kuzmin.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "vote")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Vote extends BaseEntity {
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @Column(name = "vote_date")
    @NotNull
    private LocalDate voteDate;

    public Vote(Integer id, User user, Restaurant restaurant, LocalDate voteDate) {
        this.id = id;
        this.user = user;
        this.restaurant = restaurant;
        this.voteDate = voteDate;
    }
}
