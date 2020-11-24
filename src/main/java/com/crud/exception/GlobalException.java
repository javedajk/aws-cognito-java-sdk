package com.crud.exception;

import com.amazonaws.services.cognitoidp.model.CodeMismatchException;
import com.amazonaws.services.cognitoidp.model.InvalidPasswordException;
import com.amazonaws.services.cognitoidp.model.LimitExceededException;
import com.amazonaws.services.cognitoidp.model.NotAuthorizedException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ValidationException;

import java.util.Date;

@ControllerAdvice
public class GlobalException extends ResponseEntityExceptionHandler {
    public GlobalException() {
        super();
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        System.out.println("Exception >> MethodNotSupportedException ");
        ErrorResponse error = new ErrorResponse();
        error.setTimestamp(new Date());
        error.setStatus_code((HttpStatus.METHOD_NOT_ALLOWED.value()));
        error.setStatus((false));
        error.setError_msg(exception.getLocalizedMessage());
        error.setMessage(" Server Error, please try again");
        return new ResponseEntity<>(error, HttpStatus.METHOD_NOT_ALLOWED);
    }

   @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        System.out.println("Exception >> MediaTypeNotSupportedException "+ exception.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse();
        error.setTimestamp(new Date());
        error.setStatus_code((HttpStatus.UNSUPPORTED_MEDIA_TYPE.value()));
        error.setStatus((false));
        error.setError_msg(exception.getLocalizedMessage());
        error.setMessage(" Server Error, please try again");
        return new ResponseEntity<>(error, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        System.out.println("Exception >> MediaTypeNotAcceptable "+ exception.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse();
        error.setTimestamp(new Date());
        error.setStatus_code((HttpStatus.NOT_ACCEPTABLE.value()));
        error.setStatus((false));
        error.setError_msg(exception.getLocalizedMessage());
        error.setMessage(" Server Error, please try again");
        return new ResponseEntity<>(error, HttpStatus.NOT_ACCEPTABLE);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        System.out.println("Exception >> NoHandlerFoundException ");
        ErrorResponse error = new ErrorResponse();
        error.setTimestamp(new Date());
        error.setStatus_code((HttpStatus.NOT_FOUND.value()));
        error.setStatus((false));
        error.setError_msg(exception.getLocalizedMessage());
        error.setMessage(" Server Error, please try again");
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleAsyncRequestTimeoutException(AsyncRequestTimeoutException exception, HttpHeaders headers, HttpStatus status, WebRequest webRequest) {
        System.out.println("Exception >> RequestTimeoutException: " + exception.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse();
        error.setTimestamp(new Date());
        error.setStatus_code((HttpStatus.REQUEST_TIMEOUT.value()));
        error.setStatus((false));
        error.setMessage(" Server Error, please try again");
        error.setError_msg(exception.getLocalizedMessage());
        return new ResponseEntity<>(error, HttpStatus.REQUEST_TIMEOUT);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception exception, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        System.out.println("Exception >> InvalidRequestException: " + exception.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse();
        error.setTimestamp(new Date());
        error.setStatus_code((HttpStatus.INTERNAL_SERVER_ERROR.value()));
        error.setStatus((false));
        error.setMessage(" Server Error, please try again");
        error.setError_msg(exception.getLocalizedMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = RecordNotFoundException.class)
    public ResponseEntity<?> resourceNotFoundException(RecordNotFoundException exception) {
        System.out.println("Exception >> RecordNotFoundException: "+ exception.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse();
        error.setTimestamp(new Date());
        error.setStatus_code((HttpStatus.NOT_FOUND.value()));
        error.setStatus((false));
        error.setMessage(" Record details Not Found");
        error.setError_msg(exception.getLocalizedMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(value = InvalidOldPasswordException.class)
    public ResponseEntity<?> InvalidOldPasswordException(InvalidOldPasswordException exception) {
        System.out.println("Exception >> InvalidOldPasswordException: "+ exception.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse();
        error.setTimestamp(new Date());
        error.setStatus_code((HttpStatus.NOT_FOUND.value()));
        error.setStatus((false));
        error.setMessage("Incorrect Old Password.");
        error.setError_msg(exception.getLocalizedMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = ValidationException.class)
    public ResponseEntity<?> dbValidationException(ValidationException exception) {
        System.out.println("Exception >> DB Column Size Exceeded: "+ exception.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse();
        error.setTimestamp(new Date());
        error.setStatus_code((HttpStatus.BAD_REQUEST.value()));
        error.setStatus((false));
        error.setMessage("Field Size Exceeded, Should not be more than 400KB ");
        error.setError_msg(exception.getLocalizedMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = CodeMismatchException.class)
    public ResponseEntity<?> verifyCode(CodeMismatchException exception) {
        System.out.println("Exception >> Mis-Match Verification Code: "+ exception.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse();
        error.setTimestamp(new Date());
        error.setStatus_code((HttpStatus.BAD_REQUEST.value()));
        error.setStatus((false));
        error.setMessage("Verification Code is Not Correct ");
        error.setError_msg(exception.getLocalizedMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = LimitExceededException.class)
    public ResponseEntity<?> smsLimitExceeded(LimitExceededException exception) {
        System.out.println("Exception >> Request Attempt limit exceeded : "+ exception.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse();
        error.setTimestamp(new Date());
        error.setStatus_code((HttpStatus.BAD_REQUEST.value()));
        error.setStatus((false));
        error.setMessage("Request limit exceeded, please try after some time. ");
        error.setError_msg(exception.getLocalizedMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(value = InvalidPasswordException.class)
    public ResponseEntity<ErrorResponse> invalidPasswordPolicy(InvalidPasswordException exception) {
        System.out.println("Exception >> InvalidPasswordException: " + exception.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse();
            System.out.println("InvalidPasswordException: Password must be atleast 8 Charaters in size with number, special character, Upper Case and Lower Case");
            error.setTimestamp(new Date());
            error.setStatus_code((HttpStatus.BAD_REQUEST.value()));
            error.setStatus((false));
            error.setMessage("Password must be of size 8 Charaters having Number, Special Character, Upper Case and Lower Case");
            error.setError_msg(exception.getLocalizedMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(value = NotAuthorizedException.class)
    public ResponseEntity<ErrorResponse> unAuthorizedUser(NotAuthorizedException exception) {
        System.out.println("Exception >> NotAuthorizedException: " + exception.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse();
        if(exception.getLocalizedMessage().contains("Incorrect username or password")){
            System.out.println("Incorrect username or password");
            error.setTimestamp(new Date());
            error.setStatus_code((HttpStatus.BAD_REQUEST.value()));
            error.setStatus((false));
            error.setMessage("Incorrect username or password");
            error.setError_msg(exception.getLocalizedMessage());
        }
        else if(exception.getLocalizedMessage().contains("Token has expired")){
            System.out.println("Session has expired, Please try again after login");
            error.setTimestamp(new Date());
            error.setStatus_code((HttpStatus.BAD_REQUEST.value()));
            error.setStatus((false));
            error.setMessage("Session has expired, Please try again after login");
            error.setError_msg(exception.getLocalizedMessage());
        }
        else if(exception.getLocalizedMessage().contains("Token has been revoked")){
            System.out.println("You already logged-out, Please login");
            error.setTimestamp(new Date());
            error.setStatus_code((HttpStatus.BAD_REQUEST.value()));
            error.setStatus((false));
            error.setMessage("You already logged-out, Please login");
            error.setError_msg(exception.getLocalizedMessage());
        }
        else{
            System.out.println("UnKnown Error: unAuthorizedUser ");
            error.setTimestamp(new Date());
            error.setStatus_code((HttpStatus.BAD_REQUEST.value()));
            error.setStatus((false));
            error.setMessage(" Server Error, please try again ");
            error.setError_msg(exception.getLocalizedMessage());
        }
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> globalExceptionHandling(Exception exception){
        System.out.println("Exception >> Global: " + exception.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse();
        error.setTimestamp(new Date());
        error.setStatus_code((HttpStatus.INTERNAL_SERVER_ERROR.value()));
        error.setStatus((false));
        error.setError_msg(exception.getLocalizedMessage());
        error.setMessage("Server Error, please try again");
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<?> handleError405(InvalidRequestException exception) {
        System.out.println("Exception >> InvalidRequestException: " + exception.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse();
        error.setTimestamp(new Date());
        error.setStatus_code((HttpStatus.INTERNAL_SERVER_ERROR.value()));
        error.setStatus((false));
        error.setMessage("Server Error, please try again");
        error.setError_msg(exception.getLocalizedMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}