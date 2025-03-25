package ee.taltech.inbankbackend.validator;

import java.time.LocalDate;
import java.time.Period;
import org.springframework.stereotype.Component;
import com.github.vladislavgoltjajev.personalcode.locale.estonia.EstonianPersonalCodeValidator;
import ee.taltech.inbankbackend.config.DecisionEngineConstants;
import ee.taltech.inbankbackend.exception.InvalidAgeException;
import ee.taltech.inbankbackend.exception.InvalidLoanAmountException;
import ee.taltech.inbankbackend.exception.InvalidLoanPeriodException;
import ee.taltech.inbankbackend.exception.InvalidPersonalCodeException;

@Component
public class LoanInputValidator {

    private final EstonianPersonalCodeValidator validator = new EstonianPersonalCodeValidator();

    public void verifyInputs(String personalCode, Long loanAmount, int loanPeriod) {

        if (!validator.isValid(personalCode)) {
            throw new InvalidPersonalCodeException("Invalid personal ID code!");
        }

        if (!(DecisionEngineConstants.MINIMUM_LOAN_AMOUNT <= loanAmount)
                || !(loanAmount <= DecisionEngineConstants.MAXIMUM_LOAN_AMOUNT)) {
            throw new InvalidLoanAmountException("Invalid loan amount!");
        }
        if (!(DecisionEngineConstants.MINIMUM_LOAN_PERIOD <= loanPeriod)
                || !(loanPeriod <= DecisionEngineConstants.MAXIMUM_LOAN_PERIOD)) {
            throw new InvalidLoanPeriodException("Invalid loan period!");
        }

        // Extract and verify age
        int centuryIndicator = Character.getNumericValue(personalCode.charAt(0));
        int yearIndicator = Integer.parseInt(personalCode.substring(1,3));
        int day = Integer.parseInt(personalCode.substring(5, 7));
        int month = Integer.parseInt(personalCode.substring(3,5));
        int year;
        int lifeExpectancy = 80; // Made up age
        
        // Build birth year
        if (centuryIndicator == 3 || centuryIndicator == 4) {
            year = 1900 + yearIndicator;
        } else if (centuryIndicator == 5 || centuryIndicator == 6) {
            year = 2000 + yearIndicator;
        } else if (centuryIndicator == 7 || centuryIndicator == 8) {
            year = 2100 + yearIndicator;
        } else {
            throw new InvalidPersonalCodeException("Unknown century indicator in personal code");
        }

        // Build birthday
        LocalDate birthDate = LocalDate.of(year, month, day);

        // Calculate age
        int age = Period.between(birthDate, LocalDate.now()).getYears();

        System.out.println("Applicant age: " + age + " years old");

        if (age < 18) {
            throw new InvalidAgeException("Applicant is underage.");
        }
        if (age > lifeExpectancy - (DecisionEngineConstants.MAXIMUM_LOAN_PERIOD / 12 )) {
            throw new InvalidAgeException("Applicant exceeds max age.");
        }
        
    }
}
