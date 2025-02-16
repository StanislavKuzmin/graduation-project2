package com.github.kuzmin.to;

import com.github.kuzmin.model.Dish;
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

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @NotNull
    Integer restaurantId;

    @NotNull
    Boolean isExcludedFromMenu;

    public DishTo(Integer id, String name, Integer price, Integer restaurantId, Boolean isExcludedFromMenu) {
        super(id, name);
        this.price = price;
        this.restaurantId = restaurantId;
        this.isExcludedFromMenu = isExcludedFromMenu;
    }

    public static DishTo fromDish(Dish dish) {
        return new DishTo(dish.getId(), dish.getName(), dish.getPrice(), dish.getRestaurant().getId(), dish.getIsExcludedFromMenu());
    }

    @Override
    public String toString() {
        return "DishTo:" + id + '[' + name + ']' + '[' + price + ']';
    }
}
