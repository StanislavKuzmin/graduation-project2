package ru.javaops.topjava2.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import ru.javaops.topjava2.HasId;

import java.math.BigDecimal;

@Entity
@Table(name = "dish")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Dish extends NamedEntity implements HasId {

    @Column(name = "price", nullable = false)
    @NotNull
    @Range(max = 5000)
    private BigDecimal price;

    @Column(name = "calories", nullable = false)
    @NotNull
    @Range(min = 10, max = 5000)
    private Integer calories;

    @Column(name = "restaurant_id")
    private Integer restaurantId;

    public Dish(Integer id, String name, BigDecimal price, Integer calories) {
        super(id, name);
        this.price = price;
        this.calories = calories;
    }

    @Override
    public String toString() {
        return "Dish:" + id + '[' + name + ']' + '[' + price + ']' + '[' + calories + ']';
    }
}