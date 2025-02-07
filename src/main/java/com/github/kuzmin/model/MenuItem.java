package com.github.kuzmin.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "menu")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class MenuItem extends BaseEntity {
    @ManyToOne(optional = false)
    @JoinColumn(name = "dish_id")
    private Dish dish;

    @Column(name = "date")
    @NotNull
    private LocalDate date;

    public MenuItem(Integer id, Dish dish, LocalDate date) {
        this.id = id;
        this.dish = dish;
        this.date = date;
    }
}
