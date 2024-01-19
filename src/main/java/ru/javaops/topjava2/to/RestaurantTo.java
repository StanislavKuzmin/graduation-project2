package ru.javaops.topjava2.to;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Value;
import ru.javaops.topjava2.util.validation.NoHtml;

import java.time.LocalDate;

@Value
@EqualsAndHashCode(callSuper = true)
public class RestaurantTo extends NamedTo {

    @NotBlank
    @Size(min = 10, max = 200)
    @NoHtml
    String address;

    LocalDate voteDate;

    Long votesNumber;

    public RestaurantTo(Integer id, String name, String address, LocalDate voteDate, Long votesNumber) {
        super(id, name);
        this.address = address;
        this.voteDate = voteDate;
        this.votesNumber = votesNumber;
    }

    @Override
    public String toString() {
        return "RestaurantTo:" + id + '[' + name + ']' + '[' + address + ']';
    }
}
