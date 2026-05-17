package dev.terfehr.gymtrackerapi.dto;

import dev.terfehr.gymtrackerapi.model.Exercise;

import java.time.ZonedDateTime;

public record ExerciseDateTuple(Exercise exercise, ZonedDateTime dateTime) {
}
