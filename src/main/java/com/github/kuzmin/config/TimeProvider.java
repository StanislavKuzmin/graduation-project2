package com.github.kuzmin.config;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class TimeProvider {

    private final Clock clock;

    public LocalDate getCurrentDate() {
        return LocalDate.now(clock);
    }

    public LocalTime getCurrentTime() {
        return LocalTime.now(clock);
    }
}
