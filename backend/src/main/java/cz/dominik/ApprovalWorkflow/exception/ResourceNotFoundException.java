package cz.dominik.ApprovalWorkflow.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException (String message){
        super(message);
    }
}
