package ru.javaops.topjava2.to;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.hibernate.validator.constraints.Range;

import java.math.BigDecimal;
import java.time.LocalDate;

@Value
@EqualsAndHashCode(callSuper = true)
public class DishTo extends NamedTo {

    @Column(name = "price", nullable = false)
    @NotNull
    @Range(max = 5000)
    BigDecimal price;

    @Column(name = "calories", nullable = false)
    @NotNull
    @Range(min = 10, max = 5000)
    Integer calories;

    LocalDate date;

    public DishTo(Integer id, String name, BigDecimal price, Integer calories, LocalDate date) {
        super(id, name);
        this.price = price;
        this.calories = calories;
        this.date = date;
    }

    @Override
    public String toString() {
        return "DishTo:" + id + '[' + name + ']' + '[' + price + ']' + '[' + calories + ']';
    }
}
