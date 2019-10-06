package ru.shred.electromachine.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProtocolAzur {
    private Long id;

    private String protocolNumber;

    private String measurementPurpose;

    private String documentsNumber;

    private String resultVisualInspection;

    private String climateData;
}
