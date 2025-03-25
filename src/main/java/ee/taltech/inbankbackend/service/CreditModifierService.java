package ee.taltech.inbankbackend.service;

import org.springframework.stereotype.Service;

import ee.taltech.inbankbackend.config.DecisionEngineConstants;

@Service
public class CreditModifierService {
        public int getCreditModifier(String personalCode) {

        int segment = Integer.parseInt(personalCode.substring(personalCode.length() - 4));
        System.out.println("segment: " + segment);

        if (segment < 2500) {
            return 0;
        } else if (segment < 5000) {
            return DecisionEngineConstants.SEGMENT_1_CREDIT_MODIFIER;
        } else if (segment < 7500) {
            return DecisionEngineConstants.SEGMENT_2_CREDIT_MODIFIER;
        }

        return DecisionEngineConstants.SEGMENT_3_CREDIT_MODIFIER;
    }
}
