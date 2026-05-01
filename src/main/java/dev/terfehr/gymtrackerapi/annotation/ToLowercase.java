package dev.terfehr.gymtrackerapi.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import dev.terfehr.gymtrackerapi.annotation.deserializer.LowercaseDeserializer;
import tools.jackson.databind.annotation.JsonDeserialize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonDeserialize(using = LowercaseDeserializer.class)
public @interface ToLowercase {
}
