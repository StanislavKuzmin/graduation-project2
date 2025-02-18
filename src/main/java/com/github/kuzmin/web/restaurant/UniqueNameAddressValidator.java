package com.github.kuzmin.web.restaurant;

import com.github.kuzmin.repository.RestaurantRepository;
import com.github.kuzmin.to.RestaurantTo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

@Component
@AllArgsConstructor
public class UniqueNameAddressValidator implements org.springframework.validation.Validator {

    public static final String EXCEPTION_DUPLICATE_NAME_ADDRESS = "Restaurant with those name and address already exists";

    private final RestaurantRepository repository;

    private final HttpServletRequest request;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return RestaurantTo.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        RestaurantTo restaurantTo = ((RestaurantTo) target);
        if (StringUtils.hasText(restaurantTo.getName()) && StringUtils.hasText(restaurantTo.getAddress())) {
            repository.findByNameAndAddress(restaurantTo.getName(), restaurantTo.getAddress())
                    .ifPresent(dbRestaurant -> {
                        if (request.getMethod().equals("PUT")) {
                            int dbId = dbRestaurant.id();
                            if (dbRestaurant.getId() != null && dbId == restaurantTo.id()) return;
                            if (request.getRequestURI().endsWith("/" + dbId)) return;
                        }
                        errors.rejectValue("name", "", EXCEPTION_DUPLICATE_NAME_ADDRESS);
                    });
        }
    }
}
