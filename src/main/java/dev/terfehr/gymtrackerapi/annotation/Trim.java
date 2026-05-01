package dev.terfehr.gymtrackerapi.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import dev.terfehr.gymtrackerapi.annotation.deserializer.TrimDeserializer;
import tools.jackson.databind.annotation.JsonDeserialize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonDeserialize(using = TrimDeserializer.class)
public @interface Trim {
    boolean trim() default true;
}