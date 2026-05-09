package dev.terfehr.gymtrackerapi.annotation.validator;

import dev.terfehr.gymtrackerapi.annotation.ValidExecutionSetNumbers;
import dev.terfehr.gymtrackerapi.dto.request.CreateExecutionSetRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;
import java.util.Set;

public class ExecutionSetNumbersValidator
        implements ConstraintValidator<
        ValidExecutionSetNumbers,
        List<CreateExecutionSetRequest>> {

    @Override
    public boolean isValid(
            List<CreateExecutionSetRequest> sets,
            ConstraintValidatorContext context
    ) {

        if (sets == null || sets.isEmpty()) {
            return true;
        }

        List<Integer> numbers = sets.stream()
                .map(CreateExecutionSetRequest::numberInExecution)
                .toList();

        Set<Integer> unique = Set.copyOf(numbers);
        if (unique.size() != numbers.size()) {
            return false;
        }

        List<Integer> sorted = numbers.stream().sorted().toList();

        for (int i = 0; i < sorted.size(); i++) {
            if (sorted.get(i) != i + 1) {
                return false;
            }
        }

        return true;
    }
}