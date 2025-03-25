package ee.taltech.inbankbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Holds the response data of the REST endpoint.
 */
@Getter
@AllArgsConstructor
public class DecisionDTO {
    private final Integer loanAmount;
    private final Integer loanPeriod;
    private final String errorMessage;
}
