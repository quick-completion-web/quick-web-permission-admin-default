package tech.guyi.web.quick.permission.admin.defaults.controller.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import tech.guyi.web.quick.core.controller.ResponseContent;
import tech.guyi.web.quick.core.controller.ResponseEntities;
import tech.guyi.web.quick.permission.admin.defaults.exception.NoAuthorizationException;

@ControllerAdvice
public class ExceptionAdvice {


    @ExceptionHandler(NoAuthorizationException.class)
    public ResponseEntity<ResponseContent<Void>> onNoAuthorizationException(NoAuthorizationException e){
        return ResponseEntities._401();
    }

}
