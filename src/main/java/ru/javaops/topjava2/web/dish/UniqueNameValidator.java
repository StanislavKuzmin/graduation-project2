package ru.javaops.topjava2.web.dish;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import ru.javaops.topjava2.model.Dish;
import ru.javaops.topjava2.repository.DishRepository;

import java.util.Arrays;
import java.util.Objects;

@Component
@AllArgsConstructor
public class UniqueNameValidator implements org.springframework.validation.Validator {

    public static final String EXCEPTION_DUPLICATE_NAME = "Dish with this name already exists in restaurant with id=";

    private final DishRepository repository;

    private final HttpServletRequest request;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return Dish.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        Dish dish = ((Dish) target);
        String requestUri = request.getRequestURI();
        int restaurantId = Integer.parseInt(Arrays.stream(requestUri.split("\\D+"))
                                                  .filter(s -> s.length() > 0)
                                                  .findFirst().orElse("0"));
        if (StringUtils.hasText(dish.getName())) {
            repository.findByName(dish.getName(), restaurantId)
                    .ifPresent(dbDish -> {
                        if (request.getMethod().equals("PUT")) {
                            int dbId = dbDish.id();
                            if (dish.getId() != null && dbId == dish.id() && Objects.equals(dish.getRestaurantId(), dbDish.getRestaurantId()))
                                return;
                            if (requestUri.endsWith("/" + dbId) && requestUri.contains("/restaurants/" + restaurantId)) return;
                        }
                        errors.rejectValue("name", "", EXCEPTION_DUPLICATE_NAME + restaurantId);
                    });
        }
    }
}
