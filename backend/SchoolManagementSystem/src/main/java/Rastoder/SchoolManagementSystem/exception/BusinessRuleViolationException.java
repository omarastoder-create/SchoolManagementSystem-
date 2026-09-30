package Rastoder.SchoolManagementSystem.exception;

public class BusinessRuleViolationException extends RuntimeException{

    public BusinessRuleViolationException(String message){
        super(message);
    }
}
