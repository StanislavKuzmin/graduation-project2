package ru.javaops.topjava2.to;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.hibernate.validator.constraints.Range;

@Value
@EqualsAndHashCode(callSuper = true)
public class DishTo extends NamedTo {

    @Column(name = "price", nullable = false)
    @NotNull
    @Range(max = 5000)
    Integer price;

    @Column(name = "calories", nullable = false)
    @NotNull
    @Range(min = 10, max = 5000)
    Integer calories;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    Integer restaurantId;

    public DishTo(Integer id, String name, Integer price, Integer calories, Integer restaurantId) {
        super(id, name);
        this.price = price;
        this.calories = calories;
        this.restaurantId = restaurantId;
    }

    @Override
    public String toString() {
        return "DishTo:" + id + '[' + name + ']' + '[' + price + ']' + '[' + calories + ']';
    }
}
