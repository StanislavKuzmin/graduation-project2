package com.github.kuzmin.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@Entity
@Table(name = "dish")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Dish extends NamedEntity {

    @Column(name = "price", nullable = false)
    @NotNull
    @Range(min = 10000)
    private Integer price;

    @ManyToOne(optional = false)
    @JoinColumn(name = "restaurant_id")
    @JsonBackReference
    private Restaurant restaurant;

    @Column(name = "is_excluded_from_menu", columnDefinition = "boolean default false")
    private Boolean isExcludedFromMenu;

    public Dish(Integer id, String name, Integer price) {
        super(id, name);
        this.price = price;
        this.isExcludedFromMenu = false;
    }
    public Dish(Integer id, String name, Restaurant restaurant, Integer price) {
        super(id, name);
        this.price = price;
        this.restaurant = restaurant;
        this.isExcludedFromMenu = false;
    }

    public Dish(Dish dish) {
        this(dish.getId(), dish.getName(), dish.getPrice());
    }

    @Override
    public String toString() {
        return "Dish:" + id + '[' + name + ']' + '[' + price + ']';
    }
}