package ee.taltech.inbankbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Holds the request data of the REST endpoint
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DecisionRequestDTO {
    // Define a rough range for personal code length for developed countries
    // @Size(min = 11, max = 14, message = "Invalid personal code")
    // @NotBlank(message = "Invalid personal code")
    private String personalCode;

    // @Min(value = 2000, message = "Invalid loan amount")
    // @Max(value = 10000, message = "Invalid loan amount")
    private Long loanAmount;

    // @Min(value = 12, message = "Invalid loan period")
    // @Max(value = 48, message = "Invalid loan period")
    private int loanPeriod;
}

