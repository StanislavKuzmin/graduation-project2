package com.github.kuzmin.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.github.kuzmin.util.validation.NoHtml;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Entity
@Table(name = "restaurant")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Restaurant extends NamedEntity {

    @NotBlank
    @Size(max = 200)
    @Column(name = "address", nullable = false)
    @NoHtml
    private String address;

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonManagedReference
    @Hidden
    private List<Dish> dishes;

    public Restaurant(Integer id, String name, String address) {
        super(id, name);
        this.address = address;
    }

    public Restaurant(Restaurant r) {
        this(r.id(), r.name, r.address);
    }

    @Override
    public String toString() {
        return "Restaurant:" + id + '[' + name + ']' + '[' + address + ']';
    }
}
