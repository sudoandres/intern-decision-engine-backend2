package ee.taltech.inbankbackend.controller;

import ee.taltech.inbankbackend.dto.DecisionDTO;
import ee.taltech.inbankbackend.dto.DecisionRequestDTO;
import ee.taltech.inbankbackend.dto.DecisionResponseDTO;
import ee.taltech.inbankbackend.service.DecisionEngineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/loan")
@CrossOrigin
public class DecisionEngineController {

    private final DecisionEngineService decisionEngine;

    @Autowired
    DecisionEngineController(DecisionEngineService decisionEngine) {
        this.decisionEngine = decisionEngine;
    }

    @PostMapping("/decision")
    public ResponseEntity<DecisionResponseDTO> requestDecision(@RequestBody DecisionRequestDTO request) {

        DecisionDTO decision = decisionEngine.calculateApprovedLoan(
            request.getPersonalCode(), 
            request.getLoanAmount(), 
            request.getLoanPeriod()
        );

        DecisionResponseDTO response = new DecisionResponseDTO();
            response.setLoanAmount(decision.getLoanAmount());
            response.setLoanPeriod(decision.getLoanPeriod());
            response.setErrorMessage(null);

        return ResponseEntity.ok(response);
    }
}
