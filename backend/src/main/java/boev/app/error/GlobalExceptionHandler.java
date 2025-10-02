package boev.app.error;

import boev.app.error.exception.Error404;
import boev.app.error.exception.Error500;
import boev.app.payload.utils.MyError;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<MyError> httpMessageNotRead(HttpMessageNotReadableException ex){
        return new ResponseEntity<>(new MyError("Input error"), HttpStatusCode.valueOf(404));
    }

    @ExceptionHandler(Error404.class)
    public ResponseEntity<MyError> badData(Error404 er){
        return new ResponseEntity<>(new MyError(er.getMessage()), HttpStatusCode.valueOf(404));
    }

    @ExceptionHandler(Error500.class)
    public ResponseEntity<MyError> problemServer(Error500 er){
        return new ResponseEntity<>(new MyError("Problem with server"), HttpStatusCode.valueOf(500));
    }
}
