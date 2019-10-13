package ru.shred.electromachine.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TestTypeAzur {
    RATED_VOLTAGE("Номинальное напряжение"),
    RESISTANCE("Сопротивление"),
    TIME("Время");

    private final String name;
}
