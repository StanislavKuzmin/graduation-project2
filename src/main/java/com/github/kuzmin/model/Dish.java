package com.github.kuzmin.model;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import com.github.kuzmin.HasId;

@Entity
@Table(name = "dish")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Dish extends NamedEntity implements HasId {

    @Column(name = "price", nullable = false)
    @NotNull
    @Range(min = 10000, max = 50000000)
    private Integer price;

    @Column(name = "calories", nullable = false)
    @NotNull
    @Range(min = 10, max = 5000)
    private Integer calories;

    @Column(name = "restaurant_id")
    @Hidden
    private Integer restaurantId;

    public Dish(Integer id, String name, Integer price, Integer calories) {
        super(id, name);
        this.price = price;
        this.calories = calories;
    }

    public Dish(Dish d) {
        this(d.id(), d.name, d.price, d.calories);
    }

    @Override
    public String toString() {
        return "Dish:" + id + '[' + name + ']' + '[' + price + ']' + '[' + calories + ']';
    }
}