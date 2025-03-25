# Inbank Software Engineering Internship 2025 Take Home task

This document summarizes the work completed for the Inbank Software Engineering Internship 2025 take-home task. It covers the validation process, areas for improvement, and the implementation of key requirements from TICKET-101 and TICKET-102.

The task focuses on backend code development using Spring Boot, demonstrating the functionality, improvements, and design decisions made throughout the process.

## Table of Contents

- [Summary of Validation (TICKET-101)](#-summary-of-validation-ticket-101)
- [What Was Done well](#✅-what-was-done-well)
- [Areas of Improvement](#⚠️-areas-for-improvement)
- [Most Critical Shortcoming](#🚨-most-critical-shortcoming)
- [Fix for the Most Critical Issue](#🚀-fix-for-the-most-critical-issue)
- [Implementation of TICKET-102](#🔒-implementation-of-ticket-102)

## 📊 Summary of Validation (TICKET-101)

In this task, we were asked to validate `TICKET-101` and write a conclusion regarding the implementation. The goal was to ensure that the code works as expected, meeting the outlined requirements.

**Validation**:

- **Confirmed whether the code works as intended**: The initial code base ran without errors but was incomplete, as it only returned the input values rather than performing the correct calculations. This was expected, as the task required us to implement the correct logic.
- **Missing credit scoring algorithm**: The credit scoring algorithm was missing in the original implementation, which used a simplified formula to calculate the highest valid loan amount. This was expected as part of the task, where the initial code was intended to lay the foundation, and the actual credit scoring logic was to be implemented as part of the solution.
- **Test coverage and edge case behavior**: The application includes comprehensive tests for both valid and invalid input cases, ensuring that the system handles different scenarios correctly. This includes validating boundary conditions and edge cases where applicable.
- **Functionality meets the requirements**: The application meets the core requirements of `TICKET-101`, with proper API functionality, input validation, and appropriate error handling. The required logic for calculating loan eligibility was added, and edge cases were properly handled.

## ✅ What Was Done Well

1. The code follows _separation of concerns_ well. The controller only handles request routing and delegates business logic to the service layer, keeping responsibilities focused and easier to manage.
2. Custom exception handlers are implemented, centralizing error handling and reducing clutter in the controller.
3. The entire application compiles and runs without issues, and core functionality executes as intended.
4. The main API endpoint handles various input scenarios, returning expected responses and proper error codes.
5. The project structure (e.g. config, exceptions, service) is clear, helping developers locate logic easily.
6. DTOs are used effectively to separate internal logic from external interfaces.
7. Both unit and integration tests cover normal and edge cases, giving confidence in functionality and stability.
8. Lombok is used to reduce repetitive code by automatically generating getters, setters, and constructors, making the code cleaner.

## ⚠️ Areas for Improvement

1. **Naming conventions**:

   a. **`endpoint`**: The name should be `controller` to follow Spring’s standard naming conventions. This improves consistency across Spring-based projects and makes the codebase easier to understand.

   b. **`DecisionRequest.java` and `DecisionResponse.java`**: These are clearly DTOs, and for better organization, they should be placed in a `dto` package and have `DTO` added at the end of their names. This improves clarity and aligns with best practices, especially in larger projects where clear separation of concerns is critical.

   c. **`exceptions`**: The package should be named in singular form. In Java, it’s common to use singular names for packages like `service`, `controller`, and `exception` because each represents a single concept.

   d. **`Decision.java`**: This file looks like a DTO but is ambiguously named, making it unclear whether it’s a model, DTO, or an internal value object. Renaming it to `DecisionDTO.java` and moving it to the `dto` package would improve clarity and better represent its role in the code.

   e. **`DecisionEngine.java`**: The name should follow the convention of including `Service` at the end (e.g., `DecisionEngineService.java`). This clarifies that it’s a service class and aligns with common naming practices in Spring applications.

2. **No input validation in `controller`**: The controller doesn’t use annotations like `@Valid` for input validation. Normally, it’s best to handle validation in the `controller` and DTO layers to make sure the data is correct and prevent issues like unexpected behavior or security problems. Because of time constraints, I didn’t rewrite the validation there since it would also mean changing a lot of the existing tests. I did add example validations in `DecisionRequestDTO.java`.
3. **Try/Catch in the Controller**: The controller currently uses `try/catch` blocks to handle exceptions, which works on its own but isn’t ideal in this context. Since custom exceptions are already created, it would be better to set up a global exception handler to handle errors instead. This would allow the `controller` to focus solely on routing the request and make the code cleaner. By removing the `try/catch` and centralizing error handling in a global handler, the logic becomes easier to maintain and follows best practices.
4. **Inconsistent data types**: The incoming data uses `Long` for `loanAmount` and `int` for `loanPeriod`, but the outgoing data uses `Integer`. This inconsistency isn’t necessary, especially since the values being handled are small numbers that fit comfortably within the range of an `int`. Using `Long` or `Integer` for such small values adds unnecessary complexity.
5. **`null` values in API Responses**: In the current implementation, the response includes `errorMessage: null` even when the request is successful (`HTTP 200`). Similarly, for unsuccessful queries, fields like `loanAmount` and `loanPeriod` are returned as `null`, but with an error message. This creates inconsistency. It’s better to return only the relevant information based on the response. For successful requests, omit unnecessary fields like `errorMessage: null`. For errors, include only the fields necessary to describe the problem, without returning other fields as `null`.
6. **Inconsistent constants**: There is conflicting information regarding the loan period constants. The constant `MAXIMUM_LOAN_PERIOD = 60` in `DecisionEngineConstants.java` aligns with the service comment that the loan period should be between 12 and 60 months. However, the task description specifies a maximum loan period of 48 months, which creates confusion.

## 🚨 Most Critical Shortcoming

The most important shortcoming of `TICKET-101` is that the service layer is doing too much.

The `calculateApprovedLoan` method calculates loan eligibility, validates input, builds the response, and handles credit modification.

Some of these tasks should not be handled in the service layer. For example, input validation (`verifyInputs`) and credit modification (`getCreditModifier`) should not be part of the business logic in the service.

The service layer should focus only on business logic, not on validating input or modifying credit.

**This violates the Single Responsibility Principle (SRP), a key principle of SOLID. When a method takes on too many tasks, it becomes harder to understand, test, and maintain.**

Also, if we add more business logic in the future (like age validation for `TICKET-102`), it will only make the method more complicated and harder to manage.

## 🚀 Fix for the Most Critical Issue

### What I changed:

I created a new package called “validator” and moved the verifyInputs method into a new class called LoanInputValidator.java. This makes the input validation separate from the business logic.

I also moved the `getCreditModifier` method to its own class, `CreditModifierService.java`, which is placed next to the `DecisionEngineService.java`. This makes the code more organized and the `CreditModifier` logic is now easier to manage and reuse.

### Why this fix improves the design/robustness:

By moving validation and credit modification out of the service layer, we now have a cleaner and more focused service class. This makes the code easier to read, test, and maintain.

The `LoanInputValidator` is reusable and if more types of input validation are needed in the future, they can be added easily in one place without changing the service class.

The `CreditModifierService` is now a separate class, which follows the _Single Responsibility Principle_ by handling only one responsibility, modifying the credit. This separation allows each class to be more focused on what it should do.

## 🔒 Implementation of TICKET-102

In this ticket, I updated the `LoanInputValidator` class to add age verification.

I implemented logic to calculate the applicant’s age based on their personal code and check if they are under 18 or exceed the expected life expectancy.

**What was added**:

- **Age calculation**: Extracted the birth year, month, and day from the personal code, built the birthdate, and calculated the applicant’s age.
- **Age validation**: Added checks to ensure the applicant is at least 18 years old and doesn’t exceed the maximum age allowed based on the loan period.
- **`InvalidAgeException`**: Created a new custom exception to handle cases where the applicant is either underage or exceeds the expected life expectancy. This exception is thrown when age validation fails.

This update ensures that age verification is handled correctly, adding another layer of validation while keeping the logic separate and clean.
