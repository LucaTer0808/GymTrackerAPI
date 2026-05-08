package dev.terfehr.gymtrackerapi.dto;

import dev.terfehr.gymtrackerapi.model.Day;
import dev.terfehr.gymtrackerapi.model.Split;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.List;

@NullMarked
public record SplitDTO(long id, String name, List<DayDTO> days) {
    public SplitDTO(Split split) {
        Long id = split.getId();
        assert  id != null;

        List<DayDTO> dayDTOs = new ArrayList<>();

        for (Day day : split.getDays()) {
            dayDTOs.add(new DayDTO(day));
        }

        this(id, split.getName(), dayDTOs);
    }
}
