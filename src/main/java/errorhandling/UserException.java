package errorhandling;


public class UserException extends Exception{
    public static final String IN_USE_USERNAME = "Username is not available";

    public UserException(String message) {
        super(message);
    }

    public UserException() {
        super("Could not be Authenticated");
    }
}