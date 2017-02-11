package com.vaka.hotel_manager.repository.exception;

import com.vaka.hotel_manager.util.exception.RepositoryException;
import lombok.Getter;

/**
 * Created by Iaroslav on 2/2/2017.
 */
@Getter
public class ConstraintViolationException extends RepositoryException {
    private ConstraintViolationType violationType;
    private String violatedField;

    public ConstraintViolationException(ConstraintViolationType violationType, String violatedField) {
        this.violationType = violationType;
        this.violatedField = violatedField;
    }

    public ConstraintViolationException(String message, ConstraintViolationType violationType, String violatedField) {
        super(message);
        this.violationType = violationType;
        this.violatedField = violatedField;
    }

    public ConstraintViolationException(String message, Throwable cause, ConstraintViolationType violationType, String violatedField) {
        super(message, cause);
        this.violationType = violationType;
        this.violatedField = violatedField;
    }
}
