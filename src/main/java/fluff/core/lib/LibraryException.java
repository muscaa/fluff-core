package fluff.core.lib;

public class LibraryException extends Exception {
	
    private static final long serialVersionUID = -8193416847606969193L;
    
    public LibraryException() {
        super();
    }
    
    public LibraryException(String message) {
        super(message);
    }
    
    public LibraryException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public LibraryException(Throwable cause) {
        super(cause);
    }
}
