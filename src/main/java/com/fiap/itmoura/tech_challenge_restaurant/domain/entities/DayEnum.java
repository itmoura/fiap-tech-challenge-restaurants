package com.fiap.itmoura.tech_challenge_restaurant.domain.entities;

public enum DayEnum {
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY,
    SUNDAY;

    public static DayEnum fromString(String day) {
        return DayEnum.valueOf(day.toUpperCase());
    }

    public static String toString(DayEnum day) {
        return day.name().toLowerCase();
    }
}
