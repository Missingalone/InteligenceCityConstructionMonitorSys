package com.cyx.exception;

import com.cyx.result.Result;
import com.cyx.Enums.ResultCodeEnum;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusException.class)
    public Result<Void> handleBusException(BusException exception) {
        return Result.failure(exception.getCode(), exception.getMessage());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class, HttpMessageNotReadableException.class})
    public Result<Void> handleValidationException(Exception exception) {
        return Result.failure(ResultCodeEnum.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception exception) {
        return Result.failure(ResultCodeEnum.INTERNAL_SERVER_ERROR);
    }
}
