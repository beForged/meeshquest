package cmsc420.meeshquest.part3;

public class cityDoesNotExistException extends Throwable {
    public cityDoesNotExistException(String cityOutOfBounds) {
        super(cityOutOfBounds);
    }
}
