package com.github.kuzmin.to;

import com.github.kuzmin.util.validation.NoHtml;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Value;

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
