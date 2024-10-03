package ru.gubern.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PaymentDto {
    String firstName;
    String lastName;
}
