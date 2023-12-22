package com.aurine.cloudx.open.common.core.exception;

import com.aurine.cloudx.open.common.core.response.Result;
import com.pig4cloud.pigx.common.core.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = SQLIntegrityConstraintViolationException.class)
    public R violationException(SQLIntegrityConstraintViolationException e){
        log.error("走到了GlobalExceptionHandler异常处理器",e);
        return Result.fail(null, CloudxOpenErrorEnum.REPEATING_DATA);
    }
}
