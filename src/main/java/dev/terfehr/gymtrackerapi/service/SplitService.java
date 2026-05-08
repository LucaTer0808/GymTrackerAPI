package dev.terfehr.gymtrackerapi.service;

import dev.terfehr.gymtrackerapi.dto.SplitDTO;
import dev.terfehr.gymtrackerapi.exception.ResourceNotFoundException;
import dev.terfehr.gymtrackerapi.model.Split;
import dev.terfehr.gymtrackerapi.model.User;
import dev.terfehr.gymtrackerapi.repository.SplitRepositoryI;
import dev.terfehr.gymtrackerapi.repository.UserRepositoryI;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@NullMarked
@Transactional
public class SplitService {

    private final UserRepositoryI userRepository;
    private final SplitRepositoryI splitRepository;

    public SplitDTO createSplit(User authUser, String name, List<String> dayNames) {
        Split split = authUser.changeSplit(name, dayNames);
        userRepository.save(authUser);

        return new SplitDTO(split);
    }

    public @Nullable SplitDTO getSplit(User authUser) {
        Split split = authUser.getSplit();

        return split != null ? new SplitDTO(split) : null;
    }

    public SplitDTO changeSplitName(User authUser, @Nullable String name) {
        Split split = authUser.getSplit();

        if (split == null) {
            throw new ResourceNotFoundException("The authenticated user has no split, so its name cannot be changed");
        }

        split.changeName(name);
        splitRepository.save(split);
        return new SplitDTO(split);
    }

    public void deleteSplit(User authUser) {
        authUser.deleteSplit();
        userRepository.save(authUser);
    }
}
