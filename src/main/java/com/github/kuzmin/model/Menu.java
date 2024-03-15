package com.github.kuzmin.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "menu")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Menu {

    @EmbeddedId
    private MenuId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("dishId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Dish dish;

    public Menu(Dish dish, LocalDate date) {
        this.id = new MenuId(dish.getId(), date);
        this.dish = dish;
    }

    public Menu(MenuId menuId, Dish dish) {
        this.id = menuId;
        this.dish = dish;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Menu menu = (Menu) o;
        return Objects.equals(id, menu.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
