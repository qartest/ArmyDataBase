package boev.app.error.exception;

public class Error500 extends RuntimeException {
    public Error500(String message) {
        super(message);
    }

    public Error500(){
        super("");
    }
}
