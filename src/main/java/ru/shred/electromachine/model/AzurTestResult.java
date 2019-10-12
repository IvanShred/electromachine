package ru.shred.electromachine.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AzurTestResult {
    private Long id;

    private Long protocolAzurId;

    private String testType;

    private String parameters;

    private double norm;

    private double result;

    private String conclusion;

    private String notation;
}
