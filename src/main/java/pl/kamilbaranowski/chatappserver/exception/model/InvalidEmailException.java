package pl.kamilbaranowski.chatappserver.exception.model;

public class InvalidEmailException extends Exception {

    public InvalidEmailException(String errMessage) {
        super(errMessage);
    }
}

