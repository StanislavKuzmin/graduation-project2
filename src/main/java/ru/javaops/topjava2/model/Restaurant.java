package ru.javaops.topjava2.model;

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
import ru.javaops.topjava2.HasId;
import ru.javaops.topjava2.util.validation.NoHtml;

import java.util.List;

@Entity
@Table(name = "restaurant")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Restaurant extends NamedEntity implements HasId {

    @NotBlank
    @Size(min = 10, max = 200)
    @Column(name = "address", nullable = false)
    @NoHtml
    private String address;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval=true)
    @JoinColumn(name = "restaurant_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
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
