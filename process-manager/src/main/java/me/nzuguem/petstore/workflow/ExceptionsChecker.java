package me.nzuguem.petstore.workflow;

import io.temporal.failure.ApplicationFailure;
import lombok.experimental.UtilityClass;

import java.util.Objects;

@UtilityClass
public class ExceptionsChecker {

    public static boolean isExceptionType(Exception e, Class<?> exceptionClass) {
        Objects.requireNonNull(e, "Exception is required");
        Objects.requireNonNull(exceptionClass, "Exception class is required");

        if (e.getCause() instanceof ApplicationFailure applicationFailure) {
            return Objects.equals(applicationFailure.getType(), exceptionClass.getName());
        }
        return false;
    }
}
