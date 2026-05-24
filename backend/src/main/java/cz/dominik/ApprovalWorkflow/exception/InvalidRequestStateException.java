package cz.dominik.ApprovalWorkflow.exception;

public class InvalidRequestStateException extends RuntimeException {

    public InvalidRequestStateException(String message) {
        super(message);
    }

}
