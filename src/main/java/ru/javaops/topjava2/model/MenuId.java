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
public class MenuId implements Serializable {

    @Column(name = "dish_id")
    private Integer dishId;

    @Column(name = "date")
    private LocalDate date;

    public MenuId(Integer dishId, LocalDate date) {
        this.dishId = dishId;
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuId that = (MenuId) o;
        return Objects.equals(dishId, that.dishId) && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dishId, date);
    }
}
