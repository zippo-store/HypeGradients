package me.doublenico.hypegradients.api.packet.annotations;

import me.doublenico.hypegradients.api.MessageDetection;
import me.doublenico.hypegradients.api.packet.enums.DetectionPriority;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to set the priority of a {@link MessageDetection}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Priority {

    /**
     * The priority of the {@link MessageDetection}
     * @return the {@link DetectionPriority}
     */
    DetectionPriority value() default DetectionPriority.NORMAL;
}