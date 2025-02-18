package com.github.kuzmin.web.dish;

import com.github.kuzmin.to.DishTo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import com.github.kuzmin.repository.DishRepository;

import java.util.Arrays;

@Component
@AllArgsConstructor
public class UniqueDishValidator implements org.springframework.validation.Validator {

    public static final String EXCEPTION_DUPLICATE_DISH = "Dish with this the same parameters already exists in restaurant with id=";

    private final DishRepository repository;

    private final HttpServletRequest request;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return DishTo.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        DishTo dishTo = ((DishTo) target);
        String requestUri = request.getRequestURI();
        int restaurantId = Integer.parseInt(Arrays.stream(requestUri.split("\\D+"))
                                                  .filter(s -> s.length() > 0)
                                                  .findFirst().orElse("0"));
        if (repository.exists(dishTo.getName(), restaurantId, dishTo.getPrice())) {
            errors.rejectValue("dish", "", EXCEPTION_DUPLICATE_DISH + restaurantId);
        }
    }
}
