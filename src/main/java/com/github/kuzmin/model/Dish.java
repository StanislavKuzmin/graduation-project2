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
public class Dish extends NamedEntity {

    @Column(name = "price", nullable = false)
    @NotNull
    @Range(min = 10000)
    private Integer price;

    @Column(name = "restaurant_id")
    @Hidden
    private Integer restaurantId;

    public Dish(Integer id, String name, Integer price) {
        super(id, name);
        this.price = price;
    }

    public Dish(Dish d) {
        this(d.id(), d.name, d.price);
    }

    @Override
    public String toString() {
        return "Dish:" + id + '[' + name + ']' + '[' + price + ']';
    }
}