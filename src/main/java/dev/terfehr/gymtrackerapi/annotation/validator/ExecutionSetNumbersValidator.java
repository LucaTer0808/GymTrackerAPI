package dev.terfehr.gymtrackerapi.annotation.validator;

import dev.terfehr.gymtrackerapi.annotation.ValidExecutionSetNumbers;
import dev.terfehr.gymtrackerapi.dto.request.ExecutionSetRequestDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;
import java.util.Set;

public class ExecutionSetNumbersValidator
        implements ConstraintValidator<
        ValidExecutionSetNumbers,
        List<ExecutionSetRequestDTO>> {

    @Override
    public boolean isValid(
            List<ExecutionSetRequestDTO> sets,
            ConstraintValidatorContext context
    ) {

        if (sets == null || sets.isEmpty()) {
            return true;
        }

        List<Integer> numbers = sets.stream()
                .map(ExecutionSetRequestDTO::numberInExecution)
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