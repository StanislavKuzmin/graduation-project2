package ru.javaops.topjava2.model;

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
@Table(name = "restaurant_dish")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class DishDate {

    @EmbeddedId
    private DishDateId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("dishId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Dish dish;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurant_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Restaurant restaurant;

    public DishDate(Dish dish, LocalDate date) {
        this.id = new DishDateId(dish.getId(), date);
        this.dish = dish;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DishDate dishDate = (DishDate) o;
        return Objects.equals(id, dishDate.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
