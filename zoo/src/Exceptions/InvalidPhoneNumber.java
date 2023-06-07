package Exceptions;

public class InvalidPhoneNumber extends Exception{
    public InvalidPhoneNumber(){}
    public InvalidPhoneNumber(String message){
        super(message);
    }
}
