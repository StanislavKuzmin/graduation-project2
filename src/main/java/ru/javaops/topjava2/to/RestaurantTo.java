package ru.javaops.topjava2.to;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Value;
import ru.javaops.topjava2.util.validation.NoHtml;

@Value
@EqualsAndHashCode(callSuper = true)
public class RestaurantTo extends NamedTo {

    @NotBlank
    @Size(min = 10, max = 200)
    @NoHtml
    String address;

    public RestaurantTo(Integer id, String name, String address) {
        super(id, name);
        this.address = address;
    }

    @Override
    public String toString() {
        return "RestaurantTo:" + id + '[' + name + ']' + '[' + address + ']';
    }
}
