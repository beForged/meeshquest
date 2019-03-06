package cmsc420.meeshquest.part2;

public class cityDoesNotExistException extends Throwable {
    public cityDoesNotExistException(String cityOutOfBounds) {
        super(cityOutOfBounds);
    }
}
