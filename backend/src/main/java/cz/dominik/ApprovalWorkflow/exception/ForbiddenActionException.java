package cz.dominik.ApprovalWorkflow.exception;

public class ForbiddenActionException extends RuntimeException {

    public ForbiddenActionException(String message) {
        super(message);
    }

}
