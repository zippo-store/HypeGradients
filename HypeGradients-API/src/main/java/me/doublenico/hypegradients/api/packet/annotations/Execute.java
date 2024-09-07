package me.doublenico.hypegradients.api.packet.annotations;

import me.doublenico.hypegradients.api.packet.enums.DetectionExecution;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to determine when the detection should be executed
 * <p>
 * {@link DetectionExecution#BEFORE} -> Execute the detection before the gradient is detected
 * <p>
 * {@link DetectionExecution#AFTER} -> Execute the detection after the gradient is detected
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Execute {

    /**
     * Execute the detection before the gradient is detected, see {@link DetectionExecution}
     */
    DetectionExecution value() default DetectionExecution.AFTER;
}
