package ee.taltech.inbankbackend.service;

import ee.taltech.inbankbackend.config.DecisionEngineConstants;
import ee.taltech.inbankbackend.dto.DecisionDTO;
import ee.taltech.inbankbackend.exception.NoValidLoanException;
import ee.taltech.inbankbackend.validator.LoanInputValidator;

import org.springframework.stereotype.Service;

@Service
public class DecisionEngineService {

    private final LoanInputValidator validator;
    private final CreditModifierService creditModifierService;
    private int creditModifier = 0;

    public DecisionEngineService(LoanInputValidator validator, CreditModifierService creditModifierService) {
        this.validator = validator;
        this.creditModifierService = creditModifierService;
    }

    public DecisionDTO calculateApprovedLoan(String personalCode, Long loanAmount, int loanPeriod) {
        
        System.out.println("personalCode: " + personalCode + ", loanAmount: " + loanAmount + ", loanPeriod: " + loanPeriod);

        // Validate input parameters
        validator.verifyInputs(personalCode, loanAmount, loanPeriod);
        creditModifier = creditModifierService.getCreditModifier(personalCode);

        // No approved loans with debt
        if (creditModifier == 0) {
            System.out.println("creditModifier: " + creditModifier + " >>> EXITING");
            throw new NoValidLoanException("No valid loan found!");
        }
        System.out.println("creditModifier: " + creditModifier);

        // Start from requested loanPeriod and increase if not approved
        for (int period = loanPeriod; period <= 48; period++) {

            // Start from max loan amount and decrease until approved
            for (int amount = 10000; amount >= 2000; amount -= 100) {
                boolean isApproved = isLoanApproved(creditModifier, amount, period);

                // Return highest approved loan amount for the period
                if (isApproved) {
                    System.out.println("creditModifier: " + creditModifier + ", amount: " + amount + ", period: " + period);
                    System.out.println("isApproved: " + isApproved);
                    return new DecisionDTO(amount, period, null);
                }
            }
        }

        System.out.println("No valid loan found for:");
        System.out.println("---> personalCode: " + personalCode + ", loanAmount: " + loanAmount + ", loanPeriod: " + loanPeriod);
        throw new NoValidLoanException("No valid loan found!");
    }

    private boolean isLoanApproved(int creditModifier, int loanAmount, int loanPeriod) {
        double creditScore = (((double) creditModifier /  loanAmount) * loanPeriod) / 10;
        //System.out.println("creditScore: " + creditScore);
        return creditScore >= 0.1;
    }
}
