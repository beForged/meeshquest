package cmsc420.meeshquest.part1;

public class cityDoesNotExistException extends Throwable {
    public cityDoesNotExistException(String cityOutOfBounds) {
        super(cityOutOfBounds);
    }
}
